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

    //public static boolean automaticallyPickupFood = true;


    /**
     * The percentage chance two chromosomes are crossed over at evolution stage
     */
    public static int crossoverRate = 60;

    /**
     * The chance a chromosome has for it's senses to mutate (regular gene mutation), = 1/mutationRate
     */
    public static int senseMutationRate = 8;

    /**
     * The chance a chromosome has for it's behaviour to mutate = 1/mutationRate
     */
    public static int behaviourMutationRate = 5;
}
