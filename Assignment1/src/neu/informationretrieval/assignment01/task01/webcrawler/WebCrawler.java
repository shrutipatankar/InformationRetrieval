package neu.informationretrieval.assignment01.task01.webcrawler;

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

/**
 * 
 * @author shruti This Class contains methods to: 1. Crawl a node 2. Log all the
 *         visited pages 3. Download a page (save the text of HTML as raw) 4.
 *         Check if the link is valid in order to visit it
 */

public class WebCrawler {

	private CrawlNode seed;
	private WebCrawlerThread webCrawlerThread;
	private Set<String> visitedWebPages;
	private List<CrawlNode> pagesToVisit;
	private int pagesToCrawl;
	private int maxNodeDepthToCrawl;
	private String downloadPath;
	private long startTime;
	private long endTime;

	/*
	 * These filters ensure : 
	 * 1. The links are not administrative (i.e do not contain colon) 
	 * 2. The links are not pointing to different sections of the
	 *    same page (i.e do not contain #) 
	 * 3. The links are not pointing to css/js/gif/jpg/svg/jpeg/mp3/zip 
	 *    files (i.e are only articles)
	 */
	private final static Pattern FILTERS = Pattern
			.compile(".*(\\.(css|js|gif|jpg|svg|jpeg" + "|png|mp3|zip|gz))$");
	private final static Pattern CITATAIONS = Pattern.compile("(.*)(#)(.*)");
	private final static Pattern ADMINISTRATIVE = Pattern
			.compile("(.*)(:)(.*)");

	/**
	 * According the the problem statement this constructor 
	 * sets the pages to crawl, the max depth of the node
	 * and initializes the data members of this class
	 */
	public WebCrawler() {
		downloadPath="DownloadedContent/RawHTML_WebCrawler/";
		this.pagesToCrawl = 1000;
		this.maxNodeDepthToCrawl = 5;
		this.seed = new CrawlNode();
		visitedWebPages = new LinkedHashSet<String>();
		pagesToVisit = new ArrayList<CrawlNode>();
		webCrawlerThread = new WebCrawlerThread();
	}

	/**
	 * This method will start crawling the web pages. 
	 * This is the method that needs to be called from a 
	 * WebCrawlerRunner Class.
	 */
	public void crawl(String seed) {
		this.seed.setUrl(seed);
		this.seed.setDepth(1);
		System.out.println("Seed URL:" + this.seed.getUrl());
		System.out.println("Seed depth " + this.seed.getDepth());
		pagesToVisit.add(this.seed);
		startTime = System.currentTimeMillis();
		crawlNextNode();
		endTime = System.currentTimeMillis();
		System.out.println("Time taken by this program: "+((endTime - startTime)/60000)+ "mins");
		logVisitedPages();
	}

	/**
	 * This method simply gets the next node in pagesToVisit set
	 * and checks if it is feasible to visit that node. It
	 * also logs the urls visited once its done crawling MAX 
	 * number of nodes
	 */
	private void crawlNextNode() {
		while (visitedWebPages.size() < pagesToCrawl) {
			if (pagesToVisit.isEmpty()) {
				System.out.println("Oooppss! We are out of pages");
				break;
			} else {
				CrawlNode node = pagesToVisit.iterator().next();
				checkIfPageVisitPossible(node);
			}
		}
	}

