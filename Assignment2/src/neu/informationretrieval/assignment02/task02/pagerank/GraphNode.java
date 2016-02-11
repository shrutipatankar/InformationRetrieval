package neu.informationretrieval.assignment02.task02.pagerank;

import java.util.HashSet;
import java.util.Set;

public class GraphNode {

	private String name;
	private int numberOfOutgoingEdges;
	private Set<String> incomingGraphNodes;
	private boolean sinkNode;
	private double pageRank;

	public GraphNode() {
		name = "";
		numberOfOutgoingEdges = 0;
		incomingGraphNodes = new HashSet<String>();
		pageRank = 0;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumberOfOutgoingEdges() {
		return numberOfOutgoingEdges;
	}

	public void setNumberOfOutgoingEdges(int numberOfOutgoingEdges) {
		this.numberOfOutgoingEdges = numberOfOutgoingEdges;
	}

	public Set<String> getIncomingGraphNodes() {
		return incomingGraphNodes;
	}

	public void setIncomingGraphNodes(Set<String> incomingGraphNodes) {
		this.incomingGraphNodes = incomingGraphNodes;
	}

	public boolean isSinkNode() {
		return sinkNode;
	}

	public void setSinkNode(boolean sinkNode) {
		this.sinkNode = sinkNode;
	}

	public double getPageRank() {
		return pageRank;
	}

	public void setPageRank(double pageRank) {
		this.pageRank = pageRank;
	}
}
