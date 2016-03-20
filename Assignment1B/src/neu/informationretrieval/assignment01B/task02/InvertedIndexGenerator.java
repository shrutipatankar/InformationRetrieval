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
 * @author shruti
 * This class creates an inverted index for the Corpus generated 
 * by GenerateCorpus class. It stores it in following format:
 * WORD	-> (docID,	tf),(docID,	tf),..
 * The inverted index is eventually output to a inverted_Index.txt
 * file
 * 
 * [NOTE] A WORD can be defined as:
 * A word n-gram, and n = 1, 2,	and	3
 */
public class InvertedIndexGenerator {
	
	private Map<String,List<Index>> invertedIndex;
	private Map<String,Integer> wordOccurances;
	private File corpusFolderPath;
	
	public InvertedIndexGenerator() {
		invertedIndex = new HashMap<String, List<Index>>();
		corpusFolderPath = new File("OutputRawFiles");
		wordOccurances = new HashMap<String, Integer>();
	}
	
	public void generateInvertedIndex(){
		File[] listOfFiles = corpusFolderPath.listFiles();
		for (File file : listOfFiles) {
			if (file.isFile()) {
				System.out.println("Processing File: "+file.getName());
				createWORDIndex("OutputRawFiles" + "/" + file.getName(),file.getName());
			}
		}
		writeHashMap();
	}
	
	private void createWORDIndex(String url,String filename){		
		String text = "";
		try {
			text = getFileText(url);
			// remove extra whitespace from the text
			text = text.replaceAll("\\s+", " ");
			text = text.trim();
			
			String oneGrams[] = text.split(" ");
			long startTime = System.currentTimeMillis();
			populateHashMap(oneGrams,filename,text);
			long endTime   = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			System.out.println("populate Hash map time="+totalTime);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void populateHashMap(String oneGrams[], String docID, String text) {
		
		countWordOccurances(text);
		
		for (Map.Entry<String, Integer> entry : wordOccurances.entrySet()) {
			Index tempIndex = new Index();
			tempIndex.setDocId(docID);
			tempIndex.setTermFrequency(entry.getValue());
			if (!invertedIndex.containsKey(entry.getKey())) {
				List<Index> indexPerWordList = new ArrayList<Index>();
				indexPerWordList.add(tempIndex);
				invertedIndex.put(entry.getKey(),indexPerWordList);
			}else{
				List<Index> indexPerWordList = invertedIndex.get(entry.getKey());
				indexPerWordList.add(tempIndex);
				invertedIndex.put(entry.getKey(), indexPerWordList);
			}
		}
		
		/*for (String oneGram : oneGrams) {
			Index tempIndex = new Index();
			tempIndex.setDocId(docID);
			tempIndex.setTermFrequency(wordOccurances.get(oneGram));
			if (!invertedIndex.containsKey(oneGram)) {
				Set<Index> indexPerWordList = new HashSet<Index>();
				indexPerWordList.add(tempIndex);
				invertedIndex.put(oneGram,indexPerWordList);
			}else{
				Set<Index> indexPerWordList = invertedIndex.get(oneGram);
				indexPerWordList.add(tempIndex);
				invertedIndex.put(oneGram, indexPerWordList);
			}
		}*/
	}
	
	private void writeHashMap() {
		File hashMap = new File("invertedIndex.txt");
		PrintWriter writer;
		try {
			writer = new PrintWriter(hashMap, "UTF-8");
			for (Map.Entry<String, List<Index>> entry : invertedIndex.entrySet()) {
				String wordIndex = entry.getKey() + " | ";
				List<Index> tempIndexList = new ArrayList<Index>();
				tempIndexList = entry.getValue();		
				for(Index index : tempIndexList){
					wordIndex = wordIndex + " || " + index.getDocId() + " " + index.getTermFrequency();
				}
				writer.println(wordIndex);
			}
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	private String getFileText(String url) throws IOException{
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
	
	private void countWordOccurances(String text) {
		wordOccurances.clear();
		String words[] = text.split(" ");
		for(String word : words){
			if(!wordOccurances.containsKey(word)){
				wordOccurances.put(word, 1);
			}else{
				wordOccurances.put(word, wordOccurances.get(word) + 1);
			}
		}
	}

}
