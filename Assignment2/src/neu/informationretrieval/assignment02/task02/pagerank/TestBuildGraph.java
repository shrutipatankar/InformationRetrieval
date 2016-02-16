package neu.informationretrieval.assignment02.task02.pagerank;

public class TestBuildGraph {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String filename = "Input/WG1.txt";
		//String filename = "Input/WG2.txt";
		double teleportationFactor = 0.95;
		WebGraph webGraph = new WebGraph(filename, teleportationFactor);
		webGraph.constructGraph();
	}

}
