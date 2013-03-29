package interfaces;

import javax.swing.JTextField;

import app.UserType;

import GUI.GUI;
import GUI.Login;
import GUI.components.CellTableModel;
import GUI.components.LoginButton;
import GUI.components.LogoutButton;
import GUI.components.MainTableModel;
import GUI.components.PasswordField;
import GUI.components.BuyerType;
import GUI.components.SellerType;
import GUI.components.UsernameField;

/**
 * @author Stedy
 *
 */
public interface IGUIMediator {
	public void registerNetwork(INetwork network);
	public void registerWSClient(IWSClient client);
	
	/**
	 * In GUIMediator. Login an user.
	 * 
	 * @param username
	 * @param password
	 * @param type
	 */
	public int login(String username, String password, UserType type);
	
	/**
	 * Logout user with the ID passed in input.
	 * @param userId
	 * @return
	 */
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
	 * A buyer has launched an offer request.
	 *
	 * @param   int  userType  1 for buyer and 2 for seller.
	 * @param
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
