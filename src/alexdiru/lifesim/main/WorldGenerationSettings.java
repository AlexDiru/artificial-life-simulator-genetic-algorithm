package alexdiru.lifesim.main;
import alexdiru.lifesim.interfaces.IXMLConverter;

import java.io.Serializable;

/**
 * The variables used to algorithmically generate a map
 * @author Alex
 *
 */
public class WorldGenerationSettings implements IXMLConverter, Serializable {
	
	private int openSteps;
	private int numberOfLifeForms;
	private int percentageOfFood;
	private int percentageOfPoison;
	private int percentageOfWater;
	private int xSize;
	private int ySize;
    private boolean tileBasedDistribution;

    public WorldGenerationSettings(int xSize, int ySize,int openSteps, int numberOfLifeForms, int percentageOfFood, int percentageOfPoison, int percentageOfWater, boolean tileBasedDistribution) {
		this.xSize = xSize;
		this.ySize = ySize;
		this.openSteps = openSteps;
		this.numberOfLifeForms = numberOfLifeForms;
		this.percentageOfFood = percentageOfFood;
		this.percentageOfPoison = percentageOfPoison;
		this.percentageOfWater = percentageOfWater;
        this.tileBasedDistribution = tileBasedDistribution;
	}
	
	@Override
	public String toXML() {
		StringBuilder xml = new StringBuilder();
		xml.append("<WorldGenerationSettings>\n");
		xml.append("<OpenSteps>" + openSteps + "</OpenSteps>\n");
		xml.append("<NumberOfLifeForms>" + numberOfLifeForms + "</NumberOfLifeForms>\n");
		xml.append("<FoodPercentage>" + percentageOfFood + "</FoodPercentage>\n");
		xml.append("<PoisonPercentage>" + percentageOfPoison + "</PoisonPercentage>\n");
		xml.append("<WaterPercentage>" + percentageOfWater + "</WaterPercentage>\n");
		xml.append("<XSize>" + xSize + "</XSize>\n");
		xml.append("<YSize>" + ySize + "</YSize>\n");
		return xml.toString();
	}
	
	public WorldGenerationSettings() {
		this(50,50,200,50,25,10,0,false);
	}

	public int getOpenSteps() {
		return openSteps;
	}
	
	public void setOpenSteps(int openSteps) {
		this.openSteps = openSteps;
	}
	
	public int getNumberOfLifeForms() {
		return numberOfLifeForms;
	}
	
	public void setNumberOfLifeForms(int numberOfLifeForms) {
		this.numberOfLifeForms = numberOfLifeForms;
	}
	
	public int getPercentageOfFood() {
		return percentageOfFood;
	}
	
	public void setPercentageOfFood(int percentageOfFood) {
		this.percentageOfFood = percentageOfFood;
	}
	
	public int getPercentageOfPoison() {
		return percentageOfPoison;
	}
	
	public void setPercentageOfPoison(int percentageOfPoison) {
		this.percentageOfPoison = percentageOfPoison;
	}
	
	public int getPercentageOfWater() {
		return percentageOfWater;
	}
	
	public void setXSize(int xSize) {
		this.xSize = xSize;
	}
	
	public int getXSize() {
		return xSize;
	}
	
	public void setYSize(int ySize) {
		this.ySize = ySize;
	}
	
	public int getYSize() {
		return ySize;
	}

    public boolean getTileBasedDistribution() {
        return tileBasedDistribution;
    }
}