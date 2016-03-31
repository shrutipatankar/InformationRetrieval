/**
 * 
 */
package neu.informationretrieval.assignment03.task01.relevencemodels;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shruti
 *
 */
public class BM25 {

	private double k1;
	private int k2;
	private double b;
	private Map<Integer, String> hasCodeDocIds;
	private Map<Integer, Integer> numOfTokensPerDoc;
	private Map<String, List<Index>> invertedIndex;
	private double AVDL;
	private Map<String, Integer> queryFrequency;
	private Map<Integer, Double> bm25Scores;
	final static Logger logger = LoggerFactory.getLogger(BM25.class);

	BM25() {
		k1 = 1.2;
		b = 0.75;
		k2 = 100;
		hasCodeDocIds = new HashMap<Integer, String>();
		numOfTokensPerDoc = new HashMap<Integer, Integer>();
		invertedIndex = new HashMap<String, List<Index>>();
		queryFrequency = new HashMap<String, Integer>();
		AVDL = 0;
		bm25Scores = new HashMap<Integer, Double>();

	}

	public void rankDocuments(String query) {
		readAllFilesInHashMaps();
		calculateAVDL();
		calculateBM25PageRanksForQuery(query);
	}

	private void calculateBM25PageRanksForQuery(String query) {
		// remove extra white spaces in the query
		query = query.trim();
		query = query.replaceAll("\\s+", " ");
		
		
		String queryWords[] = query.split(" ");
		int queryNumber = Integer.parseInt(queryWords[0]);
		
		queryWords = filterQueryWords(queryWords);
		
		calculateQueryFrequency(queryWords);
		for (int i = 0; i < queryWords.length; i++) {
			calculateBM25ScorePerTerm(queryWords[i]);
		}
		queryFrequency.clear();
		bm25Scores = sortByValue(bm25Scores);
		printbm25Scores(queryNumber, queryWords);
		bm25Scores.clear();
	}

	private String[] filterQueryWords(String queryWords[]) {
		String temp[] = new String[queryWords.length-1];
		int count = 0;
		for (int i = 1; i < queryWords.length ; i++) {
			temp[count] = queryWords[i];
			count++;
		}
		return temp;
	}

	private void calculateBM25ScorePerTerm(String term) {
		List<Index> termInvertedIndex = new ArrayList<Index>();
		termInvertedIndex = invertedIndex.get(term);
		int ni = termInvertedIndex.size();
		int N = numOfTokensPerDoc.size();
		int qfi = queryFrequency.get(term);
		for (Index index : termInvertedIndex) {
			logger.info("Processing doc: "
					+ hasCodeDocIds.get(index.getDocId()));
			int docLength = numOfTokensPerDoc.get(index.getDocId());
			int fi = index.getTermFrequency();
			populatebm25Scores(index.getDocId(),
					calculateScore(ni, N, qfi, fi, calculateK(docLength)));
		}
	}

	private void populatebm25Scores(int docId, double score) {
		if (bm25Scores.containsKey(docId)) {
			bm25Scores.put(docId, bm25Scores.get(docId) + score);
			logger.info("bm25Score: " + bm25Scores.get(docId));
		} else {
			bm25Scores.put(docId, score);
			logger.info("bm25Score: " + bm25Scores.get(docId));
		}

	}

	private double calculateK(int docLength) {
		logger.info("Doc length = " + docLength);
		double K = k1
				* ((double) ((double) (1 - b) + (b * (double) (docLength / AVDL))));
		return K;
	}

