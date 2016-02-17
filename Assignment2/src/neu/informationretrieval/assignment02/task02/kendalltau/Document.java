package neu.informationretrieval.assignment02.task02.kendalltau;

public class Document {

	private String name;
	private Double rank;
	private int order;
	
	public Document(String name, Double rank, int order){
		this.name = name;
		this.rank = rank;
		this.order = order; 
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getRank() {
		return rank;
	}
	public void setRank(Double rank) {
		this.rank = rank;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
}
