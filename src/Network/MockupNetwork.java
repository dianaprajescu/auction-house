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

	@Override
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
	public void newUser(int serviceId, int userId, String username) {
		this.med.newUser(serviceId, userId, username);
	}

	@Override
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
			Object[] rowx = {"seller_name" + sellerId, "No Offer", 0};
			ct.addRow(sellerId, rowx);
		}

		return ct;
	}

	@Override
	public boolean dropOfferRequest(int serviceId, int userId)
	{
		//TODO send refuse offer to all sellers.

		return true;
	}

	@Override
	public boolean acceptOffer(int serviceId, int buyerId, int sellerId)
	{
		//TODO send refuse offer to all sellers.

		return true;
	}

	@Override
	public boolean refuseOffer(int serviceId, int buyerId, int sellerId)
	{
		//TODO send refuse the selers offer.

		return true;
	}

	@Override
	public boolean makeOffer(int serviceId, int buyerId, int sellerId, int price)
	{
		//TODO send offer to the buyer. Update other offers (offer exceeded).

		return true;
	}

	@Override
	public boolean removeOffer(int serviceId, int buyerId, int sellerId)
	{
		return true;
	}

	@Override
	public void offerRefused(int serviceId, int buyerId) {
		this.med.offerRefused(serviceId, buyerId);
	}

	@Override
	public void offerAccepted(int serviceId, int buyerId) {
		this.med.offerAccepted(serviceId, buyerId);
	}

	@Override
	public void offerExceeded(int serviceId, int buyerId, int price) {
		this.med.offerExceeded(serviceId, buyerId, price);
	}

	@Override
	public void removeExceeded(int serviceId, int buyerId) {

		this.med.removeExceeded(serviceId, buyerId);
	}

	@Override
	public void offerMade(int serviceId, int sellerId, int price) {

		this.med.offerMade(serviceId, sellerId, price);

	}

	@Override
	public void offerRemoved(int serviceId, int sellerId) {
		this.med.offerRemoved(serviceId, sellerId);
	}

	@Override
	public void dropUser(int userId) {
		this.med.dropUser(userId);
	}
}
