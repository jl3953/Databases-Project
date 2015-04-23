// Defines a basic term

public class BasicTerm {
	public String function;
	public String argument;
	public double p;			// Selectivity

	public BasicTerm(String function, String argument, double p) {
		this.function = function;
		this.argument = argument;
		this.p = p;
	}

	//print function(argument)
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(function);
		sb.append("[" + argument + "]");
		return sb.toString();
	}

/*	// Compare two basic terms - Used in Algorithm 4.11
	public boolean equals(BasicTerm other) {
		return this.function.equals(other.function) &&
			   this.argument.equals(other.argument);
	}
*/
}
