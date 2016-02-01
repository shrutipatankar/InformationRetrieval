package neu.informationretrieval.assignment01.task01.webcrawler;

import org.jsoup.select.Elements;

/**
 * 
 * @author shruti
 * This class represents a WebPage in terms of
 * 1. HTML content of the web page
 * 2. URL links on the Web Page
 */
public class WebPage {
	String html;
	Elements links;

	public WebPage() {
		html = "";
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public Elements getLinks() {
		return links;
	}

	public void setLinks(Elements links) {
		this.links = links;
	}
}
