package neu.informationretrieval.assignment01.task01.webcrawler;

import java.util.Set;

/**
 * 
 * @author shruti 
 * This class is a basic data structure for the Web Crawler. 
 * It holds the URL that needs to be visited or is visited and 
 * it holds the depth of this URL from the root.
 * 
 * A CrawlNode represents a specialized node for WebCrawler 
 * in the crawling tree
 * A CrawlNode has:
 * 1. URL      : Uniform Resource Locator of the Web Page
 * 2. Depth    : Depth of the CrawlNode considering root of 
 *               the tree to be at Depth 1
 * 3. Children : Children of the current node. Children is a 
 *               set of CrawlNodes (i.e URLs appearing in the)
 *               current Web Page
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
