import java.util.Properties;

// Costs from Configuration File - Default is used unless otherwise specified

public class Costs {

	public int r = 1;				// Cost to access element of array r
	public int t = 2;				// Cost of performing if test
	public int l = 1;				// Cost of performing logical and test
	public int m = 16;				// Cost of branch mis-prediction
	public int a = 2;				// Cost of writing answer
	public int f = 4;				// Cost of applying function f

	public Costs() {
		// Default costs: nothing needs to change
	}

	// Take advantage of Properties
	public Costs(Properties prop) {
		int r = Integer.parseInt(prop.getProperty("r"));
		int t = Integer.parseInt(prop.getProperty("t"));
		int l = Integer.parseInt(prop.getProperty("l"));
		int m = Integer.parseInt(prop.getProperty("m"));
		int a = Integer.parseInt(prop.getProperty("a"));
		int f = Integer.parseInt(prop.getProperty("f"));

		this.r = r;
		this.t = t;
		this.l = l;
		this.m = m;
		this.a = a;
		this.f = f;

	}
}