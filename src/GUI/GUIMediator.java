/**
 * 
 */
package GUI;

import java.awt.HeadlessException;
import java.awt.Point;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

import app.Database;


import GUI.GUI;
import GUI.Login;
import GUI.components.LoginButton;
import GUI.components.LogoutButton;
import GUI.components.PasswordField;
import GUI.components.BuyerType;
import GUI.components.PopupMenuItem;
import GUI.components.SellerType;
import GUI.components.UsernameField;
import interfaces.IGUIMediator;
import interfaces.INetworkMediator;
import interfaces.IWSClientMediator;

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
		JPanel mainPanel = (JPanel) gui.getContentPane().getComponent(0);
		JLabel label = (JLabel) mainPanel.getComponent(0);
		String welcomeMessage = label.getText();
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
	
	public void showListPopup(JList list, int x, int y)
	{
		String username = (String) list.getSelectedValue();
		
		JPopupMenu popup = new JPopupMenu();
		
		//TODO ia din baza de date optiunile disponibile pentru username selectat.
		
        popup.add(new PopupMenuItem("option1", this, this.gui));
        popup.add(new PopupMenuItem("option2", this, this.gui));
        
        popup.show(list, x, y); //and show the menu
	}
	
	public void userAction(String command)
	{
		//TODO do sth based on command.
		System.out.println(command);
		System.out.println(this.gui.getTable().getModel().getValueAt(this.gui.getTable().getSelectedRow(), 1));
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
