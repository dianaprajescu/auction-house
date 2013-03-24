/**
 *
 */
package Network;

import interfaces.INetwork;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Random;

import app.Mediator;

/**
 * @author Stedy
 *
 */
public class MockupNetwork implements INetwork {
	private Mediator med;
	private MockupNetwork network;

	/**
	 * Constructor.
	 */
	public MockupNetwork(Mediator med)
	{
		this.med = med;
		this.network = this;
		med.registerNetwork(this);
	}

	/**
	 * Users are notified if a new users activates a service or comes online.
	 */
	public void newOnlineUser(String service, String username)
	{

	}

	/**
	 * Sellers are notified when a buyer activates a service.
	 *
	 * @param   int  buyer_id    The buyer that activated the service.
	 * @param   int  service_id  The active service.
	 */
	public void activateService(int serviceId, int userId)
	{
		med.loadUserList(serviceId);
	}

	public void initTransfer(final int serviceId, final int userId)
	{
		TransferTask tt = new TransferTask((new Random()).nextInt(101));
		tt.addPropertyChangeListener(new PropertyChangeListener(){
			@Override
			public void propertyChange(PropertyChangeEvent pce) {
				if (pce.getPropertyName() == "progress")
				{
					med.updateTransfer(serviceId, userId, (int) pce.getNewValue());
				}
			}
		});
		tt.execute();
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
