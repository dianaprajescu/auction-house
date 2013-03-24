/**
 *
 */
package Network;

import interfaces.INetwork;
import app.Mediator;

/**
 * @author Stedy
 *
 */
public class MockupNetwork implements INetwork {
	private Mediator med;

	/**
	 * Constructor.
	 */
	public MockupNetwork(Mediator med)
	{
		this.med = med;
		med.registerNetwork(this);
	}

	/**
	 * Buyers are notified if a new seller comes online.
	 */
	public void newOnlineSeller ()
	{
		med.newOnlineSeller("gigel", 1, 1);
	}

	/**
	 * Sellers are notified when a buyer activates a service.
	 *
	 * @param   int  buyer_id    The buyer that activated the service.
	 * @param   int  service_id  The active service.
	 */
	public void newActiveService(int buyer_id, int service_id)
	{

	}

	/**
	 * Buyers are notified when sellers make an offer.
	 *
	 * @param   int  seller_id  The seller that made the offer.
	 */
	public void offerMade(int seller_id)
	{

	}
}
