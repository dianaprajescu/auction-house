package interfaces;

import GUI.components.CellTableModel;
import app.Mediator;
import app.UserType;

/**
 * @author Stedy
 *
 */
public interface IWSClient {
	
	public int login(String username, String password, UserType type);
	
	public boolean logout(int userId);
	
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
}
