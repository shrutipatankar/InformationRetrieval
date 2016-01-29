package neu.informationretrieval.assignment01.webcrawler;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Pattern;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebCrawler {

	private CrawlNode seed;
	final ExecutorService service;
	private Future<WebPage> task;
	private Set<String> visitedWebPages;
	private List<CrawlNode> pagesToVisit;

	private final static Pattern FILTERS = Pattern
			.compile(".*(\\.(css|js|gif|jpg|svg|jpeg"
					+ "|png|mp3|mp3|zip|gz))$");
	private final static Pattern CITATAIONS = Pattern.compile("(.*)(#)(.*)");
	private final static Pattern ADMINISTRATIVE = Pattern
			.compile("(.*)(:)(.*)");

	public WebCrawler(String seed) {
		this.seed = new CrawlNode();
		this.seed.setUrl(seed);
		this.seed.setDepth(1);
		this.service = Executors.newFixedThreadPool(7);
		visitedWebPages = new HashSet<String>();
		pagesToVisit = new ArrayList<CrawlNode>();
	}

	/**
	 * This method will start crawling the web pages. This is the method that
	 * needs to be called from a WebCrawlerRunner Class.
	 */
	public void crawl() {
		System.out.println("Seed URL:" + seed.getUrl());
		System.out.println("Seed depth " + seed.getDepth());
		pagesToVisit.add(seed);
		crawlNextNodeByBFS();
	}

	private void crawlNextNodeByBFS() {
		while (visitedWebPages.size() < 1000) {
			if (pagesToVisit.isEmpty()) {
				System.out.println("Oooppss! We are out of pages");
			} else {
				CrawlNode node = pagesToVisit.iterator().next();
				checkIfPageVisitPossible(node);
			}
		}
		service.shutdownNow();
		logVisitedPages();
	}

	private void logVisitedPages() {
		try {
			PrintWriter printWriter = new PrintWriter(
					"DownloadedContent/Links.txt");
			printWriter.println("Following pages were visited:");
			for(String visitedWebPage: visitedWebPages){
				printWriter.println(visitedWebPage);
			}
			printWriter.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void checkIfPageVisitPossible(CrawlNode node) {
		if (!visitedWebPages.contains(node.getUrl()) && node.getDepth() <= 5) {
			System.out.println("Visiting page " + node.getUrl());
			visitPage(node);
		} else {
			pagesToVisit.remove(node);
			crawlNextNodeByBFS();
		}
	}

	private void visitPage(CrawlNode node) {
		System.out.println("Visiting web page:" + node.getUrl());
		try {
			task = service.submit(new WebCrawlerThread(node.getUrl()));
			WebPage webPage = new WebPage();
			webPage = task.get();
			Elements links = webPage.getLinks();
			String html = webPage.getHtml();
			downloadPage(node.getUrl(), html);
			attachChildren(node, links);
			visitedWebPages.add(node.getUrl());
			pagesToVisit.remove(node);
			crawlNextNodeByBFS();
		} catch (final InterruptedException ex) {
			ex.printStackTrace();
		} catch (final ExecutionException ex) {
			ex.printStackTrace();
		}
	}

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

	private boolean isNonAdministrativeUrl(String url) {
		return (!ADMINISTRATIVE.matcher(url.toLowerCase()).matches());
	}

	private boolean isValidLink(String url) {
		return ((url.toLowerCase().startsWith("https://en.wikipedia.org/wiki"))
				&& (!FILTERS.matcher(url.toLowerCase()).matches()) && (!CITATAIONS
				.matcher(url.toLowerCase()).matches()));
	}

	private void downloadPage(String url, String html) {
		PrintWriter out;
		try {
			String filename = createFileNameFromUrl(url);
			out = new PrintWriter("DownloadedContent/RawHTML/" + filename
					+ ".html");
			out.println(html);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String createFileNameFromUrl(String url) {
		String[] temp = url.split("/");
		String filename = temp[temp.length - 1];
		return filename;

	}

	private void printUrlsInCurrentPage(Elements links) {
		for (Element link : links) {
			System.out.println("Child URL:" + link.absUrl("href"));
		}
	}
}
