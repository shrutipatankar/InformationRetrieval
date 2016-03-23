package neu.informationretrieval.assignment01B.task02;

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
import java.util.TreeMap;


/**
 * 
 * @author shruti This class creates an inverted index for the Corpus generated
 *         by GenerateCorpus class. It stores it in following format: WORD ->
 *         (docID, tf),(docID, tf),.. The inverted index is eventually output to
 *         a inverted_Index.txt file
 * 
 *         [NOTE] A WORD can be defined as: A word n-gram, and n = 1, 2, and 3
 */
public class InvertedIndexGenerator {

	private Map<String, List<Index>> invertedIndexOneGram;
	private Map<String, List<Index>> invertedIndexBiGrams;
	private Map<String, List<Index>> invertedIndexTriGrams;
	
	private Map<String, Integer> termFrequencyOneGram;
	private Map<String, Integer> termFrequencyBiGram;
	private Map<String, Integer> termFrequencyTriGram;
	
	
	private Map<String, Integer> wordOccurances;
	private Map<String, Integer> numberOfOneGramTokensPerDocument;
	private Map<String, Integer> numberOfBiGramTokensPerDocument;
	private Map<String, Integer> numberOfTriGramTokensPerDocument;
	
	private Map<String, Integer> docIDHashcode;
	
	private File corpusFolderPath;
	
	private String resultPath;

	public InvertedIndexGenerator() {
		invertedIndexOneGram = new HashMap<String, List<Index>>();
		invertedIndexBiGrams = new HashMap<String, List<Index>>();
		invertedIndexTriGrams = new HashMap<String, List<Index>>();
		
		termFrequencyOneGram = new HashMap<String, Integer>();
		termFrequencyBiGram = new HashMap<String, Integer>();
		termFrequencyTriGram = new HashMap<String, Integer>();
		
		
		numberOfOneGramTokensPerDocument = new HashMap<String, Integer>();
		numberOfBiGramTokensPerDocument = new HashMap<String, Integer>();
		numberOfTriGramTokensPerDocument = new HashMap<String, Integer>();
		
		docIDHashcode = new HashMap<String, Integer>();
		
		corpusFolderPath = new File("OutputRawFiles");
		wordOccurances = new HashMap<String, Integer>();
		
		resultPath = "Results/";
	}

	public void generateInvertedIndex() {
		File[] listOfFiles = corpusFolderPath.listFiles();
		for (File file : listOfFiles) {
			if (file.isFile()) {
				System.out.println("Processing File: " + file.getName());
				createWORDIndex("OutputRawFiles" + "/" + file.getName(),
						file.getName());
			}
		}
		writeHashMap();
		generateStatistics();
	}
	
	private void generateStatistics(){
		calculateTermFreqTable();
		writeTermFreqToFile();
		
		calculateDocFreqTable(resultPath + "docFrequencyForOneGram.txt", invertedIndexOneGram);
		calculateDocFreqTable(resultPath + "docFrequencyForBiGram.txt", invertedIndexBiGrams);
		calculateDocFreqTable(resultPath + "docFrequencyForTriGram.txt", invertedIndexTriGrams);
	}
	
