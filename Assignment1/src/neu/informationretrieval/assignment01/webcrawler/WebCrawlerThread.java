package neu.informationretrieval.assignment01.webcrawler;

import java.io.IOException;
import java.util.concurrent.Callable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class WebCrawlerThread implements Callable<WebPage> {
	
	private WebPage webPage;
	private Document document;
	private String url;


	WebCrawlerThread(String url) {
		this.url = url;
		System.out.println("Creating thread..");
		webPage=new WebPage();
	}

	public WebPage call() {
		try {
			document = Jsoup.connect(url).get();
			webPage.setLinks(document.select("a[href]"));
			webPage.setHtml(document.html());
			//System.out.println("Thread is sleeping");
			Thread.sleep(1000);
			//System.out.println("Thread started again");
			//System.out.println(webPage.getLinks().size());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e2){
			e2.printStackTrace();
		}
		//System.out.println("Thread Exiting..");
		return webPage;
	}
}
