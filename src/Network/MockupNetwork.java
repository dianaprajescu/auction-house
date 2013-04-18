/**
 *
 */
package Network;

import interfaces.INetwork;

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

	private ClientNetwork client;

	/**
	 * Constructor.
	 */
	public MockupNetwork(Mediator med)
	{
		this.med = med;
		med.registerNetwork(this);

		this.client = new ClientNetwork(this);
	}

	@Override
	public void transfer(int serviceId, int buyerId, int sellerId)
	{
		System.out.println("Network: transfer");
		File file = this.client.getTransfer(serviceId, buyerId);

		this.med.updateTransfer(file.getServiceId(), file.getBuyerId(), file.getProgress());

		Object[] message = {NetworkMethods.TRANSFER.getInt(), serviceId, buyerId, sellerId};

		client.sendMessage(message);
	}

	@Override
	public void gotTransfer(int progress, int serviceId, int buyerId, int sellerId)
	{
		this.med.updateTransfer(serviceId, sellerId, progress);
	}

	@Override
	public void stopTransfer(int serviceId, int userId)
	{

	}

	@Override
	public void login(int userId, UserType type){
		client = new ClientNetwork(this);
		client.start();

		Object[] message = {NetworkMethods.LOGIN.getInt(), userId, type.getType()};

		client.sendMessage(message);
	}

	@Override
	public void newUser(int serviceId, int userId) {
		this.med.newUser(serviceId, userId);
	}

	@Override
	public void logout(int userId){
		Object[] message = {NetworkMethods.LOGOUT.getInt(), userId};

		client.sendMessage(message);
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
		Object[] message = {NetworkMethods.DROP_OFFER_REQUEST.getInt(), serviceId, userId};

		client.sendMessage(message);

		return true;
	}

	@Override
	public void requestDropped(int serviceId, int buyerId) {
		this.med.requestDropped(serviceId, buyerId);
	}

	@Override
	public boolean acceptOffer(int serviceId, int buyerId, int sellerId)
	{
		Object[] message = {NetworkMethods.ACCEPT_OFFER.getInt(), serviceId, buyerId, sellerId};

		client.sendMessage(message);

		return true;
	}

	@Override
	public boolean refuseOffer(int serviceId, int buyerId, int sellerId)
	{
		//TODO send refuse the selers offer.
		Object[] message = {NetworkMethods.REFUSE_OFFER.getInt(), serviceId, buyerId, sellerId};

		client.sendMessage(message);

		return true;
	}

	@Override
	public boolean makeOffer(int serviceId, int buyerId, int sellerId, int price)
	{
		Object[] message = {NetworkMethods.MAKE_OFFER.getInt(), serviceId, buyerId, sellerId, price};

		client.sendMessage(message);

		return true;
	}

	@Override
	public void offerMade(int serviceId, int sellerId, int price) {
		this.med.offerMade(serviceId, sellerId, price);
	}

	@Override
	public boolean removeOffer(int serviceId, int buyerId, int sellerId)
	{
		Object[] message = {NetworkMethods.REMOVE_OFFER.getInt(), serviceId, buyerId, sellerId};

		client.sendMessage(message);

		return true;
	}

	@Override
	public void offerRemoved(int serviceId, int sellerId) {
		this.med.offerRemoved(serviceId, sellerId);
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
	public void removeExceeded(int serviceId, int buyerId, int price) {

		this.med.removeExceeded(serviceId, buyerId, price);
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
		Object[] message = {NetworkMethods.REGISTER_SERVICE.getInt(), serviceId, userId};

		client.sendMessage(message);
	}
}
