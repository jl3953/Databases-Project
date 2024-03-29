import java.util.ArrayList;

public class Plan {

	public int n; 				// Number of basic terms
	public double p; 			// Product of selectivies of basic terms
	public boolean nobranch; 	// True: No-branch optimization was used to get best cost.
	public double cost; 		// Best cost of plan
	//public Plan left; 		// Left subplan
	//public Plan right;		// Right subplan
	//public int leftchild;		// Index of left child subplan that gives best cost
	//public int rightchild;	// Index of right child subplan that gives best cost
	//public AndTerm subset;	// subset (&-term) with list of basic terms
	//public long bitmap;		// the bit array representing an index

	private ArrayList<BasicTerm> terms;

	public AndTerm left;		// Left index - subset
	public AndTerm right;		// Right index - subset
	public AndTerm subset;		// What's stored here


	/**
	  * Constructor for new plan
	  */
	public Plan(double p,
		boolean nobranch,
		double cost){
	    this.p = p;
	    this.nobranch = nobranch;
	    this.cost = cost;
	    this.left = null;
	    this.right = null;
	    this.subset = null;
	    this.n = 0;
	}

	public Plan(double p,
		boolean nobranch,
		double cost,
		AndTerm subset){
	    this.p = p;
	    this.nobranch = nobranch;
	    this.cost = cost;
	    this.left = null;
	    this.right = null;
	    this.subset = subset;
	    this.n = subset.size();
	}

	/**
	// Create a new Plan
	public Plan(double p,
		boolean nobranch,
		double cost,
		Plan left,
		Plan right,
		int leftchild,
		int rightchild,
		AndTerm andTerm) {

		this.nobranch = nobranch;
		this.cost = c;
		this.left = left;
		this.right = right;
		this.leftchild = leftchild;
		this.rightchild = rightchild;
		this.subset = andTerm;
		this.p = andTerm.computePs();
		this.n = andTerm.terms.size();
	}

	// Get the leftmost &-term - Used in Algorithm 4.11
*/
	public AndTerm getLeftMostAndTerm(HashMap<AndTerm, Plan> A) {
		Plan current = this;
		while (current!= null && current.left != null) {
			current = plans.get(current.left);
		}
		return current.subset;
	}
/*
	public void setBit (long b) {
		this.bitmap = b;
	}

	// Simpler implementation of whether two plans intersect
    public boolean intersection(Plan p2) {
        return (this.bitmap & p2.bitmap) != 0;
    }

	// Index of union of two plans in the array
    public int union(Plan p2) {
		long index = (this.bitmap | p2.bitmap);
        return (int) (index - 1);
    }
    */

}
