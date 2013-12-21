package alexdiru.lifesim.main;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class GrowthOverGenerationsDataset {

	private int generation = 1;
	private XYSeries minSeries = new XYSeries("Minimum Fitness");
	private XYSeries maxSeries = new XYSeries("Maximum Fitness");
	private XYSeries avgSeries = new XYSeries("Average Fitness");
	
	public GrowthOverGenerationsDataset() {
		reset();
	}
	
	protected void addGeneration(double minFitness, double avgFitness, double maxFitness) {
		minSeries.add(generation, minFitness);
		maxSeries.add(generation, maxFitness);
		avgSeries.add(generation, avgFitness);
		generation++;
	}
	
	public void reset() {
		minSeries.clear();
		maxSeries.clear();
		avgSeries.clear();
	}
	
	public XYSeriesCollection toXYSeriesCollection() {
		XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(minSeries);
        dataset.addSeries(maxSeries);
        dataset.addSeries(avgSeries);
        return dataset;
	}

	public XYSeriesCollection toXYSeriesCollectionAverageOnly() { 
		XYSeriesCollection dataset = new XYSeriesCollection();
	    dataset.addSeries(avgSeries);
	    return dataset;
	}

	public XYSeriesCollection toXYSeriesCollectionMaximumOnly() {
		XYSeriesCollection dataset = new XYSeriesCollection();
	    dataset.addSeries(maxSeries);
	    return dataset;
	}

	public XYDataset toXYSeriesCollectionMinimumOnly() {
		XYSeriesCollection dataset = new XYSeriesCollection();
	    dataset.addSeries(minSeries);
	    return dataset;
	}
}