	private double calculateScore(int ni, int N, int qfi, int fi, double K) {
		logger.info("ni = " + ni);
		logger.info("N = " + N);
		logger.info("qfi = " + qfi);
		logger.info("fi = " + fi);
		logger.info("K = " + K);
		double term1 = (double) ((N - ni + 0.5) / (ni + 0.5));
		logger.info("term1 = " + term1);
		double term2 = (double) (((k1 + 1) * fi) / (K + fi));
		logger.info("term2 = " + term2);
		double term3 = (double) (((k2 + 1) * qfi) / (k2 + qfi));
		logger.info("term3 = " + term3);
		logger.info("Multiplication = " + (term1 * term2 * term3));
		double score = Math.log((term1 * term2 * term3));
		logger.info("Score = " + score);
		return score;
	}

	private void calculateQueryFrequency(String queryWords[]) {
		for (int i = 0; i < queryWords.length; i++) {
			if (queryFrequency.containsKey(queryWords[i])) {
				queryFrequency.put(queryWords[i],
						queryFrequency.get(queryWords[i]) + 1);
			} else {
				queryFrequency.put(queryWords[i], 1);
			}
		}
	}

	private String createOutputFileName(String queryWords[]) {
		String fileName = "bm25";
		for (int i = 0; i < queryWords.length; i++) {
			fileName = fileName + "_" + queryWords[i];
		}
		fileName = fileName + ".txt";
		return fileName;
	}

	private void printbm25Scores(int queryNum, String queryWords[]) {
		// Write to file
		PrintWriter writer;
		File file = new File(createOutputFileName(queryWords));
		try {
			writer = new PrintWriter(file, "UTF-8");
			int count = 0;
			for (Map.Entry<Integer, Double> entry : bm25Scores.entrySet()) {
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append(queryNum + " ");
				stringBuilder.append("Q0 ");
				stringBuilder.append(hasCodeDocIds.get(entry.getKey()) + " ");
				stringBuilder.append((count + 1) + " ");
				stringBuilder.append(entry.getValue() + " " + "BM25Run");
				writer.println(stringBuilder);
				count++;
				if (count == 100) {
					break;
				}
			}
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(
			Map<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			@Override
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		Map<K, V> result = new LinkedHashMap<>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	private void calculateAVDL() {
		int countOfAllTokensInAllDocuments = 0;
		for (Map.Entry<Integer, Integer> entry : numOfTokensPerDoc.entrySet()) {
			countOfAllTokensInAllDocuments = countOfAllTokensInAllDocuments
					+ entry.getValue();
		}

		AVDL = ((double) countOfAllTokensInAllDocuments)
				/ ((double) numOfTokensPerDoc.size());
		// System.out.println("AVDL = " + AVDL);
	}

	private void readAllFilesInHashMaps() {
		String fileName;
		String line;
		FileReader fileReader;

		try {
			// read numOfTokens file;
			fileName = "Input/numOfTokensPerDoc.txt";
			line = null;
			fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				String split[] = line.split(" ");
				numOfTokensPerDoc.put(Integer.parseInt(split[0]),
						Integer.parseInt(split[1]));
			}
			bufferedReader.close();
			// System.out.println(numOfTokensPerDoc.size());

			// read hashCodeToDocIds
			fileName = "Input/hashCodeToDocIds.txt";
			line = null;
			fileReader = new FileReader(fileName);
			bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				String split[] = line.split(" ");
				hasCodeDocIds.put(Integer.parseInt(split[1]), split[0]);
			}
			bufferedReader.close();
			// System.out.println(hasCodeDocIds.size());

			// read invertedIndex
			fileName = "Input/invertedIndexOneGram.txt";
			line = null;
			fileReader = new FileReader(fileName);
			bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				line = line.trim();
				String split[] = line.split(" ");

				String key = split[0];

				List<Index> indexList = new ArrayList<Index>();
				for (int i = 1; i < split.length - 2; i = i + 2) {
					Index termIndex = new Index();
					termIndex.setDocId(Integer.parseInt(split[i]));
					termIndex.setTermFrequency(Integer.parseInt(split[i + 1]));
					indexList.add(termIndex);
				}
				invertedIndex.put(key, indexList);
			}
			bufferedReader.close();
			// System.out.println(invertedIndex.size());

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
