package WSServer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Random;

import GUI.components.CellTableModel;
import WSServer.Database;

/**
 * Web service.
 * 
 * @author Stedy
 *
 */
public class Server {
	
	/**
	 * Login user and return -1 or id.
	 * 
	 * @param username
	 * @param password
	 * @param type
	 * @return
	 */
	@SuppressWarnings("resource")
	public int login(String username, String password, int type) {
		
		// Connect db.
		Database db = new Database();
		
		// Search if user exists in the DB.
		ResultSet rs = db.query("SELECT * FROM user WHERE username = '" + username + "'");
		
		int loggedId = -1;
		
		try {
			// Username does not exist.
			if (!rs.next())
			{
				return -1;
			}
			else
			{
				// Password is invalid.
				if (rs.getString("password").compareTo(password) != 0)
				{
					return -1;
				}
				else
				{
					// Return username id from db.
					loggedId = rs.getInt("id");
				}
			}
		} catch (Exception e) {
			// Bad connection.
			return -1;
		}

		// Login user and save type.
		rs = db.query("UPDATE user SET type = '" + type + "' WHERE username = '" + username + "'");

		return loggedId;
	}
	
	/**
	 * Get username for an user.
	 * 
	 * @param userId
	 * @return
	 */
	public String getUsername(int userId)
	{
		// Connect db.
		Database db = new Database();

		// Get username from db.
		ResultSet rs = db.query("SELECT username FROM user WHERE id = '" + userId + "'");

		String username;

		try {
			if (!rs.next())
			{
				return null;
			}

			username = rs.getString("username");

		} catch (SQLException e) {
			return null;
		}

		return username;
	}
	
	/**
	 * Get list of services.
	 * 
	 * @param userId
	 * @return
	 */
	public HashMap<Integer, String> getServiceList()
	{
		HashMap<Integer, String> sList = new HashMap<Integer, String>();
		
		// Get services from DB.
		Database db = new Database();
		ResultSet rs = db.query("SELECT * FROM service");

		try {
			while (rs.next())
			{
				sList.put(rs.getInt("id"), rs.getString("name"));
			}
		} catch (Exception e) {
			return null;
		}
		
		return sList;
	}
}
