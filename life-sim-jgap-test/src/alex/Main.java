package alex;

import org.epochx.gp.model.GPModel;
import org.epochx.gp.op.crossover.SubtreeCrossover;
import org.epochx.gp.representation.GPCandidateProgram;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.random.RandomNumberGenerator;

public class Main extends GPModel {
	
	
	

	public static void main(String args[]) {
		RandomNumberGenerator randomNumberGenerator = new RandomNumberGenerator();
		SubtreeCrossover sc = new SubtreeCrossover(randomNumberGenerator);
		
		GPCandidateProgram gpCP = new GPCandidateProgram(this);
		gpCP.
	}

	@Override
	public Class<?> getReturnType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getFitness(CandidateProgram arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
}
