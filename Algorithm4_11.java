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
				Metric DMetric = s.highestDMetric;
				Metric CMetricPrime = sprime.cMetric;
				Metric DMetricPrime = sprime.dMetric;

				//C-metric test: lemma 4.8
				if (CMetric.a < CMetricprime.a && CMetric.b <= CMetricprime.b)
					continue;

				//D-metric test: lemma 4.9
				if (sprime.p <= 0.5) {
					if (DMetric.a < DMetricprime.a && DMetric.b < DMetricprime.b)
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

				//if plan is optimal
				if (combinedCost < currentCost){
					u.newBranchingAnd(newCost, newLeft, newRight);
				}
			}
		}

		// Return the biggest subset with the optimal plan
		return A.get(a.size()-1);

    }

   /**
     * Initializes the Subsets.
     * @return an ArrayList of subsets indexed by bitMap
     */
    private ArrayList<Subset> initialize(){

		ArrayList<Subset> output = new ArrayList<Subset>();
		int numberOfTerm = this.bigSet.size(); 				// k
		int possibleSubsets = Math.pow(2, numberOfTerm); 	// 2^k, maximum number of subsets

		ind index = 0;
		ArrayList<BasicTerm> listOfBasicTerms = this.bigSet.getTerms();

		while (index < possibleSubsets) {
			BitSet bs = BitSet.valueOf(new long[]{index});
			Subset subset = new Subset(new ArrayList<BasicTerm>(), index, this.c, null, null);		// No children in initialization
			for (int i = 0; i < bs.length(); i++) {
				if (bs.get(i)) {
					subset.add(listOfBasicTerms.get(i));
				}
			}
			index++;
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

		// end this recursion branch if there are no children
		//if(noChildren(optimal)) {
			/*ArrayList<BasicTerm> terms = optimal.getTerms();
			int numberOfTerms = terms.size();
			for(BasicTerm term : terms) {
				sb.append(term);
				if (numberOfTerms > 1) {
					sb.append(" & ");
				}
				numberOfTerms--;
			}
			sb.append(optimal.toString());
			if (optimal.nobranch) {
				sb.insert(0, "answer[j] = i; \n j += (");
				sb.append(");");
			}
			return sb.toString();
		}*/

		// Check rightmost to see if no-branching
		Subset rightMost = optimal.rightMost;
		if (!rightMost.nobranch) {
			sb += "\tanswer[j++] = i; \n }";
		} else {
			sb += "\tanswer[j] = i; \n ";
			sb += "\j += " + rightMost + "; \n }"
		}

	}

}
