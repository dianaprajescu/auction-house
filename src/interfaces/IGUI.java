package interfaces;

import GUI.components.CellTableModel;
import GUI.components.MainTableModel;
import app.UserType;

/**
 * @author Stedy
 *
 */
public interface IGUI {
	/**
	 * Initialize model
	 */
	public void init();
	
	/**
	 * Build the GUI.
	 */
	public void build();
	
	public int login(String username, String password, UserType type);
	
	public boolean logout(int userId);
	
	/**
	 * Start transfer.
	 * 
	 * @param serviceId
	 * @param buyerId
	 * @param sellerId
	 */
	public void startTransfer(int serviceId, int buyerId, int sellerId);
	
	/**
	 * Update transfer.
	 * 
	 * @param serviceId
	 * @param userId
	 * @param progress
	 */
	public void updateTransfer(int serviceId, int userId, int progress);
	
	/**
	 * Launch offer request.
	 * 
	 * @param serviceId
	 * @param userId
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
	 * New user gets online.
	 * 
	 * @param serviceId
	 * @param userId
	 * @param username
	 */
	public void newUser(int serviceId, int userId, String username);
	
	/**
	 * User goes offline.
	 * 
	 * @param serviceId
	 * @param userId
	 */
	public void dropUser(int userId);
	
	/**
	 * Offer was refused by the buyer.
	 * 
	 * @param serviceId
	 * @param buyerId
	 */
	public void offerRefused(int serviceId, int buyerId);
	
	/**
	 * Offer was accepted by the buyer.
	 * 
	 * @param serviceId
	 * @param buyerId
	 */
	public void offerAccepted(int serviceId, int buyerId);
	
	/**
	 * The offer made to buyer was exceeded.
	 * 
	 * @param serviceId
	 * @param buyerId
	 * @param price
	 */
	public void offerExceeded(int serviceId, int buyerId, int price);
	
	/**
	 * Offer is no longer exceeded.
	 * 
	 * @param serviceId
	 * @param buyerId
	 */
	public void removeExceeded(int serviceId, int buyerId);
	
	/**
	 * The seller has made an offer.
	 * 
	 * @param serviceId
	 * @param sellerId
	 * @param price
	 */
	public void offerMade(int serviceId, int sellerId, int price);
	
	/**
	 * The seller has removed his offer.
	 * 
	 * @param serviceId
	 * @param sellerId
	 */
	public void offerRemoved(int serviceId, int sellerId);
	
	/**
	 * Get a list of users for a service.
	 * 
	 * @param serviceId
	 * @param type
	 * @return
	 */
	public CellTableModel getUserList(int serviceId, UserType type);
	
	/**
	 * Get a list of users for the logged in user.
	 *
	 * @param userId
	 * @param type
	 */
	public MainTableModel getServiceList(int userId, UserType type);
}
