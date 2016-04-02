package neu.informationretrieval.assignment03.task02.relevencemodels;
import java.io.File;

import java.io.IOException;
import java.io.PrintWriter;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * To create Apache Lucene index in a folder and add files into this index based
 * on the input of the user.
 */
public class LuceneQueryProcessor {
	
	private String outputLocation;
	private static Analyzer sAnalyzer = new SimpleAnalyzer(Version.LUCENE_47);

	/**
	 * Constructor
	 * 
	 * @param indexDir
	 *            the name of the folder in which the index should be created
	 * @throws java.io.IOException
	 *             when exception creating index.
	 */
	LuceneQueryProcessor() throws IOException {
		outputLocation = "LuceneIndexOutput";
	}

	public void searchDocuments(String query) {
		query = query.trim();
		query = query.replaceAll("\\s+", " ");
		
		String queryWords[] = query.split(" ");
		int queryNumber = Integer.parseInt(queryWords[0]);
		
		query = filterQueryWords(queryWords);
		queryWords = filterQueryWordsForFileName(queryWords);
		
		IndexReader reader;
		try {

			reader = DirectoryReader.open(FSDirectory.open(new File(
					outputLocation)));
			IndexSearcher searcher = new IndexSearcher(reader);
			TopScoreDocCollector collector = TopScoreDocCollector.create(100,
					true);

			Query q = new QueryParser(Version.LUCENE_47, "contents", sAnalyzer)
					.parse(query);

			searcher.search(q, collector);
			ScoreDoc[] hits = collector.topDocs().scoreDocs;

			PrintWriter writer;
			File file = new File("LuceneOutput/"+createOutputFileName(queryWords));
			writer = new PrintWriter(file, "UTF-8");
			System.out.println("Found " + hits.length + " hits.");
			for (int i = 0; i < hits.length; ++i) {
				int docId = hits[i].doc;
				Document d = searcher.doc(docId);
				//System.out.println(searcher.explain(q, docId));
				String docName = extractFileNameFromPath(d.get("path"));
				writer.println(queryNumber + " " + "Q0" + " " + docName + 
				" " + (i+1) + " " + hits[i].score + " " + "Lucene");
			}
			writer.close();
			// 5. term stats --> watch out for which "version" of the term
			// must be checked here instead!
			Term termInstance = new Term("contents", query);
			long termFreq = reader.totalTermFreq(termInstance);
			long docCount = reader.docFreq(termInstance);
			System.out.println(query + " Term Frequency " + termFreq
					+ " - Document Frequency " + docCount);

		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	private String extractFileNameFromPath(String path){
		String pathTokens[] = path.split("/");
		return pathTokens[pathTokens.length-1];
		
	}
	
	private String[] filterQueryWordsForFileName(String queryWords[]) {
		String temp[] = new String[queryWords.length-1];
		int count = 0;
		for (int i = 1; i < queryWords.length ; i++) {
			temp[count] = queryWords[i];
			count++;
		}
		return temp;
	}
	
	private String filterQueryWords(String queryWords[]) {
		String temp = ""; 
		for (int i = 1; i < queryWords.length ; i++) {
			temp = temp + " " + queryWords[i];
		}
		return temp;
	}
	
	private String createOutputFileName(String queryWords[]) {
		String fileName = "lucene";
		for (int i = 0; i < queryWords.length; i++) {
			fileName = fileName + "_" + queryWords[i];
		}
		fileName = fileName + ".txt";
		return fileName;
	}
}