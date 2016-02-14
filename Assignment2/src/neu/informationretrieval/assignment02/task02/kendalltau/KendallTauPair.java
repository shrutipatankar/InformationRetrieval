package neu.informationretrieval.assignment02.task02.kendalltau;

public class KendallTauPair {
	
	private String page1;
	private String page2;
	
	public KendallTauPair(String page1, String page2) {
		this.page1 = page1;
		this.page2 = page2;
	}
	
	public KendallTauPair(){
		
	}
	
	public String getPage1() {
		return page1;
	}
	public void setPage1(String page1) {
		this.page1 = page1;
	}
	public String getPage2() {
		return page2;
	}
	public void setPage2(String page2) {
		this.page2 = page2;
	}
	
}
