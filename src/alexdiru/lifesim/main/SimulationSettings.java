package alexdiru.lifesim.main;

public class SimulationSettings {

	//Fitness = Stamina + (MaxUsedEnergy - UsedEnergy)
	public static int maxUsedEnergy = 20000;
	
	//Energy -= MoveDistance * Factor
	public static float moveEnergyDecreaseFactor = 0.25f;
	
	//Energy -= TurnAngle * Factor
	public static float turnEnergyDecreaseFactor = 0.25f;
	
	public static boolean useDifferentMapForEachGeneration = false;
	
	public static int simulationPositions = 3;
	
	public static int populationSize = 50;

	public static int maxGeneration = 10000;
}