	private void calculateDocFreqTable(String filename, Map<String, List<Index>> hashMap){
		
		// Doc Freq for OneGram
		PrintWriter writer;
		File file = new File(filename);
		
		Map<String, List<Index>> map = new TreeMap<String, List<Index>>(hashMap);
		hashMap = map;
		
		try {
			writer = new PrintWriter(file, "UTF-8");
			for (Map.Entry<String, List<Index>> entry : hashMap.entrySet()) {
				String docIDs = "";
				int count = entry.getValue().size();
				for (Index temp : entry.getValue()) {
					docIDs = docIDs + temp.getDocId() + " ";
				}
				
				String termIndex = entry.getKey() + " " + count + " " + docIDs;
				writer.println(termIndex);
			}
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
	}
	

	private void writeTermFreqToFile() {
		
		writeHashMapToFile(resultPath + "termFrequencyOneGram.txt", termFrequencyOneGram);
		writeHashMapToFile(resultPath + "termFrequencyBiGram.txt", termFrequencyBiGram);
		writeHashMapToFile(resultPath + "termFrequencyTriGram.txt", termFrequencyTriGram);
	}
	
	
	private void writeHashMapToFile(String filename, Map<String, Integer> hashMap){
		PrintWriter writer;
		File file = new File(filename);
		try {
			writer = new PrintWriter(file, "UTF-8");
			for (Map.Entry<String, Integer> entry : hashMap
					.entrySet()) {
				String termIndex = entry.getKey() + " " + entry.getValue();
				writer.println(termIndex);
			}
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	private void calculateTermFreqTable() {

		// calculate termFrequencyforOneGram
		for (Map.Entry<String, List<Index>> entry : invertedIndexOneGram
				.entrySet()) {
			int count = 0;
			for (Index temp : entry.getValue()) {
				count = count + temp.getTermFrequency();
			}
			termFrequencyOneGram.put(entry.getKey(), count);
		}
		termFrequencyOneGram = sortByValue(termFrequencyOneGram);

		// calculate termFrequencyforBiGram
		for (Map.Entry<String, List<Index>> entry : invertedIndexBiGrams
				.entrySet()) {
			int count = 0;
			for (Index temp : entry.getValue()) {
				count = count + temp.getTermFrequency();
			}
			termFrequencyBiGram.put(entry.getKey(), count);
		}
		termFrequencyBiGram = sortByValue(termFrequencyBiGram);
		
		// calculate termFrequencyforTriGram
		for (Map.Entry<String, List<Index>> entry : invertedIndexTriGrams
				.entrySet()) {
			int count = 0;
			for (Index temp : entry.getValue()) {
				count = count + temp.getTermFrequency();
			}
			termFrequencyTriGram.put(entry.getKey(), count);
		}
		termFrequencyTriGram = sortByValue(termFrequencyTriGram);

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

	private void createWORDIndex(String url, String filename) {
		String text = "";
		try {
			text = getFileText(url);
			// remove extra whitespace from the text
			text = text.replaceAll("\\s+", " ");
			text = text.trim();

			populateHashMap(filename, text);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void populateHashMap(String docID, String text) {
		// Populate One Gram Hash Map
		String oneGrams[] = createNGrams(1, text);
		numberOfOneGramTokensPerDocument.put(docID, oneGrams.length);
		countWordOccurances(oneGrams);
		populateOneGramHashMap(docID);
		
		String biGrams[] = createNGrams(2, text);
		numberOfBiGramTokensPerDocument.put(docID, biGrams.length);
		countWordOccurances(biGrams);
		populateBiGramHashMap(docID);
		
		String triGrams[] = createNGrams(3, text);
		numberOfTriGramTokensPerDocument.put(docID, triGrams.length);
		countWordOccurances(triGrams);
		populateTriGramHashMap(docID);

	}
	
	private void populateTriGramHashMap(String docID){
		for (Map.Entry<String, Integer> entry : wordOccurances.entrySet()) {
			Index tempIndex = new Index();
			tempIndex.setDocId(docID);
			tempIndex.setTermFrequency(entry.getValue());
			if (!invertedIndexTriGrams.containsKey(entry.getKey())) {
				List<Index> indexPerWordList = new ArrayList<Index>();
				indexPerWordList.add(tempIndex);
				invertedIndexTriGrams.put(entry.getKey(), indexPerWordList);
			} else {
				List<Index> indexPerWordList = invertedIndexTriGrams.get(entry
						.getKey());
				indexPerWordList.add(tempIndex);
				invertedIndexTriGrams.put(entry.getKey(), indexPerWordList);
			}
		}
	}
	
	private void populateBiGramHashMap(String docID){
		for (Map.Entry<String, Integer> entry : wordOccurances.entrySet()) {
			Index tempIndex = new Index();
			tempIndex.setDocId(docID);
			tempIndex.setTermFrequency(entry.getValue());
			if (!invertedIndexBiGrams.containsKey(entry.getKey())) {
				List<Index> indexPerWordList = new ArrayList<Index>();
				indexPerWordList.add(tempIndex);
				invertedIndexBiGrams.put(entry.getKey(), indexPerWordList);
			} else {
				List<Index> indexPerWordList = invertedIndexBiGrams.get(entry
						.getKey());
				indexPerWordList.add(tempIndex);
				invertedIndexBiGrams.put(entry.getKey(), indexPerWordList);
			}
		}
	}

	private String[] createNGrams(int n, String text) {
		String words[] = new String[0];
		
		if (n == 1) {
			words = text.split(" ");
			
		} else if (n == 2) {
			String temp[] = text.split(" ");
			List<String> bigrams = new ArrayList<String>();
			for (int i = 0; i < temp.length - 1; i++) {
				String bigram = temp[i] + " " + temp[i + 1];
				bigrams.add(bigram);
			}
			words = bigrams.toArray(new String[0]);
		} else if (n == 3) {
			String temp[] = text.split(" ");
			List<String> trigrams = new ArrayList<String>();
			for (int i = 0; i < temp.length - 2; i++) {
				String trigram = temp[i] + " " + temp[i + 1] + " "
						+ temp[i + 2];
				trigrams.add(trigram);
			}
			words = trigrams.toArray(new String[0]);
		} else {
			System.out.println("irrelevent");
		}
		return words;
	}

	private void populateOneGramHashMap(String docID) {
		for (Map.Entry<String, Integer> entry : wordOccurances.entrySet()) {
			Index tempIndex = new Index();
			tempIndex.setDocId(docID);
			tempIndex.setTermFrequency(entry.getValue());
			if (!invertedIndexOneGram.containsKey(entry.getKey())) {
				List<Index> indexPerWordList = new ArrayList<Index>();
				indexPerWordList.add(tempIndex);
				invertedIndexOneGram.put(entry.getKey(), indexPerWordList);
			} else {
				List<Index> indexPerWordList = invertedIndexOneGram.get(entry
						.getKey());
				indexPerWordList.add(tempIndex);
				invertedIndexOneGram.put(entry.getKey(), indexPerWordList);
			}
		}
	}

	private void writeHashMap() {
		writeOneGramHashMap();
		writeBiGramHashMap();
		writeTriGramHashMap();
	}
	
	private void writeOneGramHashMap(){
		File hashMap = new File("invertedIndexOneGram.txt");
		PrintWriter writer;
		try {
			writer = new PrintWriter(hashMap, "UTF-8");
			for (Map.Entry<String, List<Index>> entry : invertedIndexOneGram
					.entrySet()) {
				String wordIndex = entry.getKey() + " | ";
				List<Index> tempIndexList = new ArrayList<Index>();
				tempIndexList = entry.getValue();
				for (Index index : tempIndexList) {
					wordIndex = wordIndex + " || " + index.getDocId() + " "
							+ index.getTermFrequency();
				}
				writer.println(wordIndex);
			}
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	private void writeBiGramHashMap(){
		File hashMap = new File("invertedIndexBiGrams.txt");
		PrintWriter writer;
		try {
			writer = new PrintWriter(hashMap, "UTF-8");
			for (Map.Entry<String, List<Index>> entry : invertedIndexBiGrams
					.entrySet()) {
				String wordIndex = entry.getKey() + " | ";
				List<Index> tempIndexList = new ArrayList<Index>();
				tempIndexList = entry.getValue();
				for (Index index : tempIndexList) {
					wordIndex = wordIndex + " || " + index.getDocId() + " "
							+ index.getTermFrequency();
				}
				writer.println(wordIndex);
			}
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	private void writeTriGramHashMap(){
		File hashMap = new File("invertedIndexTriGrams.txt");
		PrintWriter writer;
		try {
			writer = new PrintWriter(hashMap, "UTF-8");
			for (Map.Entry<String, List<Index>> entry : invertedIndexTriGrams
					.entrySet()) {
				String wordIndex = entry.getKey() + " | ";
				List<Index> tempIndexList = new ArrayList<Index>();
				tempIndexList = entry.getValue();
				for (Index index : tempIndexList) {
					wordIndex = wordIndex + " || " + index.getDocId() + " "
							+ index.getTermFrequency();
				}
				writer.println(wordIndex);
			}
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	

	private String getFileText(String url) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(url));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line = br.readLine();
			}
			return sb.toString();
		} finally {
			br.close();
		}
	}

	private void countWordOccurances(String words[]) {
		wordOccurances.clear();
		for (String word : words) {
			if (!wordOccurances.containsKey(word)) {
				wordOccurances.put(word, 1);
			} else {
				wordOccurances.put(word, wordOccurances.get(word) + 1);
			}
		}
	}

}
