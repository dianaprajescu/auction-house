/**
 * 
 */
package GUI;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import GUI.components.BuyerType;
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
			}
	
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Connection with DB lost!", "Connection lost", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}

		// Login user and save type.
		if (buyer.isSelected())
		{
			rs = db.query("UPDATE user SET logged = '2', type = '1' WHERE username = '" + username.getText() + "'");
		}
		else if (seller.isSelected())
		{
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

	public void logout() {
		// Get current logged username.
		String welcomeMessage = ((JLabel) ((JPanel) gui.getContentPane().getComponent(0)).getComponent(0)).getText();
		String[] result = welcomeMessage.split("Welcome to Auction House, ");
		String username = result[1].split("!")[0];
		int type = 0;
		int id = 0;
		
		// Get user type.
		Database db = new Database();
		ResultSet rs = db.query("SELECT * FROM user WHERE username = '" + username + "'");
		try
		{
			if (rs.next())
			{
				type = rs.getInt("type");
				id = rs.getInt("id");
			}
			else
			{
				//  TODO remove all data related to this user.
				JOptionPane.showMessageDialog(null, "Unexpected error!", "Logout problem", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
		} catch (Exception e) {
			//  TODO remove all data related to this user.
			JOptionPane.showMessageDialog(null, "Unexpected error!" + e.getMessage(), "Logout problem", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		// Seller can logout only if he is not taking part into any auctions or if his offers are passed.
		if (type == 2)
		{
			rs = db.query("SELECT * FROM offer WHERE seller_id = '" + id + "'");
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
		rs = db.query("UPDATE user SET logged = '1' WHERE username = '" + username + "'");
		gui.dispose();
		
		JOptionPane.showMessageDialog(null, "Logged out successfully!", "Good bye!", JOptionPane.INFORMATION_MESSAGE);
		login.setVisible(true);
	}
	
	public void showServicePopup(JList list, int x, int y)
	{
		String status = (String) list.getSelectedValue();
		int intStatus = this.getIntStatus(status);
		
		JPopupMenu popup = new JPopupMenu();
		
		//TODO ia din baza de date optiunile disponibile pentru serviciul selectat.
		
		// Get user type from db.
		int type = 0;
		Database db = new Database();
		ResultSet rs = db.query("SELECT * FROM user WHERE username = '" + username + "'");
		try
		{
			rs.next();
			type = rs.getInt("type");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Unexpected error!" + e.getMessage(), "Problem", JOptionPane.ERROR_MESSAGE);
		}
		
		// If user is a buyer.
		if (type == 1)
		{
			if (intStatus == 1)
			{
				popup.add(new PopupMenuItem("Launch Offer Request", this, this.gui));
			}
			else if (intStatus < 5)
			{
				popup.add(new PopupMenuItem("Drop Offer Request", this, this.gui));
			}
		}
		else
		{
	        popup.add(new PopupMenuItem("option1", this, this.gui));
	        popup.add(new PopupMenuItem("option2", this, this.gui));
		}
        
        popup.show(list, x, y); //and show the menu
	}
	
	public void showListPopup(JList list, int x, int y)
	{
		String user = (String) list.getSelectedValue();
		
		// Get current logged user.
		String welcomeMessage = ((JLabel) ((JPanel) gui.getContentPane().getComponent(0)).getComponent(0)).getText();
		String[] result = welcomeMessage.split("Welcome to Auction House, ");
		String username = result[1].split("!")[0];
		
		//String status = (String) this.gui.getTable().getModel().getValueAt(this.gui.getTable().getSelectedRow(), 2);
		
		JPopupMenu popup = new JPopupMenu();
		
		//TODO ia din baza de date optiunile disponibile pentru user selectat.
		
		// Get user type from db.
		int type = 0;
		Database db = new Database();
		ResultSet rs = db.query("SELECT * FROM user WHERE username = '" + username + "'");

		try
		{
			rs.next();
			type = rs.getInt("type");
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Unexpected error!" + e.getMessage(), "Problem", JOptionPane.ERROR_MESSAGE);
		}
		
		// If user is a buyer.
		if (type == 1)
		{
			popup.add(new PopupMenuItem("Accept Offer", this, this.gui));
			
	        popup.add(new PopupMenuItem("Refuse Offer", this, this.gui));
		}
		else
		{
			popup.add(new PopupMenuItem("Make Offer", this, this.gui));
			
	        popup.add(new PopupMenuItem("Drop auction", this, this.gui));
		}
        
        popup.show(list, x, y); //and show the menu
	}
	
	public void userAction(String command)
	{
		//TODO do sth based on command.
		System.out.println(command);
		System.out.println(this.gui.getTable().getModel().getValueAt(this.gui.getTable().getSelectedRow(), 1));
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
		hm.put((Object) "inactive", (Object) new Integer(1));
		hm.put((Object) "no offer", (Object) new Integer(2));
		hm.put((Object) "offer made", (Object) new Integer(3));
		hm.put((Object) "offer exceeded", (Object) new Integer(4));
		hm.put((Object) "offer accepted", (Object) new Integer(5));
		hm.put((Object) "offer refused", (Object) new Integer(6));
		hm.put((Object) "transfer started", (Object) new Integer(7));
		hm.put((Object) "transfer in progress", (Object) new Integer(8));
		hm.put((Object) "transfer completed", (Object) new Integer(9));
		hm.put((Object) "transfer failed", (Object) new Integer(10));
		
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
