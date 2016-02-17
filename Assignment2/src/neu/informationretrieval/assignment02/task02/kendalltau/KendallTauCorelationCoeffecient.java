package neu.informationretrieval.assignment02.task02.kendalltau;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KendallTauCorelationCoeffecient {

	final static Logger logger = LoggerFactory
			.getLogger(KendallTauCorelationCoeffecient.class);
	private Map<String, Double> pageRankList;
	private Map<String, Integer> pageRankOrderList;
	private Map<String, Double> incomingLinksList;
	private Map<String, Integer> incomingLinksOrderedList;
	private List<KendallTauPair> kendallTauPairs;
	private List<String> commonPages;
	private BufferedReader bufferedReader1;
	private BufferedReader bufferedReader2;
	private FileReader fileReader1;
	private FileReader fileReader2;
	private String filename1;
	private String filename2;
	private int concordantPairs;
	private int discordantPairs;
	private List<String> disregardList;
	private int disregardPages;

	public KendallTauCorelationCoeffecient(String file1, String file2) {
		concordantPairs = 0;
		discordantPairs = 0;
		disregardPages = 0;
		filename1 = file1;
		filename2 = file2;
		pageRankList = new HashMap<String, Double>();
		incomingLinksList = new HashMap<String, Double>();
		kendallTauPairs = new ArrayList<KendallTauPair>();
		commonPages = new ArrayList<String>();
		pageRankOrderList = new HashMap<String, Integer>();
		incomingLinksOrderedList = new HashMap<String, Integer>();
		disregardList = new ArrayList<String>();
		try {
			fileReader1 = new FileReader(filename1);
			fileReader2 = new FileReader(filename2);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bufferedReader1 = new BufferedReader(fileReader1);
		bufferedReader2 = new BufferedReader(fileReader2);
	}

	private void loadPageRankList() {
		String line;
		int counter = 1;
		try {
			while ((line = bufferedReader1.readLine()) != null) {
				String split[] = line.split(" ");
				pageRankList.put(split[0], Double.parseDouble(split[1]));
				pageRankOrderList.put(split[0], counter);
				counter++;
			}
			bufferedReader1.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void loadIncomingLinksList() {
		String line;
		int counter = 1;
		try {
			while ((line = bufferedReader2.readLine()) != null) {
				String split[] = line.split(" ");
				incomingLinksList.put(split[0], Double.parseDouble(split[1]));
				incomingLinksOrderedList.put(split[0], counter);
				counter++;
			}
			bufferedReader2.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void calculateKendallTauCorelationCoeffecient() {
		// load data
		logger.info("Calculating Kendall Tau Corelation Coeffecient for: "
				+ filename1 + " " + filename2);
		loadPageRankList();
		loadIncomingLinksList();
		findCommonSetOfPages();
		findTotalNumberOfPossiblePairs();
		logger.info("Total Number of Possible pairs: " + kendallTauPairs.size());
		findPagesWithSameRanksOrInLinks(incomingLinksList);
		//logger.info("Docs having same inlinks count: " + disregardList.size());
		findPagesWithSameRanksOrInLinks(pageRankList);
		//logger.info("Docs having same page ranks: " + disregardList.size());

		bifercateConcordantDiscordant();
		logger.info("Concordant pairs = " + concordantPairs);
		logger.info("Disordant pairs = " + discordantPairs);
		logger.info("Total = " + (concordantPairs + discordantPairs));

		double tau = caluclateCorelationCoeffecient();
		logger.info("Tau = " + tau);

	}

	private void findPagesWithSameRanksOrInLinks(
			Map<String, Double> incomingLinksList1) {
		for (Map.Entry<String, Double> entry : incomingLinksList1.entrySet()) {
			if (countOccurences(incomingLinksList1, entry.getValue()) > 1) {
				disregardList.add(entry.getKey());
			}
		}
	}

	private int countOccurences(Map<String, Double> incomingLinksList2,
			double value) {
		int count = 0;
		for (String key : incomingLinksList2.keySet()) {
			if (incomingLinksList2.get(key) == value) {
				count++;
			}
		}
		return count;
	}

	private void findCommonSetOfPages() {
		for (Map.Entry<String, Double> entry : pageRankList.entrySet()) {
			if (incomingLinksList.containsKey(entry.getKey())) {
				commonPages.add(entry.getKey());
			}
		}
		logger.info("size of common pages: " + commonPages.size());
	}

	private void findTotalNumberOfPossiblePairs() {
		for (String page1 : commonPages) {
			for (String page2 : commonPages) {
				if (!page1.equals(page2) && !listContainsPair(page1, page2)) {
					KendallTauPair kendallTauPair = new KendallTauPair(page1,
							page2);
					kendallTauPairs.add(kendallTauPair);
				}
			}
		}
	}

	private boolean listContainsPair(String page1, String page2) {
		for (KendallTauPair temp : kendallTauPairs) {
			if ((temp.getPage1().equals(page1) && temp.getPage2().equals(page2))
					|| (temp.getPage2().equals(page1) && temp.getPage1()
							.equals(page2))) {
				return true;
			}
		}
		return false;
	}

	private void bifercateConcordantDiscordant() {
		for (KendallTauPair kendallTauPair : kendallTauPairs) {

			int page1PageRankOrder = pageRankOrderList.get(kendallTauPair
					.getPage1());
			int page2PageRankOrder = pageRankOrderList.get(kendallTauPair
					.getPage2());

			int page1IncomingLinkOrder = incomingLinksOrderedList
					.get(kendallTauPair.getPage1());
			int page2IncomingLinkOrder = incomingLinksOrderedList
					.get(kendallTauPair.getPage2());

	
			if ((page1PageRankOrder < page2PageRankOrder)
					&& (page1IncomingLinkOrder < page2IncomingLinkOrder)
					&& (!pageRankList.get(kendallTauPair.getPage1()).equals(pageRankList
							.get(kendallTauPair.getPage2())))
					&& (!incomingLinksList.get(kendallTauPair.getPage1()).equals(incomingLinksList
							.get(kendallTauPair.getPage2())))) {
				concordantPairs = concordantPairs + 1;
			} else if ((page1PageRankOrder > page2PageRankOrder)
					&& (page1IncomingLinkOrder > page2IncomingLinkOrder)
					&& (!pageRankList.get(kendallTauPair.getPage1())
							.equals(pageRankList.get(kendallTauPair.getPage2())))
					&& (!incomingLinksList.get(kendallTauPair.getPage1())
							.equals(incomingLinksList.get(kendallTauPair
									.getPage2())))) {
				concordantPairs = concordantPairs + 1;
			} else if ((!pageRankList.get(kendallTauPair.getPage1())
					.equals(pageRankList.get(kendallTauPair.getPage2())))
					&& (!incomingLinksList.get(kendallTauPair.getPage1())
							.equals(incomingLinksList.get(kendallTauPair
									.getPage2())))) {
				discordantPairs = discordantPairs + 1;
			} else {
				disregardPages++;
				logger.info("DisregardPages Size:" + disregardPages);
			}
		}
	}

	private double caluclateCorelationCoeffecient() {
		return (((double) concordantPairs - (double) discordantPairs) / (double) (kendallTauPairs
				.size()));
	}
}