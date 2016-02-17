package neu.informationretrieval.assignment02.task02.kendalltau;

public class KendallTauCorelationCoeffecientRunner {

	public static void main(String args[]) {

		// Top 50 by incoming links and top 50 by page rank of WG1
		String file1 = "KendallTauInput/WG1Top50_PageRank.txt";
		String file2 = "KendallTauInput/WG1Top50_IncomingLinks.txt";
		KendallTauCorelationCoeffecient kendallTauCorelationCoeffecient = new KendallTauCorelationCoeffecient(
				file1, file2);
		kendallTauCorelationCoeffecient
				.calculateKendallTauCorelationCoeffecient();

		// Top 50 by incoming links and top 50 by page rank of WG2
		file1 = "KendallTauInput/WG2Top50_PageRank.txt";
		file2 = "KendallTauInput/WG2Top50_IncomingLinks.txt";
		KendallTauCorelationCoeffecient kendallTauCorelationCoeffecient1 = new KendallTauCorelationCoeffecient(
				file1, file2);
		kendallTauCorelationCoeffecient1
				.calculateKendallTauCorelationCoeffecient();

		// Top 50 by PageRank (0.85 teleportation) and top 50 by page rank (0.95
		// teleportation) of WG1
		file1 = "KendallTauInput/Task3B_WG1Top50_byPageRank.txt";
		file2 = "KendallTauInput/WG1Top50_PageRank.txt";
		KendallTauCorelationCoeffecient kendallTauCorelationCoeffecient2 = new KendallTauCorelationCoeffecient(
				file1, file2);
		kendallTauCorelationCoeffecient2
				.calculateKendallTauCorelationCoeffecient();

		// Top 50 by PageRank (0.85 teleportation) and top 50 by page rank (0.95
		// teleportation) of WG2
		file1 = "KendallTauInput/Task3B_WG2Top50_byPageRank.txt";
		file2 = "KendallTauInput/WG2Top50_PageRank.txt";
		KendallTauCorelationCoeffecient kendallTauCorelationCoeffecient3 = new KendallTauCorelationCoeffecient(
				file1, file2);
		kendallTauCorelationCoeffecient3
				.calculateKendallTauCorelationCoeffecient();
		
		// Top 50 by by incoming links and top 50 by page rank (0.95
		// teleportation) of WG1
		file1 = "KendallTauInput/Task3B_WG1Top50_byPageRank.txt";
		file2 = "KendallTauInput/WG1Top50_IncomingLinks.txt";
		KendallTauCorelationCoeffecient kendallTauCorelationCoeffecient4 = new KendallTauCorelationCoeffecient(
				file1, file2);
		kendallTauCorelationCoeffecient4
				.calculateKendallTauCorelationCoeffecient();
		
		// Top 50 by PageRank (0.85 teleportation) and top 50 by page rank (0.95
		// teleportation) of WG1
		file1 = "KendallTauInput/Task3B_WG2Top50_byPageRank.txt";
		file2 = "KendallTauInput/WG2Top50_IncomingLinks.txt";
		KendallTauCorelationCoeffecient kendallTauCorelationCoeffecient5 = new KendallTauCorelationCoeffecient(
				file1, file2);
		kendallTauCorelationCoeffecient5
				.calculateKendallTauCorelationCoeffecient(); 
	}
}
