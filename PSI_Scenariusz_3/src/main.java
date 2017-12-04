import java.util.Random;

public class main {
	private static int liczba = 20;
	private static double e = 2.718281828459045235;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		/////////////////////////////////////////////////
		////////////Inicjacja wszystkich zmiennych///////
		/////////////////////////////////////////////////
		Random rand = new Random();
		double[][] all = new double[liczba][liczba];
		double[][] answers = new double[20][5];
		double[] firstLayerAnswers = new double[liczba];
		double[] secondLayerAnswers = new double[5];
		double[] thirdLayerAnswers = new double[liczba];
		double[] macierzBledowW3;
		double wspolczynnikUczenia = 0.1;
		createWectors(all);
		setAnswerVectors(answers);
		double sumaBledowW3=0;
		int liczbaIteracji = 0;
		perc1[] firstLayer = new perc1[liczba];
		perc2[] secondLayer = new perc2[5];
		perc2[] thirdLayer = new perc2[liczba];
		java.text.DecimalFormat df = new java.text.DecimalFormat();
		df.setMaximumFractionDigits(2);
		df.setMinimumFractionDigits(2);
		
		
		/////////////////////////////////////////////////
		////////////Stworzenie warstw////////////////////
		/////////////////////////////////////////////////
		for(int i=0;i<20;i++) {
			firstLayer[i] = new perc1();
		}
		for(int i=0;i<5;i++) {
			secondLayer[i]=new perc2(20);
		}
		for(int i=0;i<20;i++) {
			thirdLayer[i] = new perc2(5);
		}
		
		
		//Uczenie:
		do {
			/////////////////////////////////////////////////
			////////////Ustawiam wektor do nauki/////////////
			/////////////////////////////////////////////////
			int which = setVectorsFirstLayer(firstLayer,all,rand);
			for(int i=0;i<20;i++) {
				firstLayerAnswers[i]=1/(1+Math.pow(e, -firstLayer[i].sum()));
			}
			setVectorsSecondLayer(firstLayerAnswers, secondLayer);
			for(int i=0;i<5;i++) {
				secondLayerAnswers[i]=1/(1+Math.pow(e, -secondLayer[i].sum()));
			}
			setVectorsThirdLayer(secondLayerAnswers, thirdLayer);
			for(int i=0;i<20;i++) {
				thirdLayerAnswers[i]=1/(1+Math.pow(e, -thirdLayer[i].sum()));
			}
			
			/////////////////////////////////////////////////
			////////////Obliczam bledy///////////////////////
			/////////////////////////////////////////////////			
			
			double[] macierzBledowW1= new double[20];
			double[] macierzBledowW2 = new double[5];
			macierzBledowW3 = new double[20];
			
			for(int i=0;i<20;i++) {
				macierzBledowW3[i]=answers[which][i]-thirdLayerAnswers[i];
			}
			
			for(int j=0;j<20;j++) {
				for(int i=0;i<5;i++) {
					macierzBledowW2[i]=+macierzBledowW3[j]*thirdLayer[j].getWeights()[i];
				}
			}
			for(int i=0;i<5;i++) {
				for(int j=0;j<20;j++) {
					macierzBledowW1[j]+=macierzBledowW2[i]*secondLayer[i].getWeights()[j];
				}
			}
			
			/////////////////////////////////////////////////
			//////////////////////Ucze///////////////////////
			/////////////////////////////////////////////////
			
			
			for(int i=0;i<20;i++) {
				double newWeight = firstLayer[i].getWeight() + wspolczynnikUczenia*macierzBledowW1[i]*all[which][i]
				*(Math.pow(e, firstLayer[i].getSuma())/Math.pow((Math.pow(e, firstLayer[i].getSuma())+1), 2));
				firstLayer[i].updateWeight(newWeight);
			}
			for(int i=0;i<5;i++) {
				for(int j=0;j<20;j++) {
					secondLayer[i].getWeights()[j]=secondLayer[i].getWeights()[j]+wspolczynnikUczenia*macierzBledowW2[i]
							*(Math.pow(e, secondLayer[i].getSuma())/Math.pow((Math.pow(e, secondLayer[i].getSuma())+1), 2))
							*firstLayerAnswers[j];
				}
			}
			for(int i=0;i<20;i++) {
				for(int j=0;j<5;j++) {
					double newWeight = thirdLayer[i].getWeights()[j] + wspolczynnikUczenia*macierzBledowW3[i]*secondLayerAnswers[j]
							*(Math.pow(e, thirdLayer[i].getSuma())/Math.pow((Math.pow(e, thirdLayer[i].getSuma())+1), 2));
					thirdLayer[i].setWeight(j, newWeight);
				}
			}
			
			sumaBledowW3=0;
			for(int i=0;i<20;i++) {
				sumaBledowW3+=macierzBledowW3[i];
				
			}
			liczbaIteracji++;
		}while(sumaBledowW3>0.00001 || sumaBledowW3<-0.00001);
		
		System.out.println("Zakonczono proces uczenia po: "+liczbaIteracji+" iteracjach");
		
