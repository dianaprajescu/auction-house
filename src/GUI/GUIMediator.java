/**
 *
 */
package GUI;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import GUI.components.BuyerType;
import GUI.components.CellTable;
import GUI.components.CellTableModel;
import GUI.components.MainTable;
import GUI.components.MainTableModel;
import GUI.components.PasswordField;
import GUI.components.PopupMenuItem;
import GUI.components.SellerType;
import GUI.components.UsernameField;
import app.Database;

/**
 * @author diana
 *
 */
public class GUIMediator {
	private GUI gui;
	private Login login;
	private UsernameField username;
	private PasswordField password;
	private BuyerType buyer;
	private SellerType seller;

	/**
	 * Login.
	 */
	public void login() {
		// Connect db.
		Database db = new Database();

		// Search if user exists in the DB.
		ResultSet rs = db.query("SELECT * FROM user WHERE username = '" + username.getText() + "'");

		try {
			if (!rs.next())
			{
				JOptionPane.showMessageDialog(null, "The username " + username.getText() + " does not exist!",
						"Invalid Login", JOptionPane.ERROR_MESSAGE);
				return;
			}
			else
			{
				// Check password.
				if (rs.getString("password").compareTo(password.getText()) != 0)
				{
					JOptionPane.showMessageDialog(null, "Password is incorect!",
							"Invalid Login", JOptionPane.ERROR_MESSAGE);
					return;
				}
				else
				{
					// Set username id from db.
					username.setId(rs.getInt("id"));
				}
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Connection with DB lost!", "Connection lost", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}

		// Login user and save type.
		if (buyer.isSelected())
		{
			username.setType(1);
			rs = db.query("UPDATE user SET logged = '2', type = '1' WHERE username = '" + username.getText() + "'");
		}
		else if (seller.isSelected())
		{
			username.setType(2);
			rs = db.query("UPDATE user SET logged = '2', type = '2' WHERE username = '" + username.getText() + "'");
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Please select Buyer or Seller profile!",
					"Invalid Login", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Close login frame.
		login.setVisible(false);

		// Add username label in main frame.
		JLabel welcome = gui.getWelcomeLabel();
		welcome.setText("Welcome to Auction House, " + username.getText() + "!");

		// Set gui frame visible.
		gui.setVisible(true);
	}

	/**
	 * Logout.
	 */
	public void logout() {
		// Connect db.
		Database db = new Database();
		ResultSet rs;

		// Seller can logout only if he is not taking part into any auctions or if his offers are passed.
		if (username.getType() == 2)
		{
			rs = db.query("SELECT * FROM offer WHERE seller_id = '" + username.getId() + "'");
			try {
				// The seller is taking part in some auctions.
				if (rs.next())
				{
					// TODO Are the seller's offers passed?

				}

			} catch (SQLException e) {
				//  TODO remove all data related to this user.
				JOptionPane.showMessageDialog(null, "Unexpected error! " + e.getMessage(), "Logout problem", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
		}

		// When buyer loogs out he leaves all active auctions, declining all offers.
		// If there is no offer, his request is canceled.

		// Logging out during service transfer will cause the transfer to fail and it'll be marked accordingly.

		// Logout.
		rs = db.query("UPDATE user SET logged = '1' WHERE username = '" + username.getText() + "'");
		gui.dispose();

		JOptionPane.showMessageDialog(null, "Logged out successfully!", "Good bye!", JOptionPane.INFORMATION_MESSAGE);
		login.setVisible(true);
	}

	/**
	 * Show service option list.
	 *
	 * @param   MainTable  table  Source of the mouse event.
	 * @param   int        x      The x coordinate where the right click was made.
	 * @param   int        y      The y coordinate where the right click was made.
	 */
	public void showServicePopup(MainTable table, int x, int y)
	{
		System.out.println(this.gui.getTable().getSelectedStatus());
		System.out.println(this.gui.getTable().getSelectedId());

		// Get service status.
		String status = this.gui.getTable().getSelectedStatus();

		// If user is a buyer.
		if (username.getType() == 1)
		{
			// Create popup menu for services.
			JPopupMenu popup = new JPopupMenu();

			// Service inactive.
			if (status.compareToIgnoreCase("inactive") == 0)
			{
				popup.add(new PopupMenuItem("Launch Offer Request", this, this.gui, gui.getTable().getSelectedRow(), 0));
			}

			// Service active.
			else if (status.compareToIgnoreCase("active") == 0)
			{
				// Show this option only if no offer accepted.
				CellTableModel ctm = (CellTableModel) gui.getTable().getValueAt(gui.getTable().getSelectedRow(), 1);
				int i;
				boolean accepted = false;
				for (i = 0; i < ctm.getRowCount(); i++)
				{
					String userStatus = ctm.getStatusAt(i);
					int intStatus = this.getIntStatus(userStatus);

					if (intStatus == 4)
					{
						accepted = true;
					}
				}

				if (accepted == false)
				{
					popup.add(new PopupMenuItem("Drop Offer Request", this, this.gui, gui.getTable().getSelectedRow(), 0));
				}
			}

			// Show the menu.
			popup.show(table, x, y);
		}
	}

	/**
	 * Show user option list.
	 *
	 * @param   CellTable  table  Source of the mouse event.
	 * @param   int        x      The x coordinate where the right click was made.
	 * @param   int        y      The y coordinate where the right click was made.
	 */
	public void showTablePopup(CellTable table, int x, int y)
	{
		System.out.println(this.gui.getTable().getSelectedUserId());
		System.out.println(this.gui.getTable().getSelectedUserStatus());

		// TODO start transfer when win
		// this.gui.startTransfer(this.gui.getTable().getSelectedId(), this.gui.getTable().getSelectedUserId());

		String status = gui.getTable().getSelectedUserStatus();
		int intStatus = this.getIntStatus(status);

		JPopupMenu popup = new JPopupMenu();

		// If user is a buyer.
		if (username.getType() == 1)
		{
			// Accept or refuse offer are available only if an offer was made.
			if (intStatus == 2)
			{
				// Option available only if no other offer was accepted.
				CellTableModel ctm = (CellTableModel) gui.getTable().getValueAt(gui.getTable().getSelectedRow(), 1);
				int i;
				boolean accepted = false;
				for (i = 0; i < ctm.getRowCount(); i++)
				{
					String userStatus = ctm.getStatusAt(i);
					int intuserStatus = this.getIntStatus(userStatus);

					if (intuserStatus == 4)
					{
						accepted = true;
					}
				}

				if (accepted == false)
				{
					popup.add(new PopupMenuItem("Accept Offer", this, this.gui, gui.getTable().getSelectedRow(), table.getSelectedRow()));
				}

				// Refuse offer.
		        popup.add(new PopupMenuItem("Refuse Offer", this, this.gui, gui.getTable().getSelectedRow(), table.getSelectedRow()));
			}
		}
		else
		{
			if (intStatus == 1)
			{
				popup.add(new PopupMenuItem("Make Offer", this, this.gui, gui.getTable().getSelectedRow(), table.getSelectedRow()));
			}

			if (intStatus == 2)
			{
				popup.add(new PopupMenuItem("Remove Offer", this, this.gui, gui.getTable().getSelectedRow(), table.getSelectedRow()));
			}

			if (intStatus == 3)
			{
				popup.add(new PopupMenuItem("Drop auction", this, this.gui, gui.getTable().getSelectedRow(), table.getSelectedRow()));
			}
		}

        popup.show(table, x, y); //and show the menu
	}

	/**
	 * Execute selected action from popup.
	 *
	 * @param   String  command  The requested command.
	 * @param   int     row      The selected row in table.
	 */
	public void userAction(String command, int mainRow, int cellRow)
	{
		//TODO do sth based on command.
		System.out.println(command);
		System.out.println(this.gui.getTable().getSelectedService());

		CellTableModel ctm;

		switch (command.toLowerCase())
		{
			case "launch offer request":
				// Activate service.
				((MainTableModel) gui.getTable().getModel()).setStatusAt("Active", mainRow);

				// TODO Launch offer request in the sistem. (Notify Network?)
				gui.getMediator().activateService(((MainTableModel)gui.getTable().getModel()).getIdAt(mainRow), this.username.getId());

				break;

			case "drop offer request":
				// Deactivate service.
				((MainTableModel) gui.getTable().getModel()).setStatusAt("Inactive", mainRow);

				// Clear user list.
				gui.getTable().setValueAt(new CellTableModel(), mainRow, 1);

				// TODO Refuse all offers.
				//gui.getMediator().refuseAllOffers();

				break;

			case "accept offer":
				// Accept offer.
				ctm = (CellTableModel) gui.getTable().getValueAt(mainRow, 1);

				// Refuse offers.
				int i;
				for (i = 0; i < ctm.getRowCount(); i++)
				{
					if (i != cellRow)
					{
						ctm.setValueAt("Offer Refused", i, 1);
					}
					else
					{
						ctm.setValueAt("Offer Accepted", cellRow, 1);
					}
				}

				// Refuse all other offers.
				//gui.getMediator().refuseAllOffers();

				break;

			case "refuse offer":
				// Refuse offer.
				ctm = (CellTableModel) gui.getTable().getValueAt(mainRow, 1);
				ctm.setValueAt("Offer Refused", cellRow, 1);

				// TODO Refuse offer.
				//gui.getMediator().refuseOffer();

				break;

			case "make offer":
				// Make offer.
				ctm = (CellTableModel) gui.getTable().getValueAt(mainRow, 1);
				ctm.setValueAt("Offer Made", cellRow, 1);

				// TODO Make offer.
				//gui.getMediator().makeOffer();

				break;

			case "remove offer":
				// remove offer.
				ctm = (CellTableModel) gui.getTable().getValueAt(mainRow, 1);
				ctm.setValueAt("No Offer", cellRow, 1);

				// TODO Remove offer.
				//gui.getMediator().removeOffer();

				break;

			case "drop auction":
				// Drop auction.
				ctm = (CellTableModel) gui.getTable().getValueAt(mainRow, 1);
				ctm.setValueAt("No Offer", cellRow, 1);
				break;
		}

		((MainTableModel)gui.getTable().getModel()).fireTableDataChanged();
		gui.getTable().rebuildTable();
	}

	/**
	 * Method used to transform the status into it's int equivalent.
	 *
	 * @param   String  status  The string status.
	 *
	 * @return  int  The int status equivalent.
	 */
	private int getIntStatus(String status)
	{
		// Status int db values.
		HashMap<String, Integer> hm = new HashMap<String, Integer>(10);
		hm.put("no offer", new Integer(1));
		hm.put("offer made", new Integer(2));
		hm.put("offer exceeded", new Integer(3));
		hm.put("offer accepted", new Integer(4));
		hm.put("offer refused", new Integer(5));
		hm.put("transfer started", new Integer(6));
		hm.put("transfer in progress", new Integer(7));
		hm.put("transfer completed", new Integer(8));
		hm.put("transfer failed", new Integer(9));

		return hm.get(status.toLowerCase()).intValue();
	}

	public void registerGUI(GUI gui) {
		this.gui = gui;
	}

	public void registerLogin(Login login) {
		this.login = login;
	}

	public void registerUsername(UsernameField username) {
		this.username = username;
	}

	public void registerPassword(PasswordField password) {
		this.password = password;
	}

	public void registerBuyerType(BuyerType buyer) {
		this.buyer = buyer;
	}

	public void registerSellerType(SellerType seller) {
		this.seller = seller;
	}
}
