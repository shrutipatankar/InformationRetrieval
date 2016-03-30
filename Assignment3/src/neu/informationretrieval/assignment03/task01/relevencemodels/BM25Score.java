package neu.informationretrieval.assignment03.task01.relevencemodels;

public class BM25Score {

	private int queryId;
	private int docId;
	private int rank;
	private double BM25_score;
	private String systemName;
	
	BM25Score(){
		queryId = 0;
		docId = 0;
		rank = 0;
		BM25_score = 0;
		systemName = "BM25Run";
	}

	public int getQueryId() {
		return queryId;
	}

	public void setQueryId(int queryId) {
		this.queryId = queryId;
	}

	public int getDocId() {
		return docId;
	}

	public void setDocId(int docId) {
		this.docId = docId;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public double getBM25_score() {
		return BM25_score;
	}

	public void setBM25_score(double bM25_score) {
		BM25_score = bM25_score;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	
}
