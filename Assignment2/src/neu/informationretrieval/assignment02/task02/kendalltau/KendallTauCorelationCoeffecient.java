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
	private Map<String, Integer> pageRankList;
	private Map<String, Integer> incomingLinksList;
	private List<KendallTauPair> kendallTauPairs;
	private List<String> commonPages;
	private BufferedReader bufferedReader1;
	private BufferedReader bufferedReader2;
	private FileReader fileReader1;
	private FileReader fileReader2;
	String filename1;
	String filename2;
	private int concordantPairs;
	private int discordantPairs;

	public KendallTauCorelationCoeffecient() {
		concordantPairs = 0;
		discordantPairs = 0;
		filename1 = "KendallTauInput/WG2Top50_PageRank.txt";
		filename2 = "KendallTauInput/WG2Top50_IncomingLinks.txt";
		pageRankList = new HashMap<String, Integer>();
		incomingLinksList = new HashMap<String, Integer>();
		kendallTauPairs = new ArrayList<KendallTauPair>();
		commonPages = new ArrayList<String>();
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
				pageRankList.put(line, counter);
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
				incomingLinksList.put(line, counter);
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

		loadPageRankList();
		loadIncomingLinksList();
		findCommonSetOfPages();
		findTotalNumberOfPossiblePairs();
		logger.info("Total Number of Possible pairs" + kendallTauPairs.size());

		bifercateConcordantDiscordant();
		logger.info("Concordant pairs = " + concordantPairs);
		logger.info("Disordant pairs = " + discordantPairs);
		logger.info("Total = " + (concordantPairs + discordantPairs));

		double tau = caluclateCorelationCoeffecient();
		logger.info("Tau = " + tau);

	}

	private void findCommonSetOfPages() {
		for (Map.Entry<String, Integer> entry : pageRankList.entrySet()) {
			if (incomingLinksList.containsKey(entry.getKey())) {
				commonPages.add(entry.getKey());
			}
		}
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

			int page1PageRankOrder = pageRankList
					.get(kendallTauPair.getPage1());
			int page2PageRankOrder = pageRankList
					.get(kendallTauPair.getPage2());

			int page1IncomingLinkOrder = incomingLinksList.get(kendallTauPair
					.getPage1());
			int page2IncomingLinkOrder = incomingLinksList.get(kendallTauPair
					.getPage2());

			if ((page1PageRankOrder < page2PageRankOrder)
					&& (page1IncomingLinkOrder < page2IncomingLinkOrder)) {
				concordantPairs = concordantPairs + 1;
			} else if ((page1PageRankOrder > page2PageRankOrder)
					&& (page1IncomingLinkOrder > page2IncomingLinkOrder)) {
				concordantPairs = concordantPairs + 1;
			} else {
				discordantPairs = discordantPairs + 1;
			}
		}
	}

	private double caluclateCorelationCoeffecient() {
		return (((double) concordantPairs - (double) discordantPairs)
				/ (double) (kendallTauPairs.size()));
	}
}
