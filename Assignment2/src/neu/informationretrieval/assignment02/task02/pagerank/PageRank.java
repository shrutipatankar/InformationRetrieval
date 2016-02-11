package neu.informationretrieval.assignment02.task02.pagerank;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

public class PageRank {
	
	private Set<GraphNode> allPages;
	private Set<GraphNode> sinkNodes;
	private double teleportationFactor;
	
	public PageRank(){
		allPages = new HashSet<GraphNode>();
		sinkNodes = new HashSet<GraphNode>();
		teleportationFactor = 0.85;
	}
	
	public PageRank(Set<GraphNode> allPages){
		//this.allPages = new HashSet<GraphNode>();
		this.allPages = allPages;
		this.sinkNodes = new HashSet<GraphNode>();;
		teleportationFactor = 0.85;
	}
	
	public void calculatePageRank(){
		
		setInitialPageRank();
		for(GraphNode node : allPages){
			System.out.println("Page Rank of "+ node.getName() + " is " + node.getPageRank());
		}
	}
	
	private void setInitialPageRank(){
		double initialPageRank = (double) 1 / (double) allPages.size();
		//(((double) 1) / ((double) allPages.size()))
		System.out.println("Setting initial page rank to " + initialPageRank);
		for(GraphNode node : allPages){
			node.setPageRank(initialPageRank);
		}
	}
	
	
}
