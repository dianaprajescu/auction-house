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

	/**
	 * Constructor.
	 */
	public MockupNetwork(Mediator med)
	{
		this.med = med;
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

	/**
	 * Start transfer from seller to buyer.
	 * 
	 * @param serviceId
	 * @param buyerId
	 * @param sellerId
	 */
	public void startTransfer(final int serviceId, final int buyerId, final int sellerId)
	{
		TransferTask tt = new TransferTask( 20 + (new Random()).nextInt(61));
		
		tt.addPropertyChangeListener(new PropertyChangeListener(){
			@Override
			public void propertyChange(PropertyChangeEvent pce) {
				if (pce.getPropertyName() == "progress")
				{
					med.updateTransfer(serviceId, sellerId, (int) pce.getNewValue());
					med.updateTransfer(serviceId, buyerId, (int) pce.getNewValue());
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
