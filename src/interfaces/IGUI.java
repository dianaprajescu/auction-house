package interfaces;

import GUI.components.CellTableModel;
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
}
