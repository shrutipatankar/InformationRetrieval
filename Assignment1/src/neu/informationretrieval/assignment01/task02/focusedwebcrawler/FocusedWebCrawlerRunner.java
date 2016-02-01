package neu.informationretrieval.assignment01.task02.focusedwebcrawler;

/**
 * 
 * @author shruti
 * This class invokes the FocusedWebCrawler instance to start crawling.
 * The seed provided here takes two arguments:
 * 1. seed
 * 2. keyword
 */
public class FocusedWebCrawlerRunner {
	public static void main(String[] args){
		String seed = "https://en.wikipedia.org/wiki/Sustainable_energy";
		String keyWord = "solar";
		FocusedWebCrawler focusedWebCrawler=new FocusedWebCrawler();
		focusedWebCrawler.setBFS(true);
		//focusedWebCrawler.setDFS(true);
		focusedWebCrawler.crawl(seed,keyWord);
	}
}
