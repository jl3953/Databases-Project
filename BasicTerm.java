// Defines a basic term

public class BasicTerm {
	public String function;
	public String argument;
	public double selectivity;

	public BasicTerm(String function, String argument, double selectivity) {
		this.function = function;
		this.argument = argument;
		this.selectivity = selectivity;
	}

	//print function(argument)
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(function);
		sb.append("[" + argument + "]");
		return sb.toString();
	}

	// Compare two basic terms
	public boolean equals(BasicTerm other) {
		return this.function.equals(other.function) &&
			   this.argument.equals(other.argument);
	}

}