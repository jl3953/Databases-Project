import java.util.*;

public class Algorithm4_11 {

    private ArrayList<Subset> A;	 	//array indexed by subset
    private Subset bigSet; 				//the set of basic terms given to us for testing
    private Costs c; 					//given configurations

    public Algorithm4_11(Subset bigSet, Costs c){
		this.bigSet = bigSet;
		this.c = c;
		this.A = this.initialize();
    }

    /**
      * Dynamic Programming algorithm that computes the least cost plan
      */

    public Subset runAlgorithm (){

		//Iterate over all subsets
		for (Subset s: this.A){ //s
			for (Subset sprime: this.A){ //sprime

				//check for intersection and skip this plan if there is intersection
				if (sprime.intersection(s))
					continue;

				//Otherwise, compute c and d metrics for both plans

				Metric CMetric = s.leftMost.cMetric;
				HashSet<Metric> DMetrics = s.DMetrics;
				Metric CMetricPrime = sprime.cMetric;
				Metric DMetricPrime = sprime.dMetric;

				//C-metric test: lemma 4.8
				if (CMetric.a < CMetricPrime.a && CMetric.b <= CMetricPrime.b)
					continue;

				//D-metric test: lemma 4.9
				if (sprime.p <= 0.5) {
				    boolean passedTheDTest = true;
				    for (Metric metric : DMetrics) {
					if (metric.a < DMetricPrime.a && metric.b < DMetricPrime.b) {
					    passedTheDTest = false;
					    break;
					}
				    }
				    if (!passedTheDTest)
					continue;
				}

				//Find the optimal plan

				//Union of s and sprime
				Subset u = this.A.get(sprime.union(s));
				double currentCost = u.cost;

				//Branching-and Cost (Equation 1)
				double FCost = sprime.computeFCost(c);
				double ps = sprime.p;
				double q = Math.min(ps, 1 - ps);
				double combinedCost = FCost + c.m*q + ps * s.cost;

				System.out.println(combinedCost + " " + currentCost);
				//if plan is optimal
				if (combinedCost < currentCost){
					u.newBranchingAnd(combinedCost, sprime, s);
				}
			}
		}

		// Return the biggest subset with the optimal plan
		System.out.println(A.size());
		System.out.println(A.get(A.size() -1).cost);
		return A.get(A.size()-1);

    }

   /**
     * Initializes the Subsets.
     * @return an ArrayList of subsets indexed by bitMap
     */
    private ArrayList<Subset> initialize(){

		ArrayList<Subset> output = new ArrayList<Subset>();
		int numberOfTerm = this.bigSet.size(); 				// k
		long possibleSubsets = (int) Math.pow(2, numberOfTerm); 	// 2^k, maximum number of subsets

		int index = 1;
		ArrayList<BasicTerm> listOfBasicTerms = this.bigSet.getTerms();

		while (index < possibleSubsets) {
			BitSet bs = BitSet.valueOf(new long[]{index});
			ArrayList<BasicTerm> subsetTerms = new ArrayList<BasicTerm>();
			//Subset subset = new Subset(new ArrayList<BasicTerm>(), index, this.c, null, null);		// No children in initialization
			for (int i = 0; i < bs.length(); i++) {
				if (bs.get(i)) {
					subsetTerms.add(listOfBasicTerms.get(i));
				}
			}
			index++;
			Subset subset = new Subset(subsetTerms, index-1, this.c, null, null);		// No children in initialization
			output.add(subset);
		}

		return output;
	}

	// Determine if the current subset is a leaf
	public boolean noChildren (Subset s) {
		return (s.left == null && s.right == null);
	}


	// If branching-and, left node must be an &

	// Need to print the code recursively (binary tree)
	public String getCode(Subset optimal) {
		String sb = "if" + optimal + "{ \n";

		// Check rightmost to see if no-branching
		Subset rightMost = optimal.rightMost;
		if (rightMost.nobranch && rightMost.right == null) {
			sb += "\tanswer[j] = i; \n ";
			sb += "\tj += " + rightMost + "; \n }";
		} else {
			sb += "\tanswer[j++] = i; \n }";
		}

		return sb;

	}

}
