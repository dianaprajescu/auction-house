/**
 * 
 */
package Network;

import app.Mediator;
import interfaces.INetwork;

/**
 * @author diana
 *
 */
public class Network implements INetwork {
	private Mediator med = new Mediator();
	
	/**
	 * Constructor.
	 */
	public Network()
	{
		med.registerNetwork(this);
	}
}
