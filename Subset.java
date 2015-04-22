import java.util.ArrayList;
import java.Math.*;

// The Logical-and term  / subset (&-term) is a set of basic terms
// Implements functions to compute the C-metric and D-metric
// Implements functions to compute the cost of No-Branching and Logical-And Algorithms

public class Subset {

	private ArrayList<BasicTerm> terms;
	public int index; 			// Represents where in the array of keys that this subset is stored
	public double p; 			// Product of selectivies of basic terms
	public boolean nobranch; 	// True: No-branch optimization was used to get best cost.
	public double cost; 		// Best cost of plan
	public Subset left;			// Left child
	public Subset leftMost;		// Leftmost child
	public Subset rightMost;	// Rightmost child
	public Subset right;		// Right child
	public Metric cMetric;		// C-Metric
	public Metric dMetric;		// D-Metric
	public Metric highestDMetric;	// highest D-Metric

	// The complete constructor
	public Subset(ArrayList<BasicTerm> terms, int index, Costs c, Subset left, Subset right) {
		this.terms = terms;
		this.index = index;
		this.p = this.computePs();
		this.nobranch = false;						// By default
		this.cost = this.logicalAndCost(c);			// Default cost is Logical-And cost
		double nbc = this.noBranchCost(c);
		if (nbc < this.cost) {
			this.nobranch = true;
			this.cost = nbc;
		}
		this.left = left;
		this.right = right;
		this.leftMost = (left == null)? this : left.leftMost;
		this.rightMost = (right == null)? this : right.rightMost;
		this.cMetric = this.computeCMetric();
		this.dMetric = this.computeDMetric();
		this.highestDMetric = this.dMetric;
	}

	// Add term to list of basic terms
	public void add(BasicTerm term) {
		this.terms.add(term);
	}

	// Number of basic Terms
	public int size(){
	    return this.terms.size();
	}

	// Return list of basic terms
	public ArrayList<BasicTerm> getTerms() {
		return this.terms;
	}

	// Index in A
	public int getIndex() {
		return this.index;
	}

	// The cost of Algorithm No-Branch

	public double NoBranchCost(Costs c) {

		int k = terms.size();
		int r = c.r;
		int l = c.l;
		int t = c.t;
		int m = c.m;
		int a = c.a;
		int f = c.f;

		return k*r + (k-1)*l + f*k + a;		// Example 4.4
	}

	// The cost of Algorithm Logical-And

	// kr + (k-1)l + (f1 + ... + fk) + t + mq + (p1...pk)*a
	// k = # of Basic terms
	// r = Cost of accessing array element rj[i]
	// l = Cost of performing logical and
	// t = Cost of performing if test
	// m = Cost of branch misprediction
	// (f1 + ... + fk) = Sum of costs of applying all functions: Assume this is k*f since each function has equal cost
	// q = (p1...pk) if (p1...pk) <= 0.5; otherwise q = 1 - (p1...pk)
	// a = Cost of writing an answer to the answer array and incrementing the answer array counter

	public double LogicalAndCost(Costs c) {

		int k = terms.size();
		int r = c.r;
		int l = c.l;
		int t = c.t;
		int m = c.m;
		int a = c.a;
		int f = c.f;

		// Compute q
		double ps = computePs();
		double q = Math.min(ps, 1- ps);

		return computeFCost(c) + m*q + ps*a;
	}

	// Get the product of the selectivities of the basic terms

	public double computePs() {
		if (this.terms.size() == 0)
			return 0;
		double ps = 1.0;
		for (BasicTerm term: this.terms) {
			ps *= term.p;
		}
		return ps;
	}

	// Get F cost of this &-term according to Definition 4.7

    public double computeFCost(Costs c) {
		int k = terms.size();
		int r = c.r;
		int l = c.l;
		int t = c.t;
		int m = c.m;
		int a = c.a;
		int f = c.f;

		return k*r + (k-1)*l + k*f + t;
	}

	// Get the C-Metric of this & term according to Definition 4.10
	// C-metric = ((p-1)/fcost(E), p)

	public Metric computeCMetric(Costs c) {
		double fCost = computeFCost(c);
		double ps = computePs();
		double a = (ps - 1) / fCost;
		double b = ps;
		return new Metric(a, b);
	}

	// Get the D-Metric of this & term according to Definition 4.10
	// D-metric = ((fcost(E), p)

	public Metric computeDMetric(Costs c) {
		double fCost = computeFCost(c);
		double ps = computePs();
		return new Metric(fCost, ps);
	}

	// Output this expression of basic terms as a string

	public String toString() {
		String buffer = "";
		//branching and
		if (p.left != null){
			if (p.right.noBranch){
				buffer += "(" + p.left.toString() + ")";
			}
			else{
				buffer += "(" + p.left.toString() + ") && (" + p.right.toString() + ")";
			}
		}
		//logical and
		else {
			buffer += "(" + this.terms.get(1);
			for (int i = 2; i < this.terms.size(); i++){
				buffer += " & " + this.terms.get(i);
			}
			buffer += ")";
		}
		return buffer;
	}

	// Output the selectivities of the basic terms as a string

	public String printPs() {
		StringBuffer sb = new StringBuffer();

		for(BasicTerm term : this.terms) {
			sb.append(term.p.toString());
			sb.append(" ");
		}

		return sb.toString();
	}

	/**
	  * Simpler implementation of whether two plans intersect.
	  * @param p2 other AndTerm set
	  * @return true if the intersection is nonempty, false otherwise
	  */
    public boolean intersection(Subset s2) {
        return (this.index & s2.index) != 0;
    }

	// Index of union of two plans in the array
    public int union (Subset s2) {
		return (this.index | s2.index);
    }

	// Update pointers to children, costs, and metrics (Algorithm 4.111)
    public void newBranchingAnd(newCost, newLeft, newRight) {
		this.cost = newCost;
		this.left = newLeft;
		this.right = newRight;

		this.leftMost = left.leftMost;
		this.rightMost = right.rightMost;

		double childHighestDMetric = Math.max(left.highestDMetric, left.highestDMetric);
		this.highestDMetric = Math.max(this.highestDMetric, childHighestDMetric);

	}

}
