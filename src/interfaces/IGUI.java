package interfaces;

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
}
