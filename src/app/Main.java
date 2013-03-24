/**
 *
 */
package app;

import GUI.GUI;
import GUI.Simulator;

/**
 * @author Stedy
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GUI gui = new GUI();

		Simulator s = new Simulator(gui);
		s.execute();
	}

}
