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

		// Generate subsets with basic terms
		int i = 1;
		ArrayList<Subset> subsets = new ArrayList<Subset>();
		for (double[] query: queries) {
			Subset current = new Subset(new ArrayList<BasicTerm>(), -1, c, null, null);
			for (double p: query) {
				String function = "t" + new Integer(i).toString;
				String argument = "o" + new Integer(i).toString + "[i]";
				current.add(new BasicTerm(function, argument, p));
				i++;
			}
			subsets.add(current);
			i = 1;
		}

		// Run Algorithm 4.11 and print the code for the optimal plans

		File file = new File ("output.txt");
		if (!file.exists())
			file.createNewFile();

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);


		for (Subset subset: subsets) {
			Algorithm4_11 alg = new Algorithm4_11(subset, c);
			Subset optimal = alg.runAlgorithm();
			String code = alg.getCode(optimal);
			double cost = optimal.cost;


			bw.write("=====================================================");
			bw.write(terms.printPs());
			bw.write("-----------------------------------------------------");
			bw.write(code);
			bw.write("-----------------------------------------------------");
			bw.write("Cost: " + cost);
		}

		bw.close();
	}
}
