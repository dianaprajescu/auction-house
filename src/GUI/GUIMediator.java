/**
 *
 */
package GUI;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

import GUI.components.BuyerType;
import GUI.components.MainTable;
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
	private JList list;

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
		JPanel mainPanel = (JPanel) gui.getContentPane().getComponent(0);
		mainPanel.add(new JLabel("Welcome to Auction House, " + username.getText() + "!"), "North", 0);

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
	 * @param   int    x      The x coordinate where the right click was made.
	 * @param   int    y      The y coordinate where the right click was made.
	 */
	public void showServicePopup(MainTable table, int x, int y)
	{
		System.out.println(this.gui.getTable().getSelectedStatus());
		System.out.println(this.gui.getTable().getSelectedId());

		// Get service status.
		String status = (String) this.gui.getTable().getModel().getValueAt(this.gui.getTable().getSelectedRow(), 2);
		int intStatus = this.getIntStatus(status);

		// If user is a buyer.
		if (username.getType() == 1)
		{
			// Create popup menu for services.
			JPopupMenu popup = new JPopupMenu();

			if (intStatus == 1)
			{
				popup.add(new PopupMenuItem("Launch Offer Request", this, this.gui));
			}
			else if (intStatus == 2)
			{
				// TODO only if no offer made.

				popup.add(new PopupMenuItem("Drop Offer Request", this, this.gui));
			}

			// Show the menu.
			popup.show(table, x, y);
		}
	}

	public void showTablePopup(JTable table, int x, int y)
	{
		JPopupMenu popup = new JPopupMenu();
		popup.add(new JMenuItem("test"));
		popup.add(new JMenuItem("test1"));
		popup.show(table, x, y);

		System.out.println(this.gui.getTable().getSelectedUserId());
		System.out.println(this.gui.getTable().getSelectedUserStatus());

		/*
		String user = (String) list.getSelectedValue();

		String status = (String) this.gui.getTable().getModel().getValueAt(this.gui.getTable().getSelectedRow(), 2);
		int intStatus = this.getIntStatus(status);

		JPopupMenu popup = new JPopupMenu();

		//TODO ia din baza de date optiunile disponibile pentru user selectat.

		// If user is a buyer.
		if (username.getType() == 1)
		{
			popup.add(new PopupMenuItem("Accept Offer", this, this.gui));

	        popup.add(new PopupMenuItem("Refuse Offer", this, this.gui));
		}
		else
		{
			popup.add(new PopupMenuItem("Make Offer", this, this.gui));

			if (intStatus == 4)
			{
				popup.add(new PopupMenuItem("Drop auction", this, this.gui));
			}
		}

        popup.show(list, x, y); //and show the menu */
	}

	public void userAction(String command)
	{
		//TODO do sth based on command.
		System.out.println(command);
		System.out.println(this.gui.getTable().getModel().getValueAt(this.gui.getTable().getSelectedRow(), 0));
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
		HashMap hm = new HashMap(10);
		hm.put("inactive", new Integer(1));
		hm.put("no offer", new Integer(2));
		hm.put("offer made", new Integer(3));
		hm.put("offer exceeded", new Integer(4));
		hm.put("offer accepted", new Integer(5));
		hm.put("offer refused", new Integer(6));
		hm.put("transfer started", new Integer(7));
		hm.put("transfer in progress", new Integer(8));
		hm.put("transfer completed", new Integer(9));
		hm.put("transfer failed", new Integer(10));

		return ((Integer) hm.get(status.toLowerCase())).intValue();
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

	public void registerList(JList list){
		this.list = list;
	}
}
