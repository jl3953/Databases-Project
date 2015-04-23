import java.util.ArrayList;
import java.util.HashSet;

/**
 * A class to represent a subset of basic terms, as described in the paper.
 *
 * @author Jennifer Lam jl3953
 * @author Richard Chiou rc2758
 */
public class Subset {

    private ArrayList<BasicTerm> terms;	// the subset of basic terms
    public int index;			// Represents where in the array of keys that this subset is stored
    public double p; 			// Product of selectivies of basic terms
    public boolean nobranch; 		// True: No-branch optimization was used to get best cost.
    public double cost; 			// Best cost of plan
    public Subset left;			// Left child
    public Subset leftMost;			// Leftmost child
    public Subset rightMost;		// Rightmost child
    public Subset right;			// Right child
    public Metric cMetric;			// C-Metric
    public Metric dMetric;			// D-Metric
    public HashSet<Metric> DMetrics;	// set of D-Metric
    public Costs c;				// CPU cost model

    /**
     * Constructor for the class.
     */
    public Subset(ArrayList<BasicTerm> terms, 
	    int index, 
	    Costs c, 
	    Subset left, 
	    Subset right) {

	this.terms = terms;
	System.out.println(terms);
	this.index = index;
	this.p = this.computePs();
	this.nobranch = false; // By default
	this.cost = this.LogicalAndCost(c); // Default cost is Logical-And cost
	double nbc = this.NoBranchCost(c);
	if (nbc < this.cost) {
	    this.nobranch = true;
	    this.cost = nbc;
	}
	this.left = left;
	this.right = right;
	this.leftMost = (left == null)? this : left.leftMost;
	this.rightMost = (right == null)? this : right.rightMost;
	this.cMetric = this.computeCMetric(c);
	this.dMetric = this.computeDMetric(c);
	this.DMetrics = new HashSet<Metric>();
	this.DMetrics.add(this.dMetric);
	this.c = c;
    }

    /**
      * Add term to subset of basic terms.
      * @param term term to be added
      */ 
    public void add(BasicTerm term) {
	this.terms.add(term);
	this.p = this.computePs();
	this.cost = this.LogicalAndCost(c); // Default cost is Logical-And cost
	double nbc = this.NoBranchCost(c);
	if (nbc < this.cost) {
	    this.nobranch = true;
	    this.cost = nbc;
	}
    }

    /**
      * Number of basic Terms in the set.
      */
    public int size(){
	return this.terms.size();
    }

    /** 
      * Returns list of basic terms.
      */
    public ArrayList<BasicTerm> getTerms() {
	return this.terms;
    }

    /**
      * Index in overall arraylist where all possible subsets are stored
      */
    public int getIndex() {
	return this.index;
    }

    /**
      * The cost of Algorithm No-Branch.
      * @param c CPU Cost model
      * @return cost of the branch
      */
    public double NoBranchCost(Costs c) {

	int k = terms.size();
	int r = c.r;
	int l = c.l;
	int t = c.t;
	int m = c.m;
	int a = c.a;
	int f = c.f;

	return k*r + (k-1)*l + f*k + a;	// Example 4.4
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

    /**
      * Cost of logical-and.
      * @param c CPU cost model.
      * @return cost of logical-and branch.
      */
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

    /**
      * Get the product of the selectivities of the basic terms
      */
    public double computePs() {
	if (this.terms.size() == 0)
	    return 0;
	double ps = 1.0;
	for (BasicTerm term: this.terms) {
	    ps *= term.p;
	}
	return ps;
    }

    /**
      * Get F cost of this logical-and term according to Definition 4.7
      */
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

    /**
      * Get the C-Metric of this logical-and term according to Definition 4.10
      * C-metric = ((p-1)/fcost(E), p).
      * @param c CPU cost model
      * @return new C metric.
      */
    public Metric computeCMetric(Costs c) {
	double fCost = computeFCost(c);
	double ps = computePs();
	double a = (ps - 1) / fCost;
	double b = ps;
	return new Metric(a, b);
    }

    /**
      * Get the D-Metric of this logical-and term according to Definition 4.10
      * D-metric = ((fcost(E), p).
      * @param c CPU cost model.
      * @return the new D metric.
      */
    public Metric computeDMetric(Costs c) {
	double fCost = computeFCost(c);
	double ps = computePs();
	return new Metric(fCost, ps);
    }

    /**
      * Output this expression of basic terms as a string.
      */
    public String toString() {
	String buffer = "";
	
	//branching and
	if (this.left != null){
	    if (this.right.nobranch && this.right.right == null){ //Needs to be rightmost 
		buffer += "(" + this.left.toString() + ")";
	    }
	    else{
		buffer += "(" + this.left.toString() + ") && (" + this.right.toString() + ")";
	    }
	}
	//logical and
	else {
	    buffer += "(" + this.terms.get(0);
	    for (int i = 1; i < this.terms.size(); i++){
		buffer += " & " + this.terms.get(i);
	    }
	    buffer += ")";
	}
	return buffer;
    }

    /**
      * Output the selectivities of the basic terms as a string
      * @return selectivity as a string.
      */
    public String printPs() {
	String sb = "";
	for (BasicTerm term : this.terms){
	    sb += term.p + " ";
	}
	return sb;
    }

    /**
     * Simpler implementation of whether two plans intersect.
     * @param p2 other AndTerm set
     * @return true if the intersection is nonempty, false otherwise
     */
    public boolean intersection(Subset s2) {
	return (this.index & s2.index) != 0;
    }

    /**
      * Index of the subset resulting from the union of two plans in the array,
      * index referring to the overall arraylist that stores all possible
      * subsets.
      * @param s2 the other subset to be union'ed with.
      * @param the index of the resulting union.
      */
    public int union (Subset s2) {
	return (this.index | s2.index) - 1;
    }

    /**
      * Update pointers to children, costs, and metrics (Algorithm 4.11).
      * @param newCost newly calculated cost.
      * @param newLeft new left child.
      * @param newRight new right child.
      */
    public void newBranchingAnd(double newCost, Subset newLeft, Subset newRight) {
	this.cost = newCost;
	this.left = newLeft;
	this.right = newRight;

	this.leftMost = left.leftMost;
	this.rightMost = right.rightMost;

	this.DMetrics = new HashSet<Metric>();
	this.DMetrics.addAll(left.DMetrics);
	this.DMetrics.addAll(right.DMetrics);

    }

}
