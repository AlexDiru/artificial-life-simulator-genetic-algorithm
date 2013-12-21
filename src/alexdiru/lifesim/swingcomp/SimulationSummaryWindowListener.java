package alexdiru.lifesim.swingcomp;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import alexdiru.lifesim.main.World;

public class SimulationSummaryWindowListener implements WindowListener {

	private World world;
	
	public SimulationSummaryWindowListener(World world) {
		this.world = world;
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		world.prepareForNextGeneration();
	}
	
	////////////////////////
	//Unused:
	////////////////////////
	
	@Override
	public void windowActivated(WindowEvent arg0) {
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
	}
}
