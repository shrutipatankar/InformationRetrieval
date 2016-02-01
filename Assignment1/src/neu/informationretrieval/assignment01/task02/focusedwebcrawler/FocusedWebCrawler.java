package neu.informationretrieval.assignment01.task02.focusedwebcrawler;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import neu.informationretrieval.assignment01.task01.webcrawler.WebCrawlerThread;
import neu.informationretrieval.assignment01.task01.webcrawler.WebPage;

/**
 * 
 * @author shruti
 * 
 */
public class FocusedWebCrawler {

	private CrawlNode seed;
	private WebCrawlerThread webCrawlerThread;
	private Set<String> visitedWebPages;
	private List<CrawlNode> pagesToVisit;
	private String keyWord;
	private Set<String> parsedWebPages;
	private long startTime;
	private long endTime;
	private int pagesToCrawl;
	private int maxNodeDepthToCrawl;
	private boolean DFS;
	private boolean BFS;
	private String downloadPath;

	/*
	 * These filters ensure : 1. The links are not administrative (i.e do not
	 * contain colon) 2. The links are not pointing to different sections of the
	 * same page (i.e do not contain #) 3. The links are not pointing to
	 * css/js/gif/jpg/svg/jpeg/mp3/zip files (i.e are only articles)
	 */
	private final static Pattern FILTERS = Pattern
			.compile(".*(\\.(css|js|gif|jpg|svg|jpeg"
					+ "|png|mp3|mp3|zip|gz))$");
	private final static Pattern CITATAIONS = Pattern.compile("(.*)(#)(.*)");
	private final static Pattern ADMINISTRATIVE = Pattern
			.compile("(.*)(:)(.*)");

	public FocusedWebCrawler() {
		pagesToCrawl = 1000;
		maxNodeDepthToCrawl = 5;
		keyWord = "";
		this.seed = new CrawlNode();
		this.visitedWebPages = new LinkedHashSet<String>();
		this.pagesToVisit = new ArrayList<CrawlNode>();
		this.webCrawlerThread = new WebCrawlerThread();
		this.parsedWebPages = new HashSet<String>();
		BFS = false;
		DFS = false;
		downloadPath = "DownloadedContent/";
	}

	/**
	 * 
	 * @param seed
	 *            : A url with which the FocusedWebCrawler would start crawling
	 *            web pages
	 * @param keyWord
	 *            : The keyword that the FocusedWebCrawler should search for in
	 *            web pages
	 */
	public void crawl(String seed, String keyWord) {
		this.seed.setUrl(seed);
		this.seed.setDepth(1);
		this.seed.setAnchorText("none");
		this.keyWord = keyWord;
		if (isValidCrawlNode(this.seed)) {
			pagesToVisit.add(this.seed);
		}
		startTime = System.currentTimeMillis();
		crawlNextNode();
		endTime = System.currentTimeMillis();
		System.out.println("Time taken= " + ((endTime - startTime) / 60000)
				+ "mins");
		logVisitedPages();
	}

	/**
	 * This method simply picks the next node in line for Crawling from
	 * pagesToVisit. Then it gives a call to checkIfPageVisitPossible if the
	 * number of visitedPages is not exceeding the one set in the constructor
	 */
	private void crawlNextNode() {
		if (visitedWebPages.size() < pagesToCrawl) {
			if (pagesToVisit.isEmpty()) {
				System.out.println("Oooppss! We are out of pages");
			} else {
				System.out.println("Entered pages to visit");
				CrawlNode node = pagesToVisit.iterator().next();
				checkIfPageVisitPossible(node);
			}
		} else {
			System.out.println("Visited max web pages.. program exiting");
		}
	}

	/**
	 * 
	 * @param node
	 *            : Accepts a CrawlNode Checks if the CrawlNode can be Visited
	 *            by checking the depth of the CrawlNode and also makes sure
	 *            that the CrawlNode that represents the web page is not already
	 *            parsed. If the CrawlNode can be visited, then based on whether
	 *            the program is DFS or BFS, it calls the Web Page to be crawled
	 */
	private void checkIfPageVisitPossible(CrawlNode node) {
		if (!parsedWebPages.contains(node.getUrl())
				&& node.getDepth() <= maxNodeDepthToCrawl) {
			if (BFS)
				parseWebPagebyBFS(node);
			else if (DFS) {
				parseWebPagebyDFS(node);
			} else {
				System.out.println("Please set either BFS or DFS flag to true");
			}
		} else {
			pagesToVisit.remove(node);
			crawlNextNode();
		}
	}

	/**
	 * 
	 * @param node
	 *            : Accepts a CrawlNode This method continues to parse a web
	 *            page by using BFS It attaches the child nodes based on the
	 *            depth of the node
	 */
	private void parseWebPagebyBFS(CrawlNode node) {
		System.out.println("Parsing Web Page: " + node.getUrl());
		System.out.println("Depth: " + node.getDepth());
		visitedWebPages.add(node.getUrl());
		if (node.getDepth() < maxNodeDepthToCrawl) {
			attachChildrenByBFS(node);
		}
		pagesToVisit.remove(node);
		crawlNextNode();
	}

