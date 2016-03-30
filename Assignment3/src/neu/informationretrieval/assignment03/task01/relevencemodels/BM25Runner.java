/**
 * 
 */
package neu.informationretrieval.assignment03.task01.relevencemodels;

/**
 * @author shruti
 *
 */
public class BM25Runner {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BM25 bm25 = new BM25();
		bm25.rankDocuments("global");
	}

}
