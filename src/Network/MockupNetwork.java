/**
 *
 */
package Network;

import interfaces.INetwork;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import GUI.components.CellTableModel;
import GUI.components.MainTableModel;
import app.Database;
import app.Mediator;
import app.UserType;

/**
 * @author Stedy
 *
 */
public class MockupNetwork implements INetwork {
	private Mediator med;

	/**
	 * Constructor.
	 */
	public MockupNetwork(Mediator med)
	{
		this.med = med;
		med.registerNetwork(this);
	}

	@Override
	public void startTransfer(final int serviceId, final int buyerId, final int sellerId)
	{
		TransferTask tt = new TransferTask( 20 + (new Random()).nextInt(61));

		tt.addPropertyChangeListener(new PropertyChangeListener(){
			@Override
			public void propertyChange(PropertyChangeEvent pce) {
				if (pce.getPropertyName() == "progress")
				{
					med.updateTransfer(serviceId, sellerId, (int) pce.getNewValue());
					med.updateTransfer(serviceId, buyerId, (int) pce.getNewValue());
				}
			}
		});
		tt.execute();
	}

	@Override
	public void newUser(int serviceId, int userId, String username) {
		this.med.newUser(serviceId, userId, username);
	}

	@Override
	public CellTableModel launchOfferRequest(int serviceId, int userId)
	{
		// Populate model with a random number of sellers.
		int noSellers = new Random().nextInt(6) + 1;

		// Create a new CellTableModel.
		CellTableModel ct = new CellTableModel();

		for (int i = 0; i < noSellers; i++)
		{
			// Create sellers.
			int sellerId = (new Random().nextInt(3)) + i * 3;
			Object[] rowx = {"seller_name" + sellerId, "No Offer", "-", 0};
			ct.addRow(sellerId, rowx);
		}

		return ct;
	}

	@Override
	public boolean dropOfferRequest(int serviceId, int userId)
	{
		//TODO send refuse offer to all sellers.

		return true;
	}

	@Override
	public boolean acceptOffer(int serviceId, int buyerId, int sellerId)
	{
		//TODO send refuse offer to all sellers.

		return true;
	}

	@Override
	public boolean refuseOffer(int serviceId, int buyerId, int sellerId)
	{
		//TODO send refuse the selers offer.

		return true;
	}

	@Override
	public boolean makeOffer(int serviceId, int buyerId, int sellerId, int price)
	{
		//TODO send offer to the buyer. Update other offers (offer exceeded).

		return true;
	}

	@Override
	public boolean removeOffer(int serviceId, int buyerId, int sellerId)
	{
		return true;
	}

	@Override
	public void offerRefused(int serviceId, int buyerId) {
		this.med.offerRefused(serviceId, buyerId);
	}

	@Override
	public void offerAccepted(int serviceId, int buyerId) {
		this.med.offerAccepted(serviceId, buyerId);
	}

	@Override
	public void offerExceeded(int serviceId, int buyerId, int price) {
		this.med.offerExceeded(serviceId, buyerId, price);
	}

	@Override
	public void removeExceeded(int serviceId, int buyerId) {

		this.med.removeExceeded(serviceId, buyerId);
	}

	@Override
	public void offerMade(int serviceId, int sellerId, int price) {

		this.med.offerMade(serviceId, sellerId, price);

	}

	@Override
	public void offerRemoved(int serviceId, int sellerId) {
		this.med.offerRemoved(serviceId, sellerId);
	}

	@Override
	public void dropUser(int userId) {
		this.med.dropUser(userId);
	}

	@Override
	public CellTableModel getUserList(int serviceId, UserType type) {
		boolean add = (new Random()).nextBoolean();
		
		if (add)
		{
			// Populate model with a random number of sellers.
			int noUsers = new Random().nextInt(6) + 1;
		
			// Create a new CellTableModel.
			CellTableModel ct = new CellTableModel();
		
			for (int i = 0; i < noUsers; i++)
			{
				// Create sellers.
				int userId = (new Random().nextInt(3)) + i * 3;
				Object[] rowx = {"user_name" + userId, "No Offer", "-", 0};
				ct.addRow(userId, rowx);
			}
		
			return ct;
		}
		else
		{
			return null;
		}
	}

	@Override
	public MainTableModel getServiceList(int userId, UserType type) {
		// Populate model.
		MainTableModel model = new MainTableModel();

		// Get services from DB.
		Database db = new Database();
		ResultSet rs = db.query("SELECT * FROM service");

		try {
			while (rs.next())
			{
				boolean add = (new Random()).nextBoolean();
				
				if (add)
				{
					Object[] row = {rs.getString("name"), new CellTableModel(), "Inactive", "-"};
					model.addRow(rs.getInt("id"), row);
				}
			}
		} catch (SQLException e1) {
			return null;
		}
		
		return model;
	}
}
