import java.util.*;
import java.io.*;

// Run the algorithm!
// Command: java Execution [query.txt] [config file]

public class Execution
{
	public static void main(String[] args)
	{

		String query = args[0];
		String config = args[1];

		// Get the list of queries
		ArrayList<double[]> queries = new ArrayList<double[]>();
		Scanner sc = new Scanner(new File(queryPath));
		while(sc.hasNextLine()) {
			String line = sc.nextLine();
			String[] selectivities = line.split(" ");
			double[] ps = new double[selectivities.length];

			for (int i = 0; i < selectivities.length; i++)
				ps[i] = Double.parseDouble(selectivities[i]);

			query.add(ps);
		}

		// Get the cost model
		/*Properties costModel = new Properties();
		costModel.load(new FileInputStream(config));
		Costs c = new Costs(costModel);*/
		Costs c = new Costs();						// Default cost model

		// Generate &-terms with basic terms
		int i = 1;
		ArrayList<AndTerm> andTerms = new ArrayList<AndTerm>();
		for (double[] query: queries) {
			AndTerm current	= new AndTerm();
			for (double p: query) {
				String function = "t" + new Integer(i).toString;
				String argument = "o" + new Integer(i).toString + "[i]";
				BasicTerm term = new BasicTerm(function, argument, p);
				current.add(term);
				i++;
			}
			andTerms.add(current);
			i = 1;
		}

		// Run Algorithm 4.11 and print the code for the optimal plans
		for (andTerm terms: andTerms) {
			Algorithm alg = new Algorithm(terms, c);
			Plan optimal = alg.runAlgorithm();
			String code = alg.getCode(optimal);
			double cost = optimal.cost;

			System.out.println("=====================================================");
			System.out.println(terms.printPs());
			System.out.println("-----------------------------------------------------");
			System.out.println(code);
			System.out.println("-----------------------------------------------------");
			System.out.println("Cost: " + cost);
		}
	}
}
