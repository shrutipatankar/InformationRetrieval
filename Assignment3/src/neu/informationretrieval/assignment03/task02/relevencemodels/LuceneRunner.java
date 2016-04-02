package neu.informationretrieval.assignment03.task02.relevencemodels;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.apache.commons.io.FileUtils;

public class LuceneRunner {

	public static void main(String args[]){
		try {
			File file = new File("LuceneIndexOutput");
			FileUtils.deleteDirectory(file);
		    LueceneIndexer indexer = new LueceneIndexer();
		    indexer.indexFiles();
		    
		    FileReader fileReader;
			String fileName = "Input/queries.txt";
			String line = null;
			LuceneQueryProcessor luceneQueryProcessor = new LuceneQueryProcessor();
				fileReader = new FileReader(fileName);
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				while ((line = bufferedReader.readLine()) != null) {
					luceneQueryProcessor.searchDocuments(line);
				}
				bufferedReader.close(); 
		} catch (Exception ex) {
		    ex.printStackTrace();
		    System.exit(-1);
		}
	}
}
