/**
 *
 */
package Network;

import interfaces.INetwork;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Random;

import GUI.components.CellTableModel;
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

	@Override
	public void newUser(int serviceId, int userId) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Sellers are notified when a buyer activates a service.
	 *
	 * @param   int  buyer_id    The buyer that activated the service.
	 * @param   int  service_id  The active service.
	 */
	public CellTableModel launchOfferRequest(int serviceId, int userId)
	{
		// Populate model with a random number of sellers.
		int noSellers = new Random().nextInt(6) + 1;
		
		// Create a new CellTableModel.
		CellTableModel ct = new CellTableModel();

		for (int i = 0; i < noSellers; i++)
		{
			// Create sellers.
			// TODO must be unique.
			int sellerId = (new Random().nextInt(3)) + i * 3;
			Object[] rowx = {sellerId, "seller_name" + sellerId, "No Offer", 0};
			ct.addRow(rowx);
		}
		
		return ct;
	}
	
	/**
	 * Drop offer request.
	 * 
	 * @param serviceId
	 * @param userId
	 * @return
	 */
	public boolean dropOfferRequest(int serviceId, int userId)
	{
		//TODO send refuse offer to all sellers.
		
		return true;
	}
	
	/**
	 * Accept offer. 
	 * @param serviceId
	 * @param buyerId
	 * @param sellerId
	 * @return
	 */
	public boolean acceptOffer(int serviceId, int buyerId, int sellerId)
	{
		//TODO send refuse offer to all sellers.
		
		return true;
	}
	
	/**
	 * Refuse offer. 
	 * @param serviceId
	 * @param buyerId
	 * @param sellerId
	 * @return
	 */
	public boolean refuseOffer(int serviceId, int buyerId, int sellerId)
	{
		//TODO send refuse the selers offer.
		
		return true;
	}
	
	/**
	 * Make offer.
	 * 
	 * @param serviceId
	 * @param buyerId
	 * @param sellerId
	 * @param price
	 * @return
	 */
	public boolean makeOffer(int serviceId, int buyerId, int sellerId, int price)
	{
		//TODO send offer to the buyer. Update other offers (offer exceeded).
		
		return true;
	}
	
	/**
	 * Remove offer. 
	 * @param serviceId
	 * @param buyerId
	 * @param sellerId
	 * @return
	 */
	public boolean removeOffer(int serviceId, int buyerId, int sellerId)
	{
		return true;
	}

	@Override
	public void offerRefused(int serviceId, int buyerId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void offerAccepted(int serviceId, int buyerId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void offerExceeded(int serviceId, int buyerId, int price) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeExceeded(int serviceId, int buyerId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void offerMade(int serviceId, int sellerId, int price) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void offerRemoved(int serviceId, int sellerId) {
		// TODO Auto-generated method stub
		
	}
}
