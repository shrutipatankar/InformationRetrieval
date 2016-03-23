Assignment 1B 

How to run this program?

1. Open the project in an IDE
2. Go to neu.informationretrieval.assignment01B.executor package in src folder
3. Click to run the InvertedIndexCreatorRunner
	- This program first runs GenerateCorpusRunner 
		- GenerateCorpusRunner picks raw input HTML files from RAWHTML_WebCrawler Folder
		- RAWHTML_WebCrawler contains 1000 files crawled during PartA of this assignment
		- The program outputs raw text files which contain only text content from the html
		  of main body tags (1 text file is created per html file)
		- The program detects duplicates in file names and does not create copies of same file
		- The output is created in folder named: OutputRawFiles
	- The Program then runs to execute InvertedIndexGenerator
		- This program creates tokens (unigrams, bigrams, and trigrams) from the given Corpus
		- docID is hashcode of the name of the file (Results/hashCodeToDocIds.txt contains the mapping)
		- There are 6 tables generated as output 
			: 3 for termFrequency [term termFreq] (sorted based on tf in descending order)
		    : 3 for docFrequency [term numOfDocs SetOfDocs(these contain docIDs)] 
		      (sorted based on term lexicographically in ascending order)
		- All these tables are generated under Folder "Results"
		- The program prints time taken by the program to execute
		
References:
http://stackoverflow.com/questions/109383/sort-a-mapkey-value-by-values-java
http://nlp.stanford.edu/IR-book/html/htmledition/dropping-common-terms-stop-words-1.html
http://datascience.stackexchange.com/questions/5893/how-to-create-a-good-list-of-stopwords
Prof Nada Naji's lecture slides

