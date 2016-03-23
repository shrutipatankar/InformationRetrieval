package neu.informationretrieval.assignment01B.executor;

import neu.informationretrieval.assignment01B.task01.GenerateCorpusRunner;
import neu.informationretrieval.assignment01B.task02.InvertedIndexGeneratorRunner;

public class InvertedIndexCreatorRunner {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long startTime = System.currentTimeMillis();
		String[] noArgs = {};
		GenerateCorpusRunner.main(noArgs);
		InvertedIndexGeneratorRunner.main(noArgs);
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println(totalTime);
	}

}
