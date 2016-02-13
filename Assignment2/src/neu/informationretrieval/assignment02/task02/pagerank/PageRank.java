package neu.informationretrieval.assignment02.task02.pagerank;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PageRank{
	final static Logger logger = LoggerFactory.getLogger(PageRank.class);
	final static Logger analytics = LoggerFactory.getLogger("analytics");
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
		logger.info("Initializing Page Rank Object");
		this.allPages = allPages;
		this.sinkNodes = sinkNodes;
		teleportationFactor = 0.85;
		perplexityVector = new ArrayList<Double>();
	}

	public void calculatePageRank() {
		logger.info("In calculate page rank");
		// set the page rank for all the pages
		logger.info("Starting to calculate page rank");
		setInitialPageRank();

		try {
			PrintWriter pw = new PrintWriter("Output/PageRank.txt");
			int count = 1;
			while ((!isConverged(count))) {

				// logs
				count = count + 1;
				logger.info("Not Converged");
				analytics.info("Loop number: " + count);
				logger.info("Loop number: " + count);

				// calculate total sink PR
				double sinkPageRank = 0.0;
				for (String sinkNode : sinkNodes) {
					sinkPageRank = sinkPageRank
							+ allPages.get(sinkNode).getPageRank();
				}
				logger.info("Calculated Sink Page Rank = " + sinkPageRank);

				// Main Page Rank Calulation Loop
				for (Map.Entry<String, GraphNode> entry : allPages.entrySet()) {
					logger.info("Calculating Page Rank for Page: "
							+ entry.getKey());
					double newPageRank = (double) (1 - teleportationFactor)
							/ (double) allPages.size();

					newPageRank += (double) teleportationFactor
							* (sinkPageRank / (double) allPages.size());

					for (String incomingLinks : entry.getValue()
							.getIncomingGraphNodes()) {
						newPageRank += (double) teleportationFactor
								* allPages.get(incomingLinks).getPageRank()
								/ (double) allPages.get(incomingLinks)
										.getNumberOfOutgoingEdges();
					}
					logger.info("PageRank after final modification: "
							+ newPageRank);
					entry.getValue().setPageRank(newPageRank);
				}
			}

			logger.info("Converged! after " + count + " loops");
			analytics.info("Converged! after " + count + " loops");
			for (Map.Entry<String, GraphNode> entry : allPages.entrySet()) {
				pw.write(entry.getKey().toString() + " "
						+ entry.getValue().getPageRank() + "\n");
			}
			pw.close();
			testPageRank();
			SortPages sortPages = new SortPages();
			sortPages.sortAllPagesbyPageRank();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void testPageRank() {
		double testPageRank = 0.0;
		for (Map.Entry<String, GraphNode> entry : allPages.entrySet()) {
			testPageRank = testPageRank + entry.getValue().getPageRank();
		}
		analytics
				.info("Page Rank after adding all page ranks: " + testPageRank);
	}

	private boolean isConverged(int loopNumber) {
		boolean converged = true;
		if (loopNumber <= 5) {
			calculatePerplexity();
			analytics.info("Perplexity vector for loop number " + loopNumber
					+ " " + perplexityVector.toString());
			logger.info(perplexityVector.toString());
			return false;
		} else {
			calculatePerplexity();
			analytics.info("Perplexity vector for loop number " + loopNumber
					+ " " + perplexityVector.toString());
			logger.info(perplexityVector.toString());
			for (int i = loopNumber; i > (loopNumber - 4); i--) {
				logger.info("Perplexity difference for loop i= " + i + " is "
					+ calculatePerplexityDifference(i));

				converged = converged && (calculatePerplexityDifference(i) < 1);
			}
		}
		return converged;
	}

	private double calculatePerplexityDifference(int loopNumber) {
		return (Math.abs(perplexityVector.get(loopNumber - 1)
				- perplexityVector.get(loopNumber - 2)));
	}

	private void calculatePerplexity() {
		perplexityVector.add(Math.pow(2, getSummationOfEntropy()));
	}

	private double getSummationOfEntropy() {
		double entropy = 0.0;
		for (Map.Entry<String, GraphNode> entry : allPages.entrySet()) {
			entropy = entropy
					+ calculateIndividualEntropy(entry.getValue().getPageRank());
		}
		entropy = 0.0 - entropy;
		logger.info("Entropy" + entropy);
		return entropy;
	}

	private double calculateIndividualEntropy(double pageRank) {
		logger.info("Individual Entropy " + pageRank
				* (Math.log(pageRank) / Math.log(2)));
		return (pageRank * (Math.log(pageRank) / Math.log(2)));
	}

	private void setInitialPageRank() {
		double initialPageRank = (double) 1 / (double) allPages.size();
		logger.info("Setting initial page rank to " + initialPageRank);
		for (Map.Entry<String, GraphNode> entry : allPages.entrySet()) {
			entry.getValue().setPageRank(initialPageRank);
		}
	}
	
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(
			Map<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			@Override
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		Map<K, V> result = new LinkedHashMap<>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}
	
}
