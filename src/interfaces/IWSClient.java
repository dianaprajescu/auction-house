package interfaces;

import GUI.components.MainTableModel;
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

	/**
	 * Method used to get the username of a user specified by id.
	 *
	 * @param   userId  The user's id.
	 * @return  The user's username.
	 */
	public String getUsername(int userId);
}
