package interfaces;

import GUI.components.MainTableModel;
import app.UserType;

/**
 * @author Stedy
 *
 */
public interface IWSClient {

	/**
	 * Login current user.
	 * 
	 * @param username
	 * @param password
	 * 
	 * @return The id of the user or -1 if fail.
	 */
	public int login(String username, String password);

	/**
	 * Get a list of services
	 */
	public MainTableModel getServiceList();

	/**
	 * Method used to get the username of a user specified by id.
	 *
	 * @param   userId  The user's id.
	 * @return  The user's username.
	 */
	public String getUsername(int userId);
}
