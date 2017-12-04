import java.util.Random;

public class perc2 {
	Random rand = new Random();
	private double suma;
	double[] wector;
	double[] weights;
	
	public perc2(int ilosc) {
		weights=new double[ilosc];
		for(int i=0;i<ilosc;i++) {
			weights[i]=rand.nextDouble()*2-1;
		}
	}
	
	public void setVector(double[] vector) {
		this.wector=vector;
	}
	public double sum() {
		suma=0;
		for(int i=0;i<weights.length;i++) {
			suma+=wector[i]*weights[i];
		}
		return suma;
	}
	public double getSuma() {
		return suma;
	}
	public double[] getWeights() {
		return weights;
	}
	public void setWeight(int index, double weight) {
		this.weights[index]=weight;
	}
}
