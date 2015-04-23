// Defines a basic term
/**
  * Representation of a basic term, as described in the paper.
  *
  * @author Richard Chiou rc2758
  * @author Jennifer Lam jl3953
  */
public class BasicTerm {

	public String function;	//the outer function -- t1, t2, etc
	public String argument; //the function's parameter -- o1, o2, etc
	public double p;	// Selectivity

	/**
	  * Constructor for BasicTerm.
	  */
	public BasicTerm(String function, String argument, double p) {
		this.function = function;
		this.argument = argument;
		this.p = p;
	}

	/**
	  * Prints the basic term in correct format with brackets.
	  * @return the string representing a basic term.
	  */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(function);
		sb.append("[" + argument + "]");
		return sb.toString();
	}
}
