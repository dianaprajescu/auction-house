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
}
