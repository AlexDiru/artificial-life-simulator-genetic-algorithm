package alexdiru.lifesim.main;

import java.util.ArrayList;

public class LifeFormDataset {
	
	private static final int MAX_USED_ENERGY = 20000;

	private ArrayList<Float> fitnessValues = new ArrayList<Float>();
	
	public void addData(float stamina, int usedEnergy) {
		int leftoverEnergy = MAX_USED_ENERGY - usedEnergy;
		if (leftoverEnergy < 0)
			leftoverEnergy = 0;
		fitnessValues.add(stamina);
	}
	
	public void clear() {
		fitnessValues.clear();
	}
	
	private double getAverage() {
		double sum = 0;
		for (int i = 0; i < fitnessValues.size(); i++)
			sum += fitnessValues.get(i);
		return sum/(double)fitnessValues.size();
	}
	
	private double getStandardDeviation() {
		double av = getAverage();
        double temp = 0;
        for(double a : fitnessValues)
            temp += (av-a)*(av-a);
        return temp/(double)fitnessValues.size();
	}
	
	public double evaluateFitness() {
		double fitness;
		double av = getAverage();
		//double sd = getStandardDeviation();
		//if (sd > 0)
		//	fitness = av / Math.sqrt(Math.sqrt(sd));
		//else 
			fitness = av;
		
		//System.out.println("Fitness: " + fitness + " Average: " + av + " Standard Deviation: " + sd + " SD^: " + Math.sqrt(Math.sqrt(sd)));
		
		return fitness;
	}
}
