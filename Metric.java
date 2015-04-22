// Pair of numbers
// Can be used as C- or D- metric

public class Metric {
	public double a;
	public double b;

	public Metric() {
		this.a = 0;
		this.b = 0;
	}
	public Metric(double a, double b) {
		this.a = b;
		this.b = b;
	}

	public double getA() {
		return a;
	}

	public void changeA(double a) {
		this.a = a;
	}

	public double getB() {
		return b;
	}

	public void changeY(double b) {
		this.b = b;
	}
}