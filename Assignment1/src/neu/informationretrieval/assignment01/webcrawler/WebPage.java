package neu.informationretrieval.assignment01.webcrawler;

import org.jsoup.select.Elements;

public class WebPage {
	String html;
	Elements links;

	WebPage() {
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
