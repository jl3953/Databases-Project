import java.util.ArrayList;

// The Logical-and term  / subset (&-term) is a set of basic terms
// Implements functions to compute the C-metric and D-metric
// Implements functions to compute the cost of No-Branching and Logical-And Algorithms

public class AndTerm {

	//private ArrayList<BasicTerm> terms;
	private TreeSet<BasicTerm> terms;
	private int index;						// Represents where in array A this subset is stored

	public AndTerm(BasicTerm term) {
		this.terms  = new ArrayList<BasicTerm>();
		terms.add(term);
	}

	public AndTerm(ArrayList<BasicTerm> terms) {
		this.terms = terms;
	}

	public AndTerm() {
		this.terms  = new ArrayList<BasicTerm>();
	}

	public void add(BasicTerm term) {
			terms.add(term);
	}

	public TreeSet<BasicTerm> getTerms() {
		return terms;
	}

	public ArrayList<BasicTerm> getArray() {
		return new ArrayList<BasicTerm>(terms);
	}

	// Location in array A of Algorithm 4.11
	public int setIndex(int i) {
		index = i;
	}

	public int getIndex() {
		return index;
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
		if (terms.size() == 0)
			return 0;
		double ps = 1.0;
		for (BasicTerm term: terms) {
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

	public String printTerms() {
		StringBuffer sb = new StringBuffer();
		for (BasicTerm term : terms) {
			sb.append(term.toString());
			sb.append(" & ");
			sb.append("\n");
		}
		return sb.toString();
	}

	// Output the selectivities of the basic terms as a string

	public String printPs() {
		StringBuffer sb = new StringBuffer();

		for(BasicTerm term : terms) {
			sb.append(term.p);
			sb.append(" ");
		}

		return sb.toString();
	}

	// Get number of terms

	public size() {
		return terms.size();
	}

}