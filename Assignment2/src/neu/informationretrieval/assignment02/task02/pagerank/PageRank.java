package neu.informationretrieval.assignment02.task02.pagerank;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PageRank {

	private Map<String, GraphNode> allPages;
	private Set<String> sinkNodes;
	private double teleportationFactor;
	private List<Double> perplexityVector;

	public PageRank() {
		allPages = new HashMap<String, GraphNode>();
		sinkNodes = new HashSet<String>();
		teleportationFactor = 0.85;
		perplexityVector = new ArrayList<Double>();
	}

	public PageRank(Map<String, GraphNode> allPages, Set<String> sinkNodes) {
		// this.allPages = new HashSet<GraphNode>();
		this.allPages = allPages;
		this.sinkNodes = sinkNodes;
		teleportationFactor = 0.85;
		perplexityVector = new ArrayList<Double>();
	}

	public void calculatePageRank() {
		// set the page rank for all the pages
		setInitialPageRank();

		int count = 0;
		while ((!isConverged(count))) {
			// logs
			System.out.println("Loop number: " + count);
			count = count + 1;
			System.out.println("Not Converged");

			/* calculate total sink PR */
			double sinkPageRank = 0.0;
			for (String sinkNode : sinkNodes) {
				sinkPageRank = sinkPageRank
						+ allPages.get(sinkNode).getPageRank();
			}
			System.out.println("Calculated Sink Page Rank = " + sinkPageRank);

			/* Main Page Rank Calulation Loop */
			for (Map.Entry<String, GraphNode> entry : allPages.entrySet()) {
				System.out.println("Calculating Page Rank for Page: "
						+ entry.getKey());
				double newPageRank = (double) (1 - teleportationFactor)
						/ (double) allPages.size();
				System.out.println("PageRank after first modification: "
						+ newPageRank);
				newPageRank += (double) teleportationFactor
						* (sinkPageRank / (double) allPages.size());
				System.out.println("PageRank after second modification: "
						+ newPageRank);

				for (String incomingLinks : entry.getValue()
						.getIncomingGraphNodes()) {
					newPageRank += (double) teleportationFactor
							* allPages.get(incomingLinks).getPageRank()
							/ (double) allPages.get(incomingLinks)
									.getNumberOfOutgoingEdges();
				}
				System.out.println("PageRank after third(final) modification: "
						+ newPageRank);
				entry.getValue().setPageRank(newPageRank);
			}
		}
		if (isConverged(count)) {
			System.out.println("Converged!");
		}

	}

	private boolean isConverged(int loopNumber) {
		boolean converged = true;
		if (loopNumber <= 4) {
			calculatePerplexity();
			System.out.println(perplexityVector.toString());
			return false;
		} else {
			calculatePerplexity();
			for (int i = loopNumber; i >= (loopNumber - 5); i--) {
				converged = converged && (calculatePerplexityDifference(i) < 1);
			}
		}
		return converged;
	}

	private double calculatePerplexityDifference(int loopNumber) {
		return (perplexityVector.get(perplexityVector.size() - 1) - perplexityVector
				.get(perplexityVector.size() - 2));
	}

	private void calculatePerplexity() {
		perplexityVector.add(Math.pow(2, getSummationOfEntropy()));
	}

	private double getSummationOfEntropy() {
		double entropy = 0;
		for (Map.Entry<String, GraphNode> entry : allPages.entrySet()) {
			entropy = entropy
					+ calculateIndividualEntropy(entry.getValue().getPageRank());
		}
		entropy = -(entropy);
		return entropy;
	}

	private double calculateIndividualEntropy(double pageRank) {
		return (pageRank * (Math.log(pageRank) / Math.log(2)));
	}

	private void setInitialPageRank() {
		DecimalFormat df = new DecimalFormat("0.00");
		double initialPageRank = (double) 1 / (double) allPages.size();
		String formate = df.format(initialPageRank);
		try {
			initialPageRank = (Double) df.parse(formate);
			System.out.println("Setting initial page rank to "
					+ initialPageRank);
			for (Map.Entry<String, GraphNode> entry : allPages.entrySet()) {
				entry.getValue().setPageRank(initialPageRank);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
