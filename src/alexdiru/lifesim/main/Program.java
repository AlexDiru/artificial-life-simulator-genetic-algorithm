package alexdiru.lifesim.main;

import alexdiru.lifesim.jgap.ga.GeneManager;

import javax.swing.SwingUtilities;

public class Program {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GUI ex = new GUI();
                ex.setVisible(true);
            }
        });
    }
}