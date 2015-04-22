import java.util.ArrayList;

// ArrayList of Basic Terms
// Implements functions to compute the C-metric and D-metric
// Implements functions to compute the cost of No-Branching and Logical-And Algorithms

public class BasicTerms {

	private ArrayList<BasicTerm> terms;

	public BasicTerms(BasicTerm term) {
		this.terms  = new ArrayList<BasicTerm>();
		terms.add(term);
	}

	public BasicTerms(ArrayList<BasicTerm> terms) {
		this.terms = terms;
	}

	public BasicTerms() {
		this.terms  = new ArrayList<BasicTerm>();
	}

	public void add(BasicTerm term) {
		data.add(term);
	}

	public BasicTerm get(int i) {
		return term.get(i);
	}

	public ArrayList<BasicTerm> getTerms() {
		return terms;
	}

	// The cost of Algorithm No-Branch

	public double NoBranchCost(Costs c) {
		if (terms.size() < 1)
			throw new IllegalArgumentException("No basic terms in plan");
		if (c == null)
			throw new IllegalArgumentException("Costs is null");

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

		// Compute Q
		double ps = computePs();
		double q = ps <= 0.5 ? ps: 1 - selectivities;

		return computeFCost(c) + m*q + ps*a;
	}

	// Get the product of the selectivities of the basic terms

	public double computePs() {
		if (terms.size() == 0)
			return 0;
		double ps = 1.0;
		for (int i = 0; i < terms.size(); ++i) {
			ps *= terms.get(i).p;
		}
		return ps;
	}

	// Get F cost of this & term according to Definition 4.7

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

	public Metric getDMetric(Costs c) {
		double fCost = computeFCost(c);
		double ps = computePs();
		return new Metric(fCost, ps);
	}

	// Output this expression of basic terms as a string

	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (BasicTerm t : terms) {
			sb.append(t.toString());
			sb.append(" & ");
			sb.append("\n");
		}
		return sb.toString();
	}

}