		//Testowanie
		for(int z=0;z<20;z++) {
			int which = setVectorsFirstLayer(firstLayer,all,rand);
			for(int i=0;i<20;i++) {
				firstLayerAnswers[i]=1/(1+Math.pow(e, -firstLayer[i].sum()));
			}
			setVectorsSecondLayer(firstLayerAnswers, secondLayer);
			for(int i=0;i<5;i++) {
				secondLayerAnswers[i]=1/(1+Math.pow(e, -secondLayer[i].sum()));
			}
			setVectorsThirdLayer(secondLayerAnswers, thirdLayer);
			for(int i=0;i<20;i++) {
				thirdLayerAnswers[i]=1/(1+Math.pow(e, -thirdLayer[i].sum()));
			}

			for(int i=0;i<20;i++) {
				macierzBledowW3[i]=answers[which][i]-thirdLayerAnswers[i];
			}
			for(int i=0;i<20;i++) {
				System.out.print(df.format(macierzBledowW3[i])+", ");
			}
			System.out.print("\nExpected answer:\n");
			for(int i=0;i<20;i++) {
				System.out.print(answers[which][i]+", ");
			}
			System.out.println("");
		}
	}
	
	private static void setVectorsThirdLayer(double[] secondLayerAnswers, perc2[] thirdLayer) {
		// TODO Auto-generated method stub
		for(int i=0;i<20;i++) {
			thirdLayer[i].setVector(secondLayerAnswers);
		}
	}
	private static void setAnswerVectors(double[][] answers) {
		// TODO Auto-generated method stub
		double[] row =  {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		answers[0]=row;
		double[] row1 = {0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		answers[1]=row1;
		double[] row2 = {0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		answers[2]=row2;
		double[] row3 = {0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		answers[3]=row3;
		double[] row4 = {0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		answers[4]=row4;
		double[] row5 = {0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		answers[5]=row5;
		double[] row6 = {0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0};
		answers[6]=row6;
		double[] row7 = {0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0};
		answers[7]=row7;
		double[] row8 = {0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0};
		answers[8]=row8;
		double[] row9 = {0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0};
		answers[9]=row9;
		double[] row10 = {0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0};
		answers[10]=row10;
		double[] row11 = {0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0};
		answers[11]=row11;
		double[] row12 = {0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0};
		answers[12]=row12;
		double[] row13 = {0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0};
		answers[13]=row13;
		double[] row14 = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0};
		answers[14]=row14;
		double[] row15 = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0};
		answers[15]=row15;
		double[] row16 = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0};
		answers[16]=row16;
		double[] row17 = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0};
		answers[17]=row17;
		double[] row18 = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0};
		answers[18]=row18;
		double[] row19 = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1};
		answers[19]=row19;
	}
	private static void setVectorsSecondLayer(double[] firstLayerAnswers, perc2[] secondLayer) {
		// TODO Auto-generated method stub
		for(int i=0;i<5;i++) {
			secondLayer[i].setVector(firstLayerAnswers);
		}
	}
	public static void createWectors(double[][] all) {
		double[] A={0,1,1,0,1,0,0,1,1,1,1,1,1,0,0,1,1,0,0,1};
		double[] B={1,1,1,0,1,0,0,1,1,1,1,0,1,0,0,1,1,1,1,0};
		double[] C={0,1,1,0,1,0,0,1,1,0,0,0,1,0,0,1,0,1,1,0};
		double[] D={1,1,1,0,1,0,0,1,1,0,0,1,1,0,0,1,1,1,1,0};
		double[] E={1,1,1,1,1,0,0,0,1,1,1,1,1,0,0,0,1,1,1,1};
		double[] F={1,1,1,1,1,0,0,0,1,1,1,0,1,0,0,0,1,0,0,0};
		double[] G={0,1,1,0,1,0,0,0,1,0,1,0,1,0,0,1,0,1,1,0};
		double[] H={1,0,0,1,1,0,0,1,1,1,1,1,1,0,0,1,1,0,0,1};
		double[] I={0,1,1,0,0,1,1,0,0,1,1,0,0,1,1,0,0,1,1,0};
		double[] J={0,1,1,1,0,0,0,1,0,0,0,1,0,0,0,1,1,1,1,0};
		double[] a={0,0,0,0,0,0,0,0,0,1,1,0,1,0,1,0,0,1,1,0};			
		double[] b={0,0,0,0,1,0,0,0,1,1,0,0,1,0,1,0,0,1,0,0};
		double[] c={0,0,0,0,0,0,0,0,0,0,1,0,0,1,0,0,0,0,1,0};
		double[] d={0,0,0,0,0,0,1,0,0,1,1,0,1,0,1,0,0,1,1,0};
		double[] e={0,0,0,0,0,1,0,0,1,0,1,0,1,0,0,0,0,1,1,0};
		double[] f={0,0,0,0,0,1,1,0,1,0,0,0,1,1,0,0,1,0,0,0};
		double[] g={0,0,0,0,0,1,0,0,1,0,1,0,0,0,1,0,0,1,0,0};
		double[] h={0,0,0,0,1,0,0,0,1,0,0,0,1,1,1,0,1,0,1,0};
		double[] i={0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,1,0,0};
		double[] j={0,0,0,0,0,0,1,0,0,0,1,0,0,0,1,0,0,1,0,0};
		all[0]=A;
		all[1]=B;
		all[2]=C;
		all[3]=D;
		all[4]=E;
		all[5]=F;
		all[6]=G;
		all[7]=H;
		all[8]=I;
		all[9]=J;
		all[10]=a;
		all[11]=b;
		all[12]=c;
		all[13]=d;
		all[14]=e;
		all[15]=f;
		all[16]=g;
		all[17]=h;
		all[18]=i;
		all[19]=j;
	}
	public static int setVectorsFirstLayer(perc1[] firstLayer, double[][] all, Random rand) {
		int wejscie = rand.nextInt(20);
		System.out.println("Podaje na wejscie wektor: "+wejscie);
		for(int i=0;i<20;i++) {
			double w1 = all[wejscie][i];
			firstLayer[i].setVariable(w1);
		}
		return wejscie;
	}
}
