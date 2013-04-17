package interfaces;

import GUI.components.CellTableModel;
import GUI.components.MainTableModel;
import app.Mediator;
import app.UserType;

/**
 * @author Stedy
 *
 */
public interface IWSClient {
	
	public int login(String username, String password, UserType type);
	
	public boolean logout(int userId);
	
	/**
	 * Get a list of users for the logged in user.
	 *
	 * @param userId
	 * @param type
	 */
	public MainTableModel getServiceList(int userId, UserType type);
}
