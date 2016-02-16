INSTRUCTIONS: HOW TO RUN THIS CODE?

--------------------------------------------------------------------------------------------------------------------------
PROBLEM STATEMENT:
--------------------------------------------------------------------------------------------------------------------------
Task 2A AND 2B: PAGE RANK AND ENTROPY CALCULATION (PART OF PAGE RANK)

Implement the PageRank algorithm.PageRank can be computed iteratively by following given pseudocode	
Run your iterative version of PageRank algorithm on WG1 and WG2 respectively until their PageRank values "converge". 
To test for convergence, calculate the perplexity of the PageRank distribution, where perplexity is simply 2 raised to 
the (Shannon) entropy of the PageRank distribution, i.e., 2^H (PR)   

--------------------------------------------------------------------------------------------------------------------------
PROGRAM IMPLEMENTATION OF Task 2A and 2B (Perplexity calculation)
--------------------------------------------------------------------------------------------------------------------------


FOR RUNNING THE ABOVE PART OF THE PROBLEM STATEMENT i.e (Task 2A AND 2B):
OPEN THE PROJECT IN A IDE (FOR EXAMPLE: ECLIPSE)
1. GO TO PROJECT : Assignment2/src/
2. OPEN PACKAGE  : neu.informationretrieval.assignment02.task02.pagerank
3. GO TO CLASS   : TestBuildGraph
4. CLICK RUN
5. THE OUTPUT OF THIS PROGRAM IS SAVED IN Output
6. REPEAT THE ABOVE STEPS BY UNCOMMENTING FILE NAME OF WG2.TXT AND COMMENTING IT FOR WG1.TXT IN TestBuildGraph

NOTE: THE OUTPUT FOR THIS CODE IS COLLECTED IN TWO PARTS:
A. GraphPlotData.txt : contains the number of pages that have certain number of incoming links
B. PageRank.txt : Each document id along with its page rank (not sorted)
C. PagesSortedByIncomingLinks.txt : Each Document ID sorted in descending order by number of incoming links
D. SortedPageRank.txt : Each document id sorted by page rank 

NOTE: There are also log files generated under Logs folder for each independent run of the program

--------------------------------------------------------------------------------------------------------------------------
Task 3A:

Sort the pages in WG1 and WG2 each by their in-link counts (from highest to lowest in-link count), and output the Top 
50 pages for each graph. Applying Kendall Tau rank correlation coefficient to evaluate the difference between two 
ranking lists. Kendall Tau rank correlation coefficient can be computed as follows: τ = P(C) − P(D) = (C − D)/ N 
where N is the total number of pairs for comparing items (in our task, you can find the items as the shared common 
pages in two Top 50 ranking lists. For better measurement, we should compare the entire ranking lists, but for 
efficiency concern, we just compare the Top 50); C is the number of concordant pairs, i.e., the number of pairs for 
which their relative ordering is preserved in the two lists; D is the number of discordant pairs, i.e., number of 
pairs for which their relative ordering is reversed in the two lists; The greater the Kendall Tau value, the more 
consistent (agreement) the two ranking lists.
   
--------------------------------------------------------------------------------------------------------------------------
PROGRAM IMPLEMENTATION OF Task 3A
--------------------------------------------------------------------------------------------------------------------------

FOR RUNNING THIS SECOND PART OF THE PROBLEM STATEMENT i.e (Task 3A):

OPEN THE PROJECT IN A IDE (FOR EXAMPLE: ECLIPSE)
1. GO TO PROJECT : Assignment1/src/
2. OPEN PACKAGE  : neu.informationretrieval.assignment02.task02.kendalltau
3. GO TO CLASS   : KendallTauCorelationCoeffecientRunner
4. CLICK RUN
5. THE OUTPUT OF THIS PROGRAM IS SAVED IN Logs

NOTE: THE OUTPUT FOR THIS CODE IS COLLECTEDAS FOLLOWS:
THE LOG FILE THAT IS CREATED CONTAINS ALL THE RESULTS OF ALL THE EXPECTED TAU VALUES
--------------------------------------------------------------------------------------------------------------------------