	/**
	 * @param root
	 *            : accepts a CrawlNode This method attaches child node to the
	 *            root node. Child nodes are links that are valid and that have
	 *            the keyword in them
	 */
	private void attachChildrenByBFS(CrawlNode root) {
		Set<CrawlNode> possibleChildNodes = getAllChildren(root.getUrl(),
				root.getDepth());
		Set<CrawlNode> validChildren = new HashSet<CrawlNode>();
		for (CrawlNode child : possibleChildNodes) {
			if (visitedWebPages.size() < pagesToCrawl) {
				if (isValidCrawlNode(child)) {
					validChildren.add(child);
					System.out.println("This child at depth "
							+ child.getDepth());
					System.out.println("Adding a valid child");
					pagesToVisit.add(child);
				}
			} else {
				break;
			}
		}
		root.setChildren(validChildren);
		parsedWebPages.add(root.getUrl());
	}

	/**
	 * 
	 * @param node
	 *            : Accepts a CrawlNode This method continues to parse a web
	 *            page by using BFS It attaches the child nodes based on the
	 *            depth of the node
	 */
	private void parseWebPagebyDFS(CrawlNode node) {
		if (visitedWebPages.size() <= pagesToCrawl) {
			System.out.println("Parsing Web Page: " + node.getUrl());
			System.out.println("Depth: " + node.getDepth());
			visitedWebPages.add(node.getUrl());
			if (node.getDepth() < maxNodeDepthToCrawl) {
				attachChildrenByDFS(node);
			}
			pagesToVisit.remove(node);
		} else {
			crawlNextNode();
		}
	}

	/**
	 * @param root
	 *            : accepts a CrawlNode This method attaches child node to the
	 *            root node in DFS fashion. Child nodes are links that are valid
	 *            and that have the keyword in them
	 */
	private void attachChildrenByDFS(CrawlNode root) {
		Set<CrawlNode> possibleChildNodes = getAllChildren(root.getUrl(),
				root.getDepth());
		Set<CrawlNode> validChildren = new HashSet<CrawlNode>();
		for (CrawlNode child : possibleChildNodes) {
			if (visitedWebPages.size() < pagesToCrawl) {
				if (isValidCrawlNode(child)) {
					validChildren.add(child);
					System.out.println("Adding a valid child");
					System.out
							.println("This Child depth = " + child.getDepth());
					pagesToVisit.add(child);
					parseWebPagebyDFS(child);
				}
			} else {
				break;
			}
		}
		root.setChildren(validChildren);
		parsedWebPages.add(root.getUrl());
	}

	/**
	 * 
	 * @param url
	 * @param rootDepth
	 * @return This method takes in a url and depth of the root node. It then
	 *         returns a set of all the links on the Web Page
	 */
	private Set<CrawlNode> getAllChildren(String url, int rootDepth) {
		Elements links = getAllLinksOnWebPage(url);
		Set<CrawlNode> setOfPossibleChildNodes = new HashSet<CrawlNode>();
		for (Element link : links) {
			CrawlNode child = new CrawlNode();
			child.setDepth(rootDepth + 1);
			child.setUrl(link.absUrl("href"));
			child.setAnchorText(link.text());
			setOfPossibleChildNodes.add(child);
		}
		return setOfPossibleChildNodes;
	}

	/**
	 * 
	 * @param url
	 * @return This method takes in a url and returns a set of valid links
	 *         present on a web page.
	 */
	private Elements getAllLinksOnWebPage(String url) {
		WebPage tempWebPage = getWebPage(url);
		Elements validLinks = getValidLinks(tempWebPage.getLinks());
		return validLinks;
	}

	/**
	 * 
	 * @param links
	 *            : takes in links (hyper link like
	 *            "https://en.wikipedia.org/wiki/Sustainable_energy")
	 * @return the set of Elements which are valid for the current problem set.
	 *         i.e 1. The links are only articles 2. The links are non
	 *         administrative 3. The links do not refer to the some section of
	 *         the same Web Page.
	 */
	private Elements getValidLinks(Elements links) {
		Elements validLinks = new Elements();
		for (Element link : links) {
			if ((isValidLink(link.absUrl("href")))
					&& (isNonAdministrativeUrl(link.attr("href")))) {
				validLinks.add(link);
			}
		}
		return validLinks;
	}

	/**
	 * @param url
	 * @return : true if the url is not administrative : false if the url is
	 *         administrative
	 */
	private boolean isNonAdministrativeUrl(String url) {
		return (!ADMINISTRATIVE.matcher(url.toLowerCase()).matches());
	}

