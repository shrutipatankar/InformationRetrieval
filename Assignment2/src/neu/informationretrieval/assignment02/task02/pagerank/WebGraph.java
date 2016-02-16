package neu.informationretrieval.assignment02.task02.pagerank;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author shruti
 * This class takes in an input file in the following format:
 * 
 * A D E F
 * B A F
 * C A B D
 * D B C
 * E B C D F
 * F A B D
 * 
 * This graph actually represents the incoming links that a 
 * node has from every other node. For example:
 * Node A: has incoming links from nodes D, E and F
 * 
 * This class then has methods to calculate the following
 * from the graph taken as an input in the above format:
 * 
 * 1. set of all nodes (WebPages in this context)
 * 2. set of sink nodes (nodes that do not have any outgoing links)
 * 3. set of all nodes that link to a node
 * 4. number of outgoing links for every node
 */
public class WebGraph {
	final static Logger logger = LoggerFactory.getLogger(WebGraph.class);
	private BufferedReader bufferedReader;
	private FileReader fileReader;
	private Map<String,Set<String>> adjacencyList;
	private Map<String, GraphNode> pages;
	private Set<String> sinkNodePages;
	private Set<String> sourceNodePages;
	private Map<String, Integer> outGoingLinks; 
	private double teleportationFactor;
	 
	public WebGraph(String filename, double teleportationFactor){
		this.teleportationFactor = teleportationFactor;
		try {
			fileReader = new FileReader(filename);
			bufferedReader = new BufferedReader(fileReader);
			adjacencyList = new HashMap<String, Set<String>>();
			pages = new HashMap<String,GraphNode>();
			sinkNodePages = new HashSet<String>();
			sourceNodePages = new HashSet<String>();
			outGoingLinks = new TreeMap<String, Integer>();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void constructGraph(){
		buildAdjacencyList();
		//printAdjacencyList();
		printIncomingLinksData();
		countPagesOccurances();
		buildPagesSet();
		//printAllPages();
		PageRank pageRank = new PageRank(pages,sinkNodePages, teleportationFactor);
		pageRank.calculatePageRank();
	}
	
	
	public void buildAdjacencyList(){
		String line;
		try {
			while((line = bufferedReader.readLine()) != null) {
				String split[] = line.split("\\s+");
				Set<String> incoming = new HashSet<String>();
				for(int i=1; i< split.length;i++){
					incoming.add(split[i]);
				}
				adjacencyList.put(split[0],incoming);
			}
			bufferedReader.close();    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}                
	}
	
	private void printIncomingLinksData() {
		try {
			PrintWriter printWriter = new PrintWriter(
					"Output/PagesSortedByIncomingLinks.txt");
			Map<String, Integer> tempMap = new HashMap<String, Integer>();
			for (Map.Entry<String, Set<String>> entry : adjacencyList
					.entrySet()) {
				tempMap.put(entry.getKey(), entry.getValue().size());
			}
			tempMap = sortByValue(tempMap);
			logger.info("Printing sorted data:");
			for (Map.Entry<String, Integer> entry : tempMap.entrySet()) {
				logger.info(entry.getKey() + " " + entry.getValue());
				printWriter.write(entry.getKey() + " " + entry.getValue()
						+ "\n");
			}
			printWriter.close();
			printGraphPlotData(tempMap);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void printGraphPlotData(Map<String, Integer> tempMap) {
		try {
			PrintWriter printWriter = new PrintWriter(
					"Output/GraphPlotData.txt");
			int incomingLinksCount[] = new int[tempMap.size()];

			int tempCount = 0;
			for (Map.Entry<String, Integer> entry : tempMap.entrySet()) {
				incomingLinksCount[tempCount] = entry.getValue();
				tempCount++;
			}
			int countPages = 0;
			logger.info("Writing count info to LogGraphData.txt");
			for (int i = 0; i < incomingLinksCount.length; i++) {
				if (i != (incomingLinksCount.length - 1)) {
					if (incomingLinksCount[i] == incomingLinksCount[i + 1]) {
						countPages++;
						//logger.info("Count so far:" + countPages);
					} else {
						countPages++;
						//logger.info("Count so far:" + countPages);
						printWriter.write(incomingLinksCount[i] + " "
								+ countPages + "\n");
						countPages = 0;
					}
				} else {
					countPages++;
					//logger.info("Count so far:" + countPages);
					printWriter.write(incomingLinksCount[i] + " " + countPages
							+ "\n");
				}
			}
			printWriter.close();
		} catch (FileNotFoundException e) {
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
	
	public void printAdjacencyList() {
		for (Map.Entry<String, Set<String>> entry : adjacencyList.entrySet()) {
			logger.info(entry.getKey() + " : " + entry.getValue().toString());
		}
	}
	
	public void buildPagesSet(){
		logger.info("In build pages set");
		for (Map.Entry<String, Set<String>> entry : adjacencyList.entrySet()) {
			//int outlinks = calculateOutGoingEdges(entry.getKey());
			int outlinks = outGoingLinks.get(entry.getKey());
			GraphNode node = new GraphNode();
			node.setName(entry.getKey());
			node.setIncomingGraphNodes(entry.getValue());
			if(node.getIncomingGraphNodes().isEmpty()){
				sourceNodePages.add(node.getName());
			}
			node.setNumberOfOutgoingEdges(outlinks);
			node.setSinkNode((outlinks == 0));
			if(node.isSinkNode()){
				sinkNodePages.add(node.getName());
			}
			pages.put(node.getName(),node);
		}
		logger.info("Done with build pages set");
		logger.info("Number of source nodes: "+sourceNodePages.size());
		logger.info("Number of sink nodes: "+sinkNodePages.size());	
	}
	
	public void printAllPages(){
		for (Map.Entry<String, GraphNode> entry : pages.entrySet()) {
			logger.info("--------------------------------------------------");
			logger.info("Name: " + entry.getValue().getName());
			logger.info("Incoming Pages: " + entry.getValue().getIncomingGraphNodes());
			logger.info("Number of Outgoing links: " + entry.getValue().getNumberOfOutgoingEdges());
			logger.info("Is this a sink node? " + entry.getValue().isSinkNode());
			logger.info("--------------------------------------------------");
		}
	}
	
	public int calculateOutGoingEdges(String key){
		int count=0;
		for (Map.Entry<String, Set<String>> entry : adjacencyList.entrySet()) {
			Set<String> inlinks = entry.getValue();
			for(String page : inlinks){
				if(page.equals(key)){
					count = count + 1;
				}
			}
		}
		return count;
	}
	
	private void countPagesOccurances() {
		for (Map.Entry<String, Set<String>> entry : adjacencyList.entrySet()) {
			if(!outGoingLinks.containsKey(entry.getKey())){
				outGoingLinks.put(entry.getKey(), 0);
			}
			for (String temp : entry.getValue()) {
				if (!outGoingLinks.containsKey(temp)) {
					outGoingLinks.put(temp, 1);
				} else {
					outGoingLinks.put(temp, outGoingLinks.get(temp) + 1);
				}
			}
		}
		logger.info("Outgoing links Size: " + outGoingLinks.size());
	}

}
