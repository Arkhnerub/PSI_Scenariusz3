import java.util.Random;

public class perc1 {
	Random random = new Random();
	private double sum = 0;
	private double variable;
	private double weight;
	
	public perc1() {
		weight = random.nextDouble()*2-1;
	}
	
	public void setVariable(double w1) {
		this.variable=w1;
	}
	
	public double sum() {
		sum= variable * weight;
		return sum;
	}
	
	public void updateWeight(double weight) {
		this.weight = weight;
	}
	
	public double getWeight() {
		return weight;
	}
	public double getSuma() {
		return sum;
	}
}
