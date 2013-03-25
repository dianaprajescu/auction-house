/**
 *
 */
package app;

import WSClient.MockupWSClient;
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
		Mediator med = new Mediator();
		GUI gui = new GUI(med);
		MockupWSClient wsClient = new MockupWSClient(med, gui);

		Simulator s = new Simulator(med, gui);
		s.execute();
	}

}
