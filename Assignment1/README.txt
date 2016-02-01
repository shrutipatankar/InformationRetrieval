INSTRUCTIONS: HOW TO RUN THIS CODE?

--------------------------------------------------------------------------------------------------------------------------
PROBLEM STATEMENT:
--------------------------------------------------------------------------------------------------------------------------
Task 1: Crawling the documents:

1. Start with the following seed URL: http://en.wikipedia.org/wiki/Sustainable_energy;
2. Your crawler has to respect the politeness policy by using a delay of at least one second between your HTTP requests.
3. Follow the links with the prefix http://en.wikipedia.org/wiki that lead to articles only
4. (avoid administrative links containing : ). Non-English articles and external links must not be followed.
5. Crawl to depth 5. The seed page is the first URL in your frontier and thus counts for depth 1.  
6. Stop once you’ve crawled 1000 unique URLs. Keep a list of these URLs in a text file. Also, keep the downloaded 
   documents (raw html, in text format) with their respective URL for future tasks (transformation, indexing, etc.) 
   

--------------------------------------------------------------------------------------------------------------------------
PROGRAM IMPLEMENTATION OF Task 1
--------------------------------------------------------------------------------------------------------------------------


FOR RUNNING THIS FIRST PART OF THE PROBLEM STATEMENT i.e (Task 1):
1. GO TO PROJECT : Assignment1/src/
2. OPEN PACKAGE  : neu.informationretrieval.assignment01.task01.webcrawler
3. GO TO CLASS   : WebCrawlerRunner
4. CLICK RUN
5. THE OUTPUT OF THIS PROGRAM IS SAVED IN DownloadedContent

NOTE: THE OUTPUT FOR THIS CODE IS COLLECTED IN TWO PARTS:
A. DownloadedContent/LinksTask01.txt : Contains all the links that are visited while crawling
B. DownloadedContent/RawHTML_WebCrawler : HTML content of all the visited URLs in txt format (Raw)


--------------------------------------------------------------------------------------------------------------------------


Task 2: Focused Crawling: 

Your crawler should be able to consume two arguments: a URL and a keyword to be matched against text, anchor text, or text
within a URL.Starting with the same seed in Task 1, crawl to depth 5 at most, using the keyword “solar”. You should return 
at most 1000 URLS for each of the following:

1. Breadth first crawling
2. Depth first crawling
3. In a few sentences compare and explain the approaches above. Briefly compare the results obtained in A & B in this 
   task in terms of the total number of URLs crawled, and the top 5 URLs (topical content).
   
   
--------------------------------------------------------------------------------------------------------------------------
PROGRAM IMPLEMENTATION OF Task 2
--------------------------------------------------------------------------------------------------------------------------


FOR RUNNING THIS SECOND PART OF THE PROBLEM STATEMENT i.e (Task 2A BFS):

1. GO TO PROJECT : Assignment1/src/
2. OPEN PACKAGE  : neu.informationretrieval.assignment01.task02.focusedwebcrawler;
3. GO TO CLASS   : FocusedWebCrawlerRunner
4. CLICK RUN
5. THE OUTPUT OF THIS PROGRAM IS SAVED IN DownloadedContent

NOTE: THE OUTPUT FOR THIS CODE IS COLLECTED IN TWO PARTS:
A. DownloadedContent/LinksTask02A.txt : Contains all the links that are visited while crawling
B. DownloadedContent/RawHTML_FocusedWebCrawler_2A : HTML content of all the visited URLs in txt format (Raw)


--------------------------------------------------------------------------------------------------------------------------

FOR RUNNING THIS SECOND PART OF THE PROBLEM STATEMENT i.e (Task 2B DFS):

1. GO TO PROJECT : Assignment1/src/
2. OPEN PACKAGE  : neu.informationretrieval.assignment01.task02.focusedwebcrawler;
3. GO TO CLASS   : FocusedWebCrawlerRunner
4. COMMENT FOLLOWING LINE IN THE CODE
	focusedWebCrawler.setBFS(true);
5. UNCOMMENT FOLLOWING LINE IN THE CODE
    focusedWebCrawler.setDFS(true);
6. CLICK RUN
7. THE OUTPUT OF THIS PROGRAM IS SAVED IN DownloadedContent

NOTE: THE OUTPUT FOR THIS CODE IS COLLECTED IN TWO PARTS:
A. DownloadedContent/LinksTask02B.txt : Contains all the links that are visited while crawling
B. DownloadedContent/RawHTML_FocusedWebCrawler_2B : HTML content of all the visited URLs in txt format (Raw)


--------------------------------------------------------------------------------------------------------------------------

