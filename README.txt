COMS 4112 Project 2, Stage 2
Implementation of Algorithm 4.11 

Richard Chiou (rc2758)
Jennifer Lam (jl3953)

Important Files:
	BasicTerm.java: Defines the BasicTerm object class. Used in Subset.
	Costs.java: The Cost Model of the CPU, given by config.txt.
	Subset.java: A subset (collection) of BasicTerms, with costs and pointers 
		to other subsets (children) representing plan execution.
	Metric.java: A helper class used for testing the C- and D-metrics.
	Algorithm4_11.java: The complete implementation of Algorithm 4.11, plus a 
		function to output the plan of the code.
	Execution.java: The main executable file. Reads in config.txt and query.txt 
		parameters to initialize the parameters and sensititivities.
	
	output.txt: Generated upon running Execution. 

Usage:
	make
	./stage2.sh query.txt config.txt
	
Query.txt format:
	Each line of the query file contains a space-separated list of doubles 
		representing the selectivities of the basic terms.
		
	Example:
	
	0.8 0.5 0.3 0.2 0.2 0.1 
	0.9 0.6 0.75 0.8 1 0.9 
	0.8 0.8 0.9 0.7 0.7 0.7
	
Config.txt format:
	A list of costs estimatd from CPU specificiation. The values of the Clic 
		machines, used in the default file, are listed below:

	r = 1 
	t = 2 
	l = 1 
	m = 16 
	a = 2 
	f = 4

Ouput format in C code:

================================================================== 
0.7 0.4 0.2 0.3 0.6 				// Selectivies of basic terms - from query.txt
-----------------------------------------------------------------
if((t1[o1[i]] & t2[o2[i]]) && t3[o3[i]]) { 	// C code to execute query
	answer[j] = i; 
	j += (t4[o4[i]] & t5[o5[i]]); 
} 
-----------------------------------------------------------------
cost: 10.5 					// Optimal Cost given by the algorithm
================================================================== 

Sample tests:
	
	The results of the sample queries below are stored in the sample execution output file, "output.txt."
