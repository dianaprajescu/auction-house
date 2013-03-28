/**
 *
 */
package app;

import GUI.GUI;
import Network.MockupNetwork;
import WSClient.MockupWSClient;

/**
 * @author Stedy
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Mediator med = new Mediator();
		GUI gui = new GUI(med);
		MockupNetwork network = new MockupNetwork(med);
		MockupWSClient wsClient = new MockupWSClient(med, gui);

		// Comment these 2 lines to stop simulator.
		//Simulator s = new Simulator(med, gui);
		//s.execute();
	}

}
