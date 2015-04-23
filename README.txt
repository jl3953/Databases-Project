COMS 4112 Project 2, Stage 2
Implementation of Algorithm 4.11 

Richard Chiou (rc2758)
Jennifer Lam (jl3953)

There is one directory containing all relevant files for your convenience.

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
	
	output.txt: Generated upon running java execution query.txt config.txt. 
		(Note that this is different from the sample execution file,
		sampleoutput.txt - see below.)
	
Summary:
	This tarball implements Algorithm 4.11 described in the paper by Ken Ross et al 
		with the following conditions:
		
	* Algorithm NoBranch can only be applied to the last &-term in the optimal plan. 
	* The left node of a &&-expresion is always an &-term.
	* The costs of applying functions are equal for all functions.
	* The maximum subset size is approximately 9.

Usage:
	make
	./stage2.sh query.txt config.txt
	
Query.txt format:
	Each line of the query file contains a space-separated list of doubles 
		representing the selectivities of the basic terms.
		
	Sample query file submitted:
	
	0.8 0.5 0.3 0.2
	0.2 0.1 0.9
	0.6 0.75 0.8 1 0.9
	0.8 0.8 0.9 0.7 0.7 0.7
	0.05 0.1 0.05 0.1
	
Config.txt format:
	A list of costs estimatd from CPU specificiation. The values of the Clic 
		machines, used in the default file, are listed below:

	r = 1 
	t = 2 
	l = 1 
	m = 16 
	a = 2 
	f = 4

Sample Query Tests and C code Output: (See sampleoutput.txt)

=====================================================
0.8 0.5 0.3 0.2 				// Selectivies of basic terms - from query.txt
-----------------------------------------------------
if((t4[o4[i]])) && (((t3[o3[i]]))){ 		// C code to execute query
	answer[j] = i; 
 	j += (t1[o1[i]] & t2[o2[i]]); 
 }
-----------------------------------------------------
Cost: 13.34					// Optimal Cost given by the algorithm
=====================================================
0.2 0.1 0.9 
-----------------------------------------------------
if((t2[o2[i]])) && (((t1[o1[i]]))){ 
	answer[j] = i; 
 	j += (t3[o3[i]]); 
 }
-----------------------------------------------------
Cost: 9.76
=====================================================
0.6 0.75 0.8 1.0 0.9 
-----------------------------------------------------
if((t1[o1[i]] & t2[o2[i]])){ 
	answer[j] = i; 
 	j += (t3[o3[i]] & t4[o4[i]] & t5[o5[i]]); 
 }
-----------------------------------------------------
Cost: 28.75
=====================================================
0.8 0.8 0.9 0.7 0.7 0.7 
-----------------------------------------------------
if((t4[o4[i]] & t5[o5[i]] & t6[o6[i]])){ 
	answer[j] = i; 
 	j += (t1[o1[i]] & t2[o2[i]] & t3[o3[i]]); 
 }
-----------------------------------------------------
Cost: 31.00
=====================================================
0.05 0.1 0.05 0.1 
-----------------------------------------------------
if((t3[o3[i]])) && (((t1[o1[i]])) && (((t4[o4[i]])))){ 
	answer[j] = i; 
 	j += (t2[o2[i]]); 
 }
-----------------------------------------------------
Cost: 8.21325



