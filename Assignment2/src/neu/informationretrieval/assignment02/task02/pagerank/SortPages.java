package neu.informationretrieval.assignment02.task02.pagerank;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SortPages {

	private Map<String, Double> pagesList;
	final static Logger logger = LoggerFactory.getLogger(WebGraph.class);
	private BufferedReader bufferedReader;
	private FileReader fileReader;

	SortPages() {
		try {
			pagesList = new HashMap<String, Double>();
			fileReader = new FileReader("Output/PageRank.txt");
			bufferedReader = new BufferedReader(fileReader);
		} catch (Exception e) {
			logger.info("Error " + e);
		}
	}

	public void sortAllPagesbyPageRank() {
		try{
		PrintWriter pw = new PrintWriter("Output/SortedPageRank.txt");
		Map<String, Double> sortedList;
		loadDataIntoHashMap();
		sortedList = sortByValue(pagesList);
		for (Map.Entry<String, Double> entry : sortedList.entrySet()) {
			pw.write(entry.getKey().toString() + " "
					+ entry.getValue().toString() + "\n");
		}
		pw.close();
		}catch(Exception e){
			logger.info("Error occured while writing to a file: "+e);
		}
		
	}

	private void loadDataIntoHashMap() {
		logger.info("Loading data to sort...");
		String line;
		try {
			while((line = bufferedReader.readLine()) != null) {
				String split[] = line.split("\\s+");
				pagesList.put(split[0], Double.parseDouble(split[1]));
			}
			bufferedReader.close();    
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
}