	/**
	 * 
	 * @param url
	 *            : takes in url
	 * @return : true if the link is starts with "https://en.wikipedia.org/wiki"
	 *         and is valid : false if the link does not start with the above
	 *         url or is not valid
	 */
	private boolean isValidLink(String url) {
		return ((url.toLowerCase().startsWith("https://en.wikipedia.org/wiki"))
				&& (!FILTERS.matcher(url.toLowerCase()).matches()) && (!CITATAIONS
				.matcher(url.toLowerCase()).matches()));
	}

	/**
	 * 
	 * @param url
	 *            : url of the link
	 * @param html
	 *            : html content of the url This method downloads the page of
	 *            url and saves it with its name after last "/" into the folder
	 *            DownloadedContent of the file hierarchy in the project in .txt
	 *            format
	 */
	private void downloadPage(String url, String html) {
		PrintWriter out;
		String filename = createFileNameFromUrl(url);
		try {
			if (BFS) {
				out = new PrintWriter(downloadPath
						+ "RawHTML_FocusedWebCrawler_2A/" + filename + ".txt");
				out.println(html);
			} else if (DFS) {
				out = new PrintWriter(downloadPath
						+ "RawHTML_FocusedWebCrawler_2B/" + filename + ".txt");
				out.println(html);
			} else {
				System.out.println("Cannot download.. Plase set "
						+ "either BFS or DFS flag to true");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param url
	 *            : the url of a web page
	 * @return : a String with a file name This method takes in url and then
	 *         returns the name of the file as the string after the last "/" of
	 *         the url For example
	 *         "https://en.wikipedia.org/wiki/Sustainable_energy" will return
	 *         "Sustainable_energy" as the name of the file
	 */
	private String createFileNameFromUrl(String url) {
		String[] temp = url.split("/");
		String filename = temp[temp.length - 1];
		return filename;

	}

	/**
	 * This method logs all the URLs in LinksTask02A.txt or LinksTask02B.txt
	 * depending on BFS search or DFS search in the DownloadedContent folder
	 */
	private void logVisitedPages() {
		try {
			PrintWriter printWriter;
			if (BFS) {
				printWriter = new PrintWriter(downloadPath + "LinksTask02A.txt");
				printWriter.println("Following pages were visited during BFS:");
				for (String visitedWebPage : visitedWebPages) {
					printWriter.println(visitedWebPage);
				}
				printWriter.close();
			} else if (DFS) {
				printWriter = new PrintWriter(downloadPath + "LinksTask02B.txt");
				printWriter.println("Following pages were visited during DFS:");
				for (String visitedWebPage : visitedWebPages) {
					printWriter.println(visitedWebPage);
				}
				printWriter.close();
			} else {
				System.out.println("Please set either DFS or BFS to true..");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param node
	 * @return
	 * This method returns true in case either of the following 
	 * is true:
	 * 1. The text of the page contains the key word
	 * 2. The anchor text of the link contains the key word
	 * 3. The url of the link has the key word
	 * else this method returns a false
	 */
	private boolean isValidCrawlNode(CrawlNode node) {
		return (urlHasKeyWord(node.getUrl()) || anchorTextHasKeyword(node) || pageTextHasKeyWord(node
				.getUrl()));
	}

	/**
	 * 
	 * @param url
	 * @return 
	 * This method returns true if the accepted url
	 * has the keyword
	 */
	private boolean urlHasKeyWord(String url) {
		if (url.contains(keyWord)) {
			pageTextHasKeyWord(url);
		}
		return (url.contains(keyWord));
	}

	/**
	 * 
	 * @param node
	 * @return
	 * This returns true if the anchor text of the accepted
	 * node has the keyword
	 */
	private boolean anchorTextHasKeyword(CrawlNode node) {
		if (node.getAnchorText().contains(keyWord)) {
			pageTextHasKeyWord(node.getUrl());
		}
		return (node.getAnchorText().contains(keyWord));
	}

	/**
	 * 
	 * @param url
	 * @return
	 * This method returns true if the page at accepted
	 * url has a keyword
	 */
	private boolean pageTextHasKeyWord(String url) {
		WebPage webPage = getWebPage(url);
		if (webPage.getHtml().toString().toLowerCase()
				.contains(keyWord.toLowerCase())) {
			downloadPage(url, webPage.getHtml());
			visitedWebPages.add(url);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @param url
	 * @return
	 * This method gets the page of the accepted url
	 * by using WebCrawlerThread class connect method
	 */
	private WebPage getWebPage(String url) {
		System.out.println("Getting web page:" + url);
		WebPage webPage = new WebPage();
		webPage = webCrawlerThread.connectToWebPage(url);
		return webPage;
	}

	public void setDFS(boolean dFS) {
		DFS = dFS;
	}

	public void setBFS(boolean bFS) {
		BFS = bFS;
	}
}
