package neu.informationretrieval.assignment01.webcrawler;

import java.util.Set;

/**
 * 
 * @author shruti This class is a basic data structure for the Web Crawler. It
 *         holds the URL that needs to be visited or is visited and it holds the
 *         depth of this URL from the root.
 */
public class CrawlNode {

	private String url;
	private int depth;
	private Set<CrawlNode> children;

	public CrawlNode() {
		depth = 0;
		url = "";
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public Set<CrawlNode> getChildren() {
		return children;
	}

	public void setChildren(Set<CrawlNode> children) {
		this.children = children;
	}

}
