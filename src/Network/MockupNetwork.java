/**
 *
 */
package Network;

import interfaces.INetwork;

import java.util.Random;

import org.apache.log4j.Logger;

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
	
	static Logger log = Logger.getLogger(MockupNetwork.class);

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
		log.debug(serviceId + " " + buyerId + " " + sellerId);
		
		File file = this.client.getTransfer(serviceId, buyerId);

		this.med.updateTransfer(file.getServiceId(), file.getBuyerId(), file.getProgress());

		Object[] message = {NetworkMethods.TRANSFER.getInt(), serviceId, buyerId, sellerId};

		client.sendMessage(message);
	}

	@Override
	public void gotTransfer(int progress, int serviceId, int buyerId, int sellerId)
	{
		log.debug(progress + " " + serviceId + " " + buyerId + " " + sellerId);
		
		this.med.updateTransfer(serviceId, sellerId, progress);
	}

	@Override
	public void stopTransfer(int serviceId, int userId)
	{
		
	}

	@Override
	public void login(int userId, UserType type){
		
		log.debug(userId);
		
		client = new ClientNetwork(this);
		client.start();

		Object[] message = {NetworkMethods.LOGIN.getInt(), userId, type.getType()};

		client.sendMessage(message);
	}

	@Override
	public void newUser(int serviceId, int userId) {
		
		log.debug(serviceId + " " + userId);
		
		this.med.newUser(serviceId, userId);
	}

	@Override
	public void logout(int userId){
		
		log.debug(userId);
		
		Object[] message = {NetworkMethods.LOGOUT.getInt(), userId};

		client.sendMessage(message);
	}

	@Override
	public void userLeft(int serviceId, int userId) {
		
		log.debug(serviceId + " " + userId);
		
		this.med.userLeft(serviceId, userId);
	}

	@Override
	public CellTableModel launchOfferRequest(int serviceId, int userId)
	{
		log.debug(serviceId + " " + userId);
		
		// Create a new CellTableModel.
		CellTableModel ct = new CellTableModel();

		this.registerService(serviceId, userId);
		
		return ct;
	}

	@Override
	public boolean dropOfferRequest(int serviceId, int userId)
	{
		log.debug(serviceId + " " + userId);
		
		Object[] message = {NetworkMethods.DROP_OFFER_REQUEST.getInt(), serviceId, userId};

		client.sendMessage(message);

		return true;
	}

	@Override
	public void requestDropped(int serviceId, int buyerId) {
		
		log.debug(serviceId + " " + buyerId);
		
		this.med.requestDropped(serviceId, buyerId);
	}

	@Override
	public boolean acceptOffer(int serviceId, int buyerId, int sellerId)
	{
		log.debug(serviceId + " " + buyerId + " " + sellerId);
		
		Object[] message = {NetworkMethods.ACCEPT_OFFER.getInt(), serviceId, buyerId, sellerId};

		client.sendMessage(message);

		return true;
	}

	@Override
	public boolean refuseOffer(int serviceId, int buyerId, int sellerId)
	{
		log.debug(serviceId + " " + buyerId + " " + sellerId);
		
		Object[] message = {NetworkMethods.REFUSE_OFFER.getInt(), serviceId, buyerId, sellerId};

		client.sendMessage(message);

		return true;
	}

	@Override
	public boolean makeOffer(int serviceId, int buyerId, int sellerId, int price)
	{
		log.debug(serviceId + " " + buyerId + " " + sellerId + " " + price);
		
		Object[] message = {NetworkMethods.MAKE_OFFER.getInt(), serviceId, buyerId, sellerId, price};

		client.sendMessage(message);

		return true;
	}

	@Override
	public void offerMade(int serviceId, int sellerId, int price) {
		
		log.debug(serviceId + " " + sellerId + " " + price);
		
		this.med.offerMade(serviceId, sellerId, price);
	}

	@Override
	public boolean removeOffer(int serviceId, int buyerId, int sellerId)
	{
		log.debug(serviceId + " " + buyerId + " " + sellerId);
		
		Object[] message = {NetworkMethods.REMOVE_OFFER.getInt(), serviceId, buyerId, sellerId};

		client.sendMessage(message);

		return true;
	}

	@Override
	public void offerRemoved(int serviceId, int sellerId) {
		
		log.debug(serviceId + " " + sellerId);
		
		this.med.offerRemoved(serviceId, sellerId);
	}

	@Override
	public void offerRefused(int serviceId, int buyerId) {
		
		log.debug(serviceId + " " + buyerId);
		
		this.med.offerRefused(serviceId, buyerId);
	}

	@Override
	public void offerAccepted(int serviceId, int buyerId) {
		
		log.debug(serviceId + " " + buyerId);
		
		this.med.offerAccepted(serviceId, buyerId);
	}

	@Override
	public void offerExceeded(int serviceId, int buyerId, int price) {
		
		log.debug(serviceId + " " + buyerId + " " + price);
		
		this.med.offerExceeded(serviceId, buyerId, price);
	}

	@Override
	public void removeExceeded(int serviceId, int buyerId, int price) {

		log.debug(serviceId + " " + buyerId + " " + price);
		
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

			return ct;
		}
		else
		{
			return null;
		}
	}

	@Override
	public void registerService(int serviceId, int userId) {
		
		log.debug(serviceId + " " + userId);
		
		Object[] message = {NetworkMethods.REGISTER_SERVICE.getInt(), serviceId, userId};

		client.sendMessage(message);
	}
}
