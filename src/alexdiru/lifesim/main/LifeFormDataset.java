package alexdiru.lifesim.main;

import java.util.ArrayList;

/**
 * A dataset which holds the fitness values (one fitness value per start position) that a life form has
 * each generation. It provides functions to generate a final fitness value for the life form accounting
 * for all the fitness values in the life form's generation
 */
public class LifeFormDataset {

    /**
     * A list of the fitness values the life form has accumilated in the current generation
     */
	private ArrayList<Float> fitnessValues = new ArrayList<Float>();

    /**
     * Adds
     * @param stamina
     * @param usedEnergy
     */
	public void addData(float stamina, int usedEnergy) {
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
