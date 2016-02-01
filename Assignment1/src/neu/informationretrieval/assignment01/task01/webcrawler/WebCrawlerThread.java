package neu.informationretrieval.assignment01.task01.webcrawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
/**
 * 
 * @author shruti This class is connects to a Web Page using JSOUP It has a
 *         WebPage and a Document (JSOUP document)
 * 
 */
public class WebCrawlerThread {

	private WebPage webPage;
	private Document document;

	public WebCrawlerThread() {
		webPage = new WebPage();
	}

	WebCrawlerThread(String url) {
		webPage = new WebPage();
	}

	/**
	 * 
	 * @param url : Takes in a url of a Web Page
	 * @return    : a WebPage of the given Url
	 * 
	 *         This method connects to a webpage using JSOUP library. It then
	 *         fetches that web page, collects its links and its HTML content.
	 *         It then creates a WebPage object which it returns. Before
	 *         returning a WebPage, it stops the execution of the program for 1
	 *         sec. i.e Politeness Policy as asked by the Problem Statement
	 */
	public WebPage connectToWebPage(String url) {
		try {
			document = Jsoup.connect(url).get();
			webPage.setLinks(document.select("a[href]"));
			webPage.setHtml(document.html());
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return webPage;
	}
}
