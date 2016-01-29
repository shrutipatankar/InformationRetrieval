package neu.informationretrieval.assignment01.webcrawler;


/**
 * 
 * @author shruti
 * This class simply calls the WebCrawler Class.
 * User is free to provide any seed to WebCrawler class using this class.
 */
public class WebCrawlerRunner {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// The seed can be any URL
		String seed="http://en.wikipedia.org/wiki/Sustainable_energy";
		WebCrawler webCrawler=new WebCrawler(seed);
		webCrawler.crawl();
		
	}

}
