/**
 *
 */
package app;

import org.apache.log4j.*;

import GUI.GUI;
import GUI.Simulator;
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
		
		// Load logger config.
		PropertyConfigurator.configure("log4j-client.properties");
		
		// Initiate.
		Mediator med = new Mediator();
		GUI gui = new GUI(med);
		MockupNetwork network = new MockupNetwork(med);
		MockupWSClient wsClient = new MockupWSClient(med, gui);

		// Check if simulation should be started.
		if (args.length > 0 && args[0].compareTo("simulate") == 0)
		{
			Simulator s = new Simulator(med, gui, network);
			s.execute();
		}
	}

}
