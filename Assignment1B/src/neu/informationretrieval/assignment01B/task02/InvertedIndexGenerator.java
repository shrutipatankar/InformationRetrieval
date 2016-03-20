package neu.informationretrieval.assignment01B.task02;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private Map<String, Integer> wordOccurances;
	private File corpusFolderPath;

	public InvertedIndexGenerator() {
		invertedIndexOneGram = new HashMap<String, List<Index>>();
		invertedIndexBiGrams = new HashMap<String, List<Index>>();
		invertedIndexTriGrams = new HashMap<String, List<Index>>();
		corpusFolderPath = new File("OutputRawFiles");
		wordOccurances = new HashMap<String, Integer>();
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
		//createWORDIndex("OutputRawFiles/100%25renewableenergy.txt","100%25renewableenergy.txt");
		writeHashMap();
	}

	private void createWORDIndex(String url, String filename) {
		String text = "";
		try {
			text = getFileText(url);
			// remove extra whitespace from the text
			text = text.replaceAll("\\s+", " ");
			text = text.trim();

			long startTime = System.currentTimeMillis();
			populateHashMap(filename, text);
			long endTime = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			System.out.println("populate Hash map time=" + totalTime);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void populateHashMap(String docID, String text) {
		// Populate One Gram Hash Map
		String oneGrams[] = createNGrams(1, text);
		countWordOccurances(oneGrams);
		populateOneGramHashMap(docID);
		
		String biGrams[] = createNGrams(2, text);
		/*for(String bigram : biGrams){
			System.out.println(bigram);
		}*/
		countWordOccurances(biGrams);
		populateBiGramHashMap(docID);
		
		String triGrams[] = createNGrams(3, text);
		/*for (String triGram : triGrams) {
			System.out.println(triGram);
		}*/
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
		
		/*for (Map.Entry<String, Integer> entry : wordOccurances.entrySet()) {
			System.out.println(entry.getKey() + " " + entry.getValue());
		}*/
		
	}

}
