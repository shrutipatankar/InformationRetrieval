package neu.informationretrieval.assignment01B.task02;

/**
 * @author shruti This class acts as a Data Structure to store an inverted
 *         index.
 * 
 *         It has following two data members: docId: The document id (title of
 *         the document) in which the WORD occurred termFrequency: The number of
 *         times the WORD occurred
 * 
 *         [NOTE] A WORD can be defined as: A word n-gram, and n = 1, 2, and 3
 * 
 */

public class Index {

	private String docId;
	private int termFrequency;

	Index(){
		docId = "temp";
		termFrequency = 0;
	}
	
	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public int getTermFrequency() {
		return termFrequency;
	}

	public void setTermFrequency(int termFrequency) {
		this.termFrequency = termFrequency;
	}
}
