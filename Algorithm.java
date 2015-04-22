import java.util.*;
import java.Math.*;

public class Algorithm {

    //TODO MySet, CostObject
    //private HashMap<AndTerm, double> A; 		//key: subset, value: cost

    private ArrayList<AndTerm> A;
 	private ArrayList<Plan> plans;
	private AndTerm terms;
	private Costs c;

    public Algorithm(AndTerm terms, Costs c){
		//this.A = new HashMap<MySet, CostObject>();
		this.terms = terms;
		this.A = new ArrayList<AndTerm>();
		this.plans = new ArrayList<Plan>();
		this.c = c;
    }

	// Implementation of Algorithm 4.11

	public Plan runAlgorithm() {

		NoBranchingAndInitialize(); 		// Initialize array A - subsets and plans

		for (Plan s: plans) {
			sTerms = s.getTerms();

			for (Plan t: plans) {
				tTerms = t.getTerms();
				boolean intersect = false;

				// First determine the intersection is null
				TreeSet<BasicTerm> intersection = new TreeSet<BasicTerm>();
				for (term: sTerms)
					intersection.add(term);
				for (term: tTerms) {
					if (intersection.contains(term)) {
						intersect = true;
						continue;
					}
					intersection.add(term);
				}
				if (intersect)					// Skip if subsets are not disjoint
					continue;

				AndTerm leftmost = s.getLeftMostAndTerm(plans);		//Leftmost &-term in S
				Metric CMetrics = leftMost.computeCMetric(c);
				Metric CMetrict = t.subset.computeCMetric(c)
				Metric DMetrics = s.subset.computeDMetric(c);
				Metric DMetrict = t.subset.computeDMetric(c);

				// C-metric test (Lemma 4.8)
				if (CMetrics.x < CMetrict.x && CMetrics.y <= CMetrict.y)
					continue;

				// D-metric test (Lemma 4.9)
				if (t.p <= 0.5) {
					boolean passDTest = true;
					ArrayList<AndTerm> all = getAndTerms(s);		// need to compare against all &-terms of S
					for (AndTerm term: all) {
						Metric DMetric = term.computeDMetric(c);
						if (DMetric.x  < DMetrict.x && DMetric.y < DMetrict.y) {
							passDTest = false;
							break;
						}
					}
					if (!passDTest)
						continue;
				}

				// If both tests pass, compare the cost c for the combined plan (s && t) using Eq. (1)
				// with the current cost
				AndTerm combined = new AndTerm();
				for(BasicTerm term : s.subset.getTerms())
					combined.add(term);
				for(BasicTerm term : t.subset.getTerms())
					combined.add(term);

				int index = 0;
				for (Plan p: plans) {
					AndTerm current = new AndTerm(p.subset);
					if (combined.size() == current.size()) {
						boolean match = true;
						for (BasicTerm term: combined.getTerms()) {
							if (!contains(curr.getTerms(), t)) {
								match = false;
								break;
							}
						}
						if (match) {
							break;
						}
					}
					index++;
				}
				Plan currentPlan = plans.get(index);
				double currentCost = currentPlan.cost;

				double p = s.p * t.p;		// the combined selectivity
				int leftchild = t.subset.getIndex();
				int rightchild = s.subset.getIndex();
				double combinedCost = getCombinedCost(new Plan(p, false, 0, null, null, leftchild, rightchild, combined));

				if (combinedCost < currentCost) {
						currentPlan.cost = combinedCost;
						currentPlan.left = t.subset.getIndex();
						currentPlan.right = s.subset.getIndex();
				}
			}
		}

		// finally return the result of the dynamic programming algorithm - the plan with all k basic terms in its subset
		return plans.get(plans.size()-1);
	}

	// Create an array A of 2^k-1 possible nonempty plans with their costs and basic terms.

    public void NoBranchingAndInitialize(){

	 	this.A = getSubsets(terms);			// Initialize the array of subsets

	 	for (AndTerm subset: A) {
			Plan plan = new Plan();
			int n = subset.size();			// Number of basic terms
			double p = subset.computePs();	// Product of selectivities of basic terms
			double cost = subset.LogicalAndCost(c);
			double nobranch = subset.NoBranchCost(c);

			if (nobranch < cost) {			// If No-Branch has lower cost, replace Logical-And
				cost = nobranch;
				boolean b = True;
			}

			plans.add(new Plan(p, b, cost, null, null, -1, -1, subset)); 	// Right now the children don't exist
		}
	}

