import java.util.*;

public class Algorithm {

    private LinkedHashMap<AndTerm, Plan> A; 	//array indexed by subset
    private AndTerm terms; 				//the set of basic terms given to us for testing
    private Cost c; 					//given configurations

    public Algorithm(AndTerm terms, Cost c){
		this.terms = terms;
		this.c = c;
		this.A = this.initialize();
    }

    /**
      * Dynamic Programming algorithm that computes the least cost plan
      */

    public void runAlgorithm (){

		//Iterate over all subsets
		for (AndTerm s: this.A.keySet()){ //s
			Plan s = A.get(s);
			AndTerm leftmost = s.getLeftMostAndTerm(A);
			for (AndTerm sprime: this.A.keySet()){ //sprime

				//check for intersection and skip this plan if there is intersection
				if (sprime.intersection(s))
					continue;

				//Otherwise, compute c and d metrics for both plans
				Plan t = A.get(sprime);

				Metric CMetric = leftmost.computeCMetric(this.c);
				Metric DMetric = leftmost.computeDMetric(this.c);
				Metric CMetricPrime = sprime.computeCMetric(this.c);
				Metric DMetricPrime = sprime.computeDMetric(this.c);

				//C-metric test: lemma 4.8
				if (CMetric.a < CMetricprime.a && CMetric.b <= CMetricprime.b)
					continue;

				//D-metric test: lemma 4.9
				//Incomplete dmetric test but will work for current purposes
				//Only compares sprime with the left-most term
				if (t.p <= 0.5) {
					if (DMetric.a < DMetricprime.a && DMetric.b <= DMetricprime.b)
						continue;
				}
				/*	boolean passDTest = true;
					ArrayList<AndTerm> all = getAndTerms(s);		// need to compare against all &-terms of S
					for (AndTerm term: all) {
						Metric DMetric = term.computeDMetric(c);
						if (DMetric.x  < DMetrict.x && DMetric.y < DMetrict.y) {
							passDTest = false;
							break;
						}
					}
					if (!passDTest)
						continue;*/
				}

				//Find the optimal plan

				//Union of s and sprime
				AndTerm key = this.A.keyset().toArray[sprime.union(s)];
				Plan currentPlan = this.A.get(key);
				double currentCost = currentPlan.cost;

				//double p = s.p * t.p;		// the combined selectivity
				//int leftchild = t.subset.getIndex();
				//int rightchild = s.subset.getIndex();

				double FCost = t.subset.computeFCost(c);
				double ps = t.p;
				double q = Math.min(ps, 1 - ps);
				double combinedCost = FCost + c.m*q + ps * s.cost;

				//if plan is optimal
				if (combinedCost < currentCost){
					currentPlan.cost = combinedCost;
					currentPlan.left = sprime;
					currentPlan.right = s;
				}
			}
		}
    }

   /**
     * Initializes the HashMap.
     * @return a hashmap of plans indexed by andTerms
     */
    private HashMap<AndTerm, Plan> initialize(){
		HashMap<AndTerm, Plan> result = new HashMap<AndTerm, Plan>();

		ArrayList<AndTerm> keys = this.getSubsets(this.terms);
		ArrayList<Plan> values = this.getPlans(keys);

		//match up the keys and values in the hashmap
		Iterator it = values.iterator();
		for (AndTerm key : keys){
			it.hasNext();
			Plan value = it.next();
			result.put(key, value);
		}

		return result;
    }

    /**
     * Generates all possible subsets, given a set of basic terms.
     * @param terms set of basic terms
     * @return an arraylist of all possible subsets
     */
    public ArrayList<AndTerm> getSubsets(AndTerm terms) {

		int size = terms.size(); // k
		long max = Math.pow(2, size); // 2^k, maximum number of subsets
		int index = max - 1; // start from 2^k - 1

		//generating all possible subsets
		ArrayList<AndTerm> sets = new ArrayList<AndTerm>();
		termArray = terms.getArray(); 		//ArrayList of basic terms

		while (index > 0) {  // skip 0 - the empty set
			BitSet bs = BitSet.valueOf(new long[]{index});
			AndTerm subset = new AndTerm();
			//iterate through the bit vector to determine which terms to add
			for (int i = 0; i < bs.length(); i++) {
				if (bs.get(i)) {
					subset.add(termArray.get(i));
				}
			}
			subset.setBit ((long) index);
			index--;
			subset.setIndex(index);
			sets.add(0, subset); // add to front
		}
		return sets;
    }

    /**
      * Retrieves the cost of all plans according to the terms in the subset.
      * @param A an arraylist of all possible subsets
      * @return an arraylist of all initial costs computed
      */
    public ArrayList<Plan> getPlans(ArrayList<AndTerm> A){

	ArrayList<Plan> result = new ArrayList<Plan>();

		for (AndTerm subset: A) {
			int n = subset.size(); 							// Number of basic terms
			double p = subset.computePs();  				// Product of selectivities of basic terms
			double cost = subset.LogicalAndCost(this.c);
			double nobranch = subset.NoBranchCost(this.c);
			boolean b = false; 								//not using nobranchcost yet

			//if no-branch has lower cost, replace logical and
			if (nobranch < cost) {
				cost = nobranch;
				boolean b = true;
			}

			Plan plan = new Plan(p, b, cost, subset);    // Right now the children don't exist
			result.add(plan);

		}

		return result;
    }
}
