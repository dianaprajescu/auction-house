package WSClient;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import app.Database;
import app.Mediator;
import app.UserType;
import interfaces.IWSClient;

public class MockupWSClient implements IWSClient {
	
	private Mediator med;
	
	public MockupWSClient(Mediator med)
	{
		this.med = med;
		med.registerWSClient(this);
	}

	@Override
	public int login(String username, String password, UserType type) {
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
		} catch (SQLException e) {
			// Bad connection.
			return -1;
		}

		// Login user and save type.
		if (type == UserType.BUYER)
		{
			rs = db.query("UPDATE user SET type = '1' WHERE username = '" + username + "'");
		}
		else if (type == UserType.SELLER)
		{
			rs = db.query("UPDATE user SET type = '2' WHERE username = '" + username + "'");
		}
		else
		{
			// Invalid User Type.
			return -1;
		}
		
		return loggedId;
	}
	
	/**
	 * Logout user.
	 * 
	 * @param userId
	 * @return
	 */
	public boolean logout(int userId)
	{
		return true;
	}

}
