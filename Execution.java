import java.util.*;
import java.io.*;

// Run the algorithm!
// Command: java Execution [query.txt] [config file]

public class Execution
{
	public static void main(String[] args) throws Exception
	{

		String query = args[0];
		String config = args[1];

		// Get the list of queries
		ArrayList<double[]> queries = new ArrayList<double[]>();
		BufferedReader br = new BufferedReader(new FileReader(query));
		String line = "";
		while ((line = br.readLine()) != null){
			String[] selectivities = line.split(" ");
			double[] ps = new double[selectivities.length];

			for (int i = 0; i < selectivities.length; i++) {
				ps[i] = Double.parseDouble(selectivities[i]);
			}

			queries.add(ps);
		}

		// Get the cost model
		/*Properties costModel = new Properties();
		costModel.load(new FileInputStream(config));
		Costs c = new Costs(costModel);*/
		Costs c = new Costs();						// Default cost model

		// Generate subsets with basic terms
		int i = 1;
		ArrayList<Subset> subsets = new ArrayList<Subset>();
		for (double[] q : queries) {
			Subset current = new Subset(new ArrayList<BasicTerm>(), -1, c, null, null);
			for (double p: q) {
			    	//System.out.println(p);
				String function = "t" + i;
				String argument = "o" + i + "[i]";
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


			bw.write("=====================================================\n");
			bw.write(optimal.printPs() + "\n");
			bw.write("-----------------------------------------------------\n");
			bw.write(code + "\n");
			bw.write("-----------------------------------------------------\n");
			bw.write("Cost: " + cost + "\n");
		}

		bw.close();
	}
}