	/**
	 * This method logs all the URLs in LinksTask01.txt file in the 
	 * DownloadedContent folder
	 */
	private void logVisitedPages() {
		try {
			PrintWriter printWriter = new PrintWriter(
					"DownloadedContent/LinksTask01.txt");
			printWriter.println("Following pages were visited:");
			for (String visitedWebPage : visitedWebPages) {
				printWriter.println(visitedWebPage);
			}
			printWriter.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param node : CrawlNode
	 * This method checks if the node visit it possible 
	 * 1. The node is not already visited
	 * 2. The node is not exceeding the MAX allowed depth
	 */
	private void checkIfPageVisitPossible(CrawlNode node) {
		if (!visitedWebPages.contains(node.getUrl())
				&& node.getDepth() <= maxNodeDepthToCrawl) {
			System.out.println("Visiting page " + node.getUrl());
			visitPage(node);
		} else {
			pagesToVisit.remove(node);
			crawlNextNode();
		}
	}

	/**
	 * @param node: CrawlNode
	 * This method visits a page i.e:
	 * 1. Gets the web page from internet
	 * 2. Downloads the web page
	 * 3. Attaches the children for the current node
	 */
	private void visitPage(CrawlNode node) {
		WebPage webPage = new WebPage();
		webPage = webCrawlerThread.connectToWebPage(node.getUrl());
		Elements links = webPage.getLinks();
		String html = webPage.getHtml();
		downloadPage(node.getUrl(), html);
		if (node.getDepth() < 5) {
			attachChildren(node, links);
		}
		visitedWebPages.add(node.getUrl());
		pagesToVisit.remove(node);
		crawlNextNode();

	}

	/**
	 * 
	 * @param root   : The CrawlNode to which the children will be attached 
	 * @param links  : The href links of the root page
	 * This method gets the valid links on the current node which is 
	 * denoted as root here. By valid links, it means that:
	 * 1. The links are only articles 
	 * 2. The links are non administrative
	 * 3. The links do not refer to the some section of the 
	 *    same Web Page. 
	 * It then attaches every such valid link as a child to the root node
	 */
	private void attachChildren(CrawlNode root, Elements links) {
		Elements validLinks = getValidLinks(links);
		// printUrlsInCurrentPage(validLinks);
		Set<CrawlNode> children = new HashSet<CrawlNode>();
		for (Element link : validLinks) {
			CrawlNode child = new CrawlNode();
			child.setDepth(root.getDepth() + 1);
			child.setUrl(link.absUrl("href"));
			pagesToVisit.add(child);
			children.add(child);
		}
		root.setChildren(children);
	}

	/**
	 * 
	 * @param links : takes in links 
	 * (hyper link like "https://en.wikipedia.org/wiki/Sustainable_energy")
	 * @return the set of Elements which are valid for the current problem 
	 * set. i.e 
	 * 1. The links are only articles 
	 * 2. The links are non administrative
	 * 3. The links do not refer to the some section of the 
	 *    same Web Page.
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
	 * @return : true if the url is not administrative
	 * 		   : false if the url is administrative
	 */
	private boolean isNonAdministrativeUrl(String url) {
		return (!ADMINISTRATIVE.matcher(url.toLowerCase()).matches());
	}


	/**
	 * 
	 * @param url : takes in url
	 * @return    : true if the link is starts with 
	 *              "https://en.wikipedia.org/wiki" and is valid
	 *            : false if the link does not start with the above 
	 *              url or is not valid
	 */
	private boolean isValidLink(String url) {
		return ((url.toLowerCase().startsWith("https://en.wikipedia.org/wiki"))
				&& (!FILTERS.matcher(url.toLowerCase()).matches()) && (!CITATAIONS
				.matcher(url.toLowerCase()).matches()));
	}

	/**
	 * 
	 * @param url  : url of the link
	 * @param html : html content of the url
	 * This method downloads the page of url and saves it with
	 * its name after last "/" into the folder DownloadedContent
	 * of the file hierarchy in the project in .txt format
	 */
	private void downloadPage(String url, String html) {
		PrintWriter out;
		try {
			String filename = createFileNameFromUrl(url);
			out = new PrintWriter(downloadPath + filename + ".txt");
			out.println(html);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param url : the url of a web page
	 * @return    : a String with a file name
	 * This method takes in  url and then returns the name of
	 * the file as the string after the last "/" of the url
	 * For example "https://en.wikipedia.org/wiki/Sustainable_energy"
	 * will return "Sustainable_energy" as the name of the file
	 */
	private String createFileNameFromUrl(String url) {
		String[] temp = url.split("/");
		String filename = temp[temp.length - 1];
		return filename;

	}
}
