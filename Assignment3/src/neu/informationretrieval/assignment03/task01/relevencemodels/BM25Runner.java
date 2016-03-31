/**
 * 
 */
package neu.informationretrieval.assignment03.task01.relevencemodels;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author shruti
 *
 */
public class BM25Runner {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String fileName = "Input/queries.txt";
		String line = null;
		FileReader fileReader;
		try {
			fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while ((line = bufferedReader.readLine()) != null) {
				BM25 bm25 = new BM25();
				bm25.rankDocuments(line);
			}
			bufferedReader.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