    // Returns an arrayList of 2^k-1 possible non-empty subsets with indices assigned

	public void getSubsets(AndTerm terms) {
		int size = terms.size();				// k
		long max = Math.pow(2, size);			// 2^k
		int index = max - 1;					// start from 2^k - 1
		ArrayList<AndTerm> sets = new ArrayList<AndTerm>();
		termArray = terms.getArray()
		while (index > 0) {						// skip 0 - the empty set
			BitSet bs = BitSet.valueOf(new long[]{index});
			AndTerm subset = new AndTerm();
			for (int i = 0; i < bs.length(); i++) {
				if (bs.get(i)) {
					subset.add(termArray.get(i));
				}
			}
			index--;
			subset.setIndex(index - 1);
			sets.add(0, subset);				// add to front
		}
		A = sets;
	}

	// Get all &-terms in a plan recursively
	public ArrayList<AndTerm> getAndTerms(Plan p) {
		ArrayList<AndTerm> answer = new ArrayList<LogicalAndTerm>();
		getAndTermsHelper(p, answer);
		return answer;
	}

	// Helper method for above method
	private void getAndTermsHelper(Plan p, ArrayList<AndTerm> answer) {
		if (p.leftchild < 0 && p.rightchild < 0) {		// No children left
			answer.add(p.subset);
			return;
		}
		if (p.leftchild >= 0) {
			Plan left = plans.get((int) p.leftchild);
			getAndTermsHelper(left, answer);
		}
		if (p.right >= 0) {
			Plan right = plans.get((int) p.rightchild);
			getAndTermsHelper(right, answer);
		}
	}

	// Recursive method for getting the cost of a combined plan
	public double getCombinedCost(Plan p) {

		if (p == null)
			return 0;

		// p is just a Logical-And Plan with no more children - lowest level of tree
		if (noChildren(p)) {
			if (p.nobranch)			// Need to implement a function that makes sure this is the last &-term (Section 4.1)
				return p.subset.NoBranchCost(c);
			return p.subset.LogicalAndCost(c);
		}

		// Otherise p must be a mixed plan, with its left child an &-term (Algorithm 4.11)

		Plan leftchild = plans.get((int) p.left)
		Plan rightchild = plans.get((int) p.right);
		double FCost = leftchild.subset.computeFCost(cm);
		double ps = leftchild.p;
		double q = Math.min(ps, 1 - ps);
		return FCost + c.m*q + ps * getCombinedCost(rightchild);	// Equation 1

	}

	// Determine if the current plan is a leaf of the tree (logical-and)
	public boolean noChildren (Plan p) {
		return p.left < 0 && p.right < 0;
	}

	// Need to print the code recursively (binary tree)
	public String getCode(Plan optimal) {
		StringBuffer sb = new StringBuffer();

		// end this recursion branch if there are no children
		if(noChildren(optimal)) {
			TreeSet<BasicTerm> terms = optimal.subset.getTerms();
			int size = terms.size();
			for(BasicTerm term : terms) {
				sb.append(t);
				if (size > 1) {
					sb.append(" & ");
				}
				size--;
			}
			if (optimal.nobranch) {
				sb.insert(0, "answer[j] = i; \n j += (");
				sb.append(");");
			}
			return sb.toString();
		}

		Plan left = plans.get((int) optimal.left);
		Plan right = plans.get((int) optimal.right);

		sb.append("if(");
		sb.append(printCode(left) + " && " + printCode(right));
		sb.append(") {\n");

		// check if last &-term works
		// otherwise, add answer[j++] = i;
		Plan rightmost = rightmostAnd(optimal, plans);

		if(rightmost.nobranch) {
			TreeSet<BasicTerm> terms = optimal.subset.getTerms();
			int size = terms.size();
			for(BasicTerm term : terms) {
				sb.append(t);
				if (size > 1) {
					sb.append(" & ");
				}
				size--;
			}
			sb.insert(0, "\tanswer[j] = i; \n j += (");
			sb.append(");");
			return sb.toString();
		}
		else {
			sb.append("\tanswer[j++] = i;");
		}

		sb.append("\n}");

		return sb.toString();
	}
}
