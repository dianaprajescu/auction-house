package interfaces;

import GUI.components.CellTableModel;
import app.UserType;

/**
 * @author Stedy
 *
 */
public interface INetwork {
	/**
	 * Login a new user.
	 *
	 * @param userId
	 * @param type
	 */
	public void login(int userId, UserType type);

	/**
	 * Logout a user.
	 *
	 * @param userId
	 */
	public void logout(int userId);

	/**
	 * New user gets online.
	 *
	 * @param serviceId
	 * @param userId
	 */
	public void newUser(int serviceId, int userId);

	/**
	 * User goes offline.
	 *
	 * @param serviceId
	 * @param userId
	 */
	public void userLeft(int serviceId, int userId);

	/**
	 * Launch an offer request.
	 *
	 * @param serviceId
	 * @param userId
	 * @return
	 */
	public CellTableModel launchOfferRequest(int serviceId, int userId);

	/**
	 * Drop offer request.
	 *
	 * @param serviceId
	 * @param userId
	 * @return
	 */
	public boolean dropOfferRequest(int serviceId, int userId);

	/**
	 * The offer request was dropped by the buyer.
	 *
	 * @param serviceId
	 * @param buyerId
	 */
	public void requestDropped(int serviceId, int buyerId);

	/**
	 * Accept offer.
	 * @param serviceId
	 * @param buyerId
	 * @param sellerId
	 * @return
	 */
	public boolean acceptOffer(int serviceId, int buyerId, int sellerId);

	/**
	 * Refuse offer.
	 * @param serviceId
	 * @param buyerId
	 * @param sellerId
	 * @return
	 */
	public boolean refuseOffer(int serviceId, int buyerId, int sellerId);

	/**
	 * Make offer.
	 *
	 * @param serviceId
	 * @param buyerId
	 * @param sellerId
	 * @param price
	 * @return
	 */
	public boolean makeOffer(int serviceId, int buyerId, int sellerId, int price);

	/**
	 * Remove offer.
	 * @param serviceId
	 * @param buyerId
	 * @param sellerId
	 * @return
	 */
	public boolean removeOffer(int serviceId, int buyerId, int sellerId);

	/**
	 * Announce a seller that his offer was refused.
	 * @param serviceId
	 * @param buyerId
	 */
	public void offerRefused(int serviceId, int buyerId);

	/**
	 * Announce a seller that his offer was accepted.
	 * @param serviceId
	 * @param buyerId
	 */
	public void offerAccepted(int serviceId, int buyerId);

	/**
	 * Announce a seller that his offer was exceeded.
	 *
	 * @param serviceId
	 * @param buyerId
	 * @param price
	 */
	public void offerExceeded(int serviceId, int buyerId, int price);

	/**
	 * Announce a seller that his offer is no longer exceeded.
	 *
	 * @param serviceId
	 * @param buyerId
	 * @param price
	 */
	public void removeExceeded(int serviceId, int buyerId, int price);

	/**
	 * Announce a buyer that there was made an offer for a service.
	 *
	 * @param serviceId
	 * @param sellerId
	 * @param price
	 */
	public void offerMade(int serviceId, int sellerId, int price);

	/**
	 * Announce a buyer that a seller removed his offer.
	 *
	 * @param serviceId
	 * @param buyerId
	 */
	public void offerRemoved(int serviceId, int sellerId);

	/**
	 * Get a list of users for the logged in user.
	 *
	 * @param serviceId
	 * @param type
	 */
	public CellTableModel getUserList(int serviceId, UserType type);

	/**
	 * Register a new service.
	 *
	 * @param serviceId
	 * @param userId
	 */
	public void registerService(int serviceId, int userId);

	/**
	 * Continue transfer.
	 *
	 * @param serviceId
	 * @param buyerId
	 * @param sellerId
	 */
	public void transfer(int serviceId, int buyerId, int sellerId);

	/**
	 * New transfer received.
	 * 
	 * @param progress
	 * @param serviceid
	 * @param buyerId
	 * @param sellerId
	 */
	void gotTransfer(int progress, int serviceid, int buyerId, int sellerId);
	
	/**
	 * Transfer failed.
	 * 
	 * @param serviceId
	 * @param buyerId
	 * @param sellerId
	 */
	void transferFailed(int serviceId, int buyerId, int sellerId);
}
