package alexdiru.lifesim.main;

import java.util.ArrayList;

import javax.swing.JTree;

import org.jgap.Configuration;
import org.jgap.Gene;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.impl.IntegerGene;

import alexdiru.lifesim.interfaces.IXMLConverter;
import alexdiru.lifesim.jgap.ga.CATBehaviourTree;
import alexdiru.lifesim.jgap.ga.DecisionNode;

public class LifeFormSenses implements IXMLConverter {
	

	private static final int lifeSpan = 400;
	
	private int moveSpeed;
	private int turnSpeed;
	private int sightFovDegrees;
	private int sightDistance;
	private int reachDistance;
	private int memoryLength;
	private CATBehaviourTree behaviourTree;

	
	private LifeForm lifeForm;
	
	
	public void restart() {
	}
	
	public void setProperties(IChromosome chromosome, LifeForm lifeForm) {
		this.lifeForm = lifeForm;
		
		int index = 0;
		
		Gene[] g = chromosome.getGenes();
		moveSpeed = (Integer)g[index++].getAllele();
		turnSpeed = (Integer)g[index++].getAllele();
		sightFovDegrees = (Integer)g[index++].getAllele();
		sightDistance = (Integer)g[index++].getAllele();
		reachDistance = (Integer)g[index++].getAllele();
		memoryLength = (Integer)g[index++].getAllele();
		
		behaviourTree = CATBehaviourTree.createFromChromosome(lifeForm, chromosome);
	}
	
	
	@Override
	public String toXML() {
		StringBuilder xml = new StringBuilder();
		xml.append("<GA>\n");
		xml.append("<MoveSpeed>" + moveSpeed + "</MoveSpeed>\n");
		xml.append("<TurnSpeed>" + turnSpeed + "</TurnSpeed>\n");
		xml.append("<SightFOV>" + sightFovDegrees + "</SightFOV>\n");
		xml.append("<SightDistance>" + sightDistance + "</SightDistance>\n");
		xml.append("<ReachDistance>" + reachDistance + "</ReachDistance>\n");
		xml.append("<MemoryLength>" + memoryLength + "</MemoryLength>\n");
		xml.append("<LifeSpan>" + lifeSpan + "</LifeSpan>\n");
	
		
		xml.append("</GA>\n");
		return xml.toString();
	}

	public int getMemoryLength() {
		return memoryLength;
	}

	public int getMoveSpeed() {
		return moveSpeed;
	}

	public int getTurnSpeed() {
		return turnSpeed;
	}

	public int getSightFovDegrees() {
		return sightFovDegrees;
	}

	public int getSightDistance() {
		return sightDistance;
	}
	
	public int getReachDistance() {
		return reachDistance;
	}

	public int getLifeSpan() {
		return lifeSpan;
	}

	public void evaluateBehaviourTree() {
		behaviourTree.evaluate();
	}

	public CATBehaviourTree getBehaviourTree() {
		return behaviourTree;
	}
}
