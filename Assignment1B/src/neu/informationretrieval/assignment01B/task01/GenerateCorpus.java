package neu.informationretrieval.assignment01B.task01;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 
 * @author shruti
 * This class implements task 01 of the assignment.
 * The corpusFolderPath corresponds to the raw data collected 
 * while crawling the web.
 * The outputFolderPath contains 1 file per raw data file
 * which has data processed as follows:
 * 
 *  1. Case Folding: All the letters are converted to lower case
 *  2. Punctuation : All the punctuation is removed apart from
 *                   between the numbers. (Symbols like '%','$'
 *                   are retained)
 *  3. Page Title  : Page title appears at the beginnning of the 
 *                   document. (This is same as the filename stored)
 * 
 */
public class GenerateCorpus {

	File corpusFolderPath;
	String outputFolderPath;

	/**
	 * Initializes the folder paths for source 
	 * and destination. Both path exist in the project.
	 */
	GenerateCorpus() {
		corpusFolderPath = new File("RawHTML_WebCrawler");
		outputFolderPath = "OutputRawFiles";
	}

	/**
	 * This method is public which uses internal private 
	 * methods to process all the data
	 */
	public void processRawData() {
		extractDataFromAllFiles();
	}

	/**
	 * This method accesses each file from the corpus folder path
	 * and passes the url of the file to parseTextFromFile
	 */
	private void extractDataFromAllFiles() {
		File[] listOfFiles = corpusFolderPath.listFiles();
		for (File file : listOfFiles) {
			if (file.isFile()) {
				parseTextFromFile(corpusFolderPath + "/" + file.getName());
			}
		}
	}

	/**
	 * 
	 * @param url : location of the file
	 * 
	 */
	public void parseTextFromFile(String url) {
		try {
			String text = "";
			File input = new File(url);
			Document doc = Jsoup.parse(input, "UTF-8", "");
			
			// get the p tag value from the body tag value
			Elements paragraphsFromBody = doc.body().select("p");
			
			// extract title
			String title = createFileNameFromUrl(url);
			text = title + " \n";
			for (Element para : paragraphsFromBody) {
				text = text + " " + para.text();
			}

			// case folding
			text = text.toLowerCase();

			// remove references in square brackets
			text = text.replaceAll("(\\[.*?\\])", "");

			// replace colons with white spaces
			text = text.replaceAll(":", " ");
			text = text.replaceAll("/", " ");
			text = text.replaceAll(" ", " ");
			

			// remove punctuation
			text = removePunctuation(text);

			System.out.println("Writing raw data to files..");

			File output = new File(outputFolderPath + "/" + title + ".txt");
			if(!output.exists()){
				PrintWriter writer = new PrintWriter(output, "UTF-8");
				writer.println(text);
				writer.close();
			}else{
				System.out.println("found duplicate file for " + output);
			}
		

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param text : text in the form of String
	 * @return text after removing all the punctuations
	 * The punctuation from characters is removed. The punctuation
	 * in between the numbers is retained
	 */
	public String removePunctuation(String text) {
		String tokens[] = text.split(" ");
		text = "";
		for (String token : tokens) {
			text = text + " " + processToken(token);
		}
		return text;
	}

	/**
	 * 
	 * @param token : String comprising of single word
	 * @return a token after it is processed based on 
	 * whether it is a number string or character string
	 */
	public String processToken(String token) {
		String rawToken = getRawToken(token);
		if (StringUtils.isNumeric(getRawToken(token))) {
			return getProcessedNumericToken(token);
		} else {
			return rawToken;
		}
	}

	/**
	 * 
	 * @param token : number String
	 * @return processed number String
	 * If a ',' or '.' occur in between the digits, it is retained
	 * If a '%' or '$' occur at beginning or end of the String it 
	 * is retained
	 */
	public String getProcessedNumericToken(String token) {
		
		char[] charArray = token.toCharArray();
		char tempArray[] = new char[charArray.length];
		int count = 0;
		for (int i = 0; i < charArray.length; i++) {
			if (Character.isDigit(charArray[i])) {
				tempArray[count] = charArray[i];
				count++;
			} else if (!Character.isDigit(charArray[i]) && i != 0
					&& i != (charArray.length - 1)) {
				if (charArray[i] == '%' || charArray[i] == '$'
						|| charArray[i] == ',' || charArray[i] == '.') {
					tempArray[count] = charArray[i];
					count++;
				}
			} else if (!Character.isDigit(charArray[i])
					&& (i == 0 || i == (charArray.length - 1))) {
				if (charArray[i] == '%' || charArray[i] == '$') {
					tempArray[count] = charArray[i];
					count++;
				} 
			}
		}
		/*System.out.println("String conversion of "
				+ String.copyValueOf(charArray) + " to "
				+ String.valueOf(tempArray));*/
		return String.valueOf(tempArray);
	}

	/**
	 * 
	 * @param token character/number String
	 * @return a chacater/number String after removing:
	 * 1. '.'
	 * 2. ','
	 * 3. '/'
	 * 4. '#'
	 * 5. '!'
	 * 6. '$'
	 * 7. '%'
	 * 8. '^'
	 * 9. '&'
	 * 10. '*'
	 * 11. '"'
	 * 12. '''
	 * 13. ';'
	 * 14. ':'
	 * 15. '{'
	 * 16. '}'
	 * 17. '='
	 * 18. '_'
	 * 19. '-'
	 * 20. '`'
	 * 21. '~'
	 * 22. '('
	 * 23. ')'
	 */
	public String getRawToken(String token) {
		token = token.trim();
		if (token.length() > 0) {
			if (token.length() == 1){
				if(token.contains("-") || token.contains("–")){
					return " ";
				}
			}else if (token.length() == 2) {
				if ((token.charAt(0) == '-' || token.charAt(0) == '–')) {
					token = String.valueOf(token.charAt(1));
				}else if((token.charAt(token.length() - 1) == '-'
						|| token.charAt(token.length() - 1) == '–')){
					token = String.valueOf(token.charAt(0));
				}
			} else{
				if(token.charAt(0) == '-' || token.charAt(0) == '–') {
					token = token.substring(1, token.length() - 1);
				} else if (token.charAt(token.length() - 1) == '-'
						|| token.charAt(token.length() - 1) == '–') {
					token = token.substring(0, token.length() - 2);
				}
			}
		}
		
		return token.replaceAll("[.,\\/#!$%\\^&\\*\"“”;:{}=_`~()\'’?ˈ<>‘@]", "");
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
		filename = filename.replaceAll("[_-]", "");
		filename = filename.replace(".txt", "");
		System.out.println("filename = " + filename);
		return filename;
	}
}
