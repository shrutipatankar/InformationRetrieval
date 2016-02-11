package neu.informationretrieval.assignment02.task02.pagerank;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.AllPermission;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
	
	private BufferedReader bufferedReader;
	private FileReader fileReader;
	private Map<String,Set<String>> adjacencyList;
	private Set<GraphNode> pages;
	private Set<GraphNode> sinkNodePages;
	
	
	WebGraph(){
		try {
			fileReader = new FileReader("Input/WG1.txt");
			bufferedReader = new BufferedReader(fileReader);
			adjacencyList = new HashMap<String, Set<String>>();
			pages = new HashSet<GraphNode>();
			sinkNodePages = new HashSet<GraphNode>();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void constructGraph(){
		buildAdjacencyList();
		printAdjacencyList();
		buildPagesSet();
		printAllPages();
		PageRank pageRank = new PageRank(pages);
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
	
	public void printAdjacencyList() {
		for (Map.Entry<String, Set<String>> entry : adjacencyList.entrySet()) {
			System.out.println(entry.getKey() + " : " + entry.getValue().toString());
		}
	}
	
	public void buildPagesSet(){
		for (Map.Entry<String, Set<String>> entry : adjacencyList.entrySet()) {
			int outlinks = calculateOutGoingEdges(entry.getKey());
			GraphNode node = new GraphNode();
			node.setName(entry.getKey());
			node.setIncomingGraphNodes(entry.getValue());
			node.setNumberOfOutgoingEdges(outlinks);
			node.setSinkNode((outlinks == 0));
			if(node.isSinkNode()){
				sinkNodePages.add(node);
			}
			pages.add(node);
		}
	}
	
	public void printAllPages(){
		for(GraphNode node : pages){
			System.out.println("--------------------------------------------------");
			System.out.println("Name: " + node.getName());
			System.out.println("Incoming Pages: " + node.getIncomingGraphNodes().toString());
			System.out.println("Number of Outgoing links: " + node.getNumberOfOutgoingEdges());
			System.out.println("Is this a sink node? " + node.isSinkNode());
			System.out.println("--------------------------------------------------");
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

}
