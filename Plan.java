import java.util.ArrayList;


public class Plan {

	int n;						// Number of basic terms
	double p;					// Product of selectivies of basic terms
	boolean nobranch;			// True: No-branch optimization was used to get best cost.
	double cost;				// Best cost of plan
	Plan left;					// Left subplan
	Plan right;					// Right subplan
	int leftchild;				// Index of left child subplan that gives best cost
	int rightchild;				// Index of right child subplan that gives best cost
	public AndTerm andTerm;		// &-term with list of basic terms

	// Create a new Plan
	public Plan(double p, boolean nobranch, double cost, Plan left, Plan right, int leftchild, int rightchild, AndTerm andTerm) {

		this.nobranch = nobranch;
		this.cost = c;
		this.left = left;
		this.right = right;
		this.leftchild = leftchild;
		this.rightchild = rightchild;
		this.andTerm = andTerm;
		this.p = andTerm.computePs();
		this.n = andTerm.terms.size();
	}

	// Get the leftmost &-term - Used in Algorithm 4.11

	public AndTerm getLeftMostAndTerm(ArrayList<Plan> plans) {
		Plan current = this;
		while (current.left >= 0) {
			current = plans.get((int)current.left);
		}
		return current.andTerm;
	}
}