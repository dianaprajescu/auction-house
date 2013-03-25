package WSClient;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import javax.swing.JOptionPane;

import GUI.GUI;
import GUI.components.CellTableModel;
import app.Database;
import app.Mediator;
import app.UserType;
import interfaces.IWSClient;

public class MockupWSClient implements IWSClient {
	
	private Mediator med;
	
	private GUI gui;
	
	public MockupWSClient(Mediator med, GUI gui)
	{
		this.med = med;
		med.registerWSClient(this);
		
		// Only used in simulation.
		this.gui = gui;
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
		//TODO verify in GUI if we can do logout.
		
		return true;
	}
	
	/**
	 * Sellers are notified when a buyer activates a service.
	 *
	 * @param   int  buyer_id    The buyer that activated the service.
	 * @param   int  service_id  The active service.
	 */
	public CellTableModel launchOfferRequest(int serviceId, int userId)
	{
		// Populate model with a random number of sellers.
		int noSellers = new Random().nextInt(6) + 1;
		
		// Create a new CellTableModel.
		CellTableModel ct = new CellTableModel();

		for (int i = 0; i < noSellers; i++)
		{
			// Create sellers.
			// TODO must be unique.
			int sellerId = (new Random().nextInt(3)) + i * 3;
			Object[] rowx = {sellerId, "seller_name" + sellerId, "No Offer", 0};
			ct.addRow(rowx);
		}
		
		return ct;
	}
	
	/**
	 * Drop offer request.
	 * 
	 * @param serviceId
	 * @param userId
	 * @return
	 */
	public boolean dropOfferRequest(int serviceId, int userId)
	{
		//TODO send refuse offer to all sellers.
		
		return true;
	}
	
	/**
	 * Accept offer. 
	 * @param serviceId
	 * @param buyerId
	 * @param sellerId
	 * @return
	 */
	public boolean acceptOffer(int serviceId, int buyerId, int sellerId)
	{
		//TODO send refuse offer to all sellers.
		
		return true;
	}
	
	/**
	 * Refuse offer. 
	 * @param serviceId
	 * @param buyerId
	 * @param sellerId
	 * @return
	 */
	public boolean refuseOffer(int serviceId, int buyerId, int sellerId)
	{
		//TODO send refuse the selers offer.
		
		return true;
	}
	
	/**
	 * Make offer.
	 * 
	 * @param serviceId
	 * @param buyerId
	 * @param sellerId
	 * @param price
	 * @return
	 */
	public boolean makeOffer(int serviceId, int buyerId, int sellerId, int price)
	{
		//TODO send offer to the buyer. Update other offers (offer exceeded).
		
		return true;
	}
	
	/**
	 * Remove offer. 
	 * @param serviceId
	 * @param buyerId
	 * @param sellerId
	 * @return
	 */
	public boolean removeOffer(int serviceId, int buyerId, int sellerId)
	{
		return true;
	}

	@Override
	public void offerRefused(int serviceId, int buyerId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void offerAccepted(int serviceId, int buyerId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void offerExceeded(int serviceId, int buyerId, int price) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeExceeded(int serviceId, int buyerId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void offerMade(int serviceId, int sellerId, int price) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void offerRemoved(int serviceId, int sellerId) {
		// TODO Auto-generated method stub
		
	}
}
