/**
 *
 */
package Network;

import interfaces.INetwork;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Random;

import GUI.components.CellTableModel;
import app.Mediator;
import app.UserType;

/**
 * @author Stedy
 *
 */
public class MockupNetwork implements INetwork {
	private Mediator med;

	HashMap<Integer, TransferTask> transfers;

	private ClientNetwork client;

	/**
	 * Constructor.
	 */
	public MockupNetwork(Mediator med)
	{
		this.med = med;
		med.registerNetwork(this);

		//this.client = new ClientNetwork(this);

		transfers = new HashMap<Integer, TransferTask>();
	}

	@Override
	public void startTransfer(final int serviceId, final int buyerId, final int sellerId)
	{
		TransferTask tt = new TransferTask(20 + (new Random()).nextInt(61), serviceId);

		transfers.put(serviceId, tt);

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
	public void stopTransfer(int serviceId, int userId)
	{
		TransferTask tt = transfers.get(serviceId);
		if (tt != null)
		{
			tt.cancel(true);
			transfers.remove(serviceId);
		}
	}

	@Override
	public void login(int userId, UserType type){
		client = new ClientNetwork(this);
		client.start();

		int[] message = {NetworkMethods.LOGIN.getInt(), userId, type.getType()};

		client.sendMessage(message);
	}

	@Override
	public void newUser(int serviceId, int userId, String username) {
		this.med.newUser(serviceId, userId, username);
	}

	@Override
	public void logout(int userId){
		int[] message = {NetworkMethods.LOGOUT.getInt(), userId};

		client.sendMessage(message);

		client.interrupt();
	}

	@Override
	public void userLeft(int serviceId, int userId) {
		this.med.userLeft(serviceId, userId);
	}

	@Override
	public CellTableModel launchOfferRequest(int serviceId, int userId)
	{
		// Create a new CellTableModel.
		CellTableModel ct = new CellTableModel();

		this.registerService(serviceId, userId);
		/*
		for (int i = 0; i < noSellers; i++)
		{
			// Create sellers.
			int sellerId = (new Random().nextInt(3)) + i * 3;
			Object[] rowx = {"seller_name" + sellerId, "No Offer", "-", 0};
			ct.addRow(sellerId, rowx);
		} */

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
		int[] message = {NetworkMethods.MAKE_OFFER.getInt(), serviceId, buyerId, sellerId, price};

		client.sendMessage(message);

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
	public CellTableModel getUserList(int serviceId, UserType type) {
		boolean add = (new Random()).nextBoolean();

		if (add)
		{
			// Populate model with a random number of sellers.
			int noUsers = new Random().nextInt(6) + 1;

			// Create a new CellTableModel.
			CellTableModel ct = new CellTableModel();

			/*
			for (int i = 0; i < noUsers; i++)
			{
				// Create sellers.
				int userId = (new Random().nextInt(3)) + i * 3;
				Object[] rowx = {"user_name" + userId, "No Offer", "-", 0};
				ct.addRow(userId, rowx);
			}*/

			return ct;
		}
		else
		{
			return null;
		}
	}

	@Override
	public void registerService(int serviceId, int userId) {
		int[] message = {NetworkMethods.REGISTER_SERVICE.getInt(), serviceId, userId};

		client.sendMessage(message);
	}
}
