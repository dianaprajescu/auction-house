/**
 *
 */
package GUI;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
import app.UserType;

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
		
		int logged = -1;
		
		UserType type;
		
		// User is buyer.
		if (buyer.isSelected())
		{
			type = UserType.BUYER;
			
		}
		// User is seller.
		else if (seller.isSelected())
		{
			type = UserType.SELLER;
		}
		else
		{
			JOptionPane.showMessageDialog(null, "No User Type was selected.");
			return;
		}
		
		logged = this.gui.login(username.getText(), password.getText(), type);
		
		// Could not login.
		if (logged == -1)
		{
			JOptionPane.showMessageDialog(null, "Invalid login data.");
			return;
		}
		
		// Login user with the ID got.
		else
		{
			// Close login frame.
			login.setVisible(false);

			// Set the logged in userID;
			username.setId(logged);
			
			// Set the logged userType.
			username.setType(type);
			
			// Add username label in main frame.
			JLabel welcome = gui.getWelcomeLabel();
			welcome.setText("Welcome to Auction House, " + username.getText() + "!");

			// Set gui frame visible.
			gui.setVisible(true);
		}
		
	}

	/**
	 * Logout.
	 */
	public void logout() {
		
		//TODO verify if we can logout the user by checking the values from CTM.
		
		if (this.gui.logout(username.getId()))
		{
			gui.dispose();
			login.setVisible(true);
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Could not logout at current stage.");
		}
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
		if (this.username.getType() == UserType.BUYER)
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

					if (intStatus >= 4 && intStatus <= 8)
					{
						System.out.println("status " + intStatus);
						accepted = true;
					}

					if (intStatus == 9)
					{
						accepted = false;
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
		// Get the status of the current row.
		String status = gui.getTable().getSelectedUserStatus();
		int intStatus = this.getIntStatus(status);

		JPopupMenu popup = new JPopupMenu();

		// If user is a buyer.
		if (this.username.getType() == UserType.BUYER)
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
		MainTable table = ((GUI)this.gui).getTable();
		MainTableModel mtm = (MainTableModel) table.getModel();
		CellTableModel ctm;
		
		switch (command.toLowerCase())
		{
			case "launch offer request":
				
				this.launchOfferRequest(mainRow);
				
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
						String status = ctm.getStatusAt(i);
						int intStatus = this.getIntStatus(status);

						if (intStatus == 2)
						{
							ctm.setValueAt("Offer Refused", i, 1);
						}
					}
					else
					{
						ctm.setValueAt("Transfer Started", cellRow, 1);
						gui.startTransfer(((MainTableModel)gui.getTable().getModel()).getIdAt(mainRow), this.username.getId(), ((CellTableModel)gui.getTable().getValueAt(mainRow, 1)).getIdAt(cellRow));
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
	 * Launch offer request.
	 * 
	 * @param mainRow
	 */
	private void launchOfferRequest(int mainRow)
	{
		MainTable table = ((GUI)this.gui).getTable();
		MainTableModel mtm = (MainTableModel) table.getModel();
		CellTableModel ctm;
		
		// Get the service id.
		int serviceId = ((MainTableModel)gui.getTable().getModel()).getIdAt(mainRow);
		
		// Launch offer request in the sistem.
		ctm = gui.launchOfferRequest(serviceId, this.username.getId());
		
		// Could not do the action.
		if (ctm == null)
		{
			JOptionPane.showMessageDialog(null, "Launching offer request failed.");
		}
		else
		{
			// Activate service.
			mtm.setStatusAt("Active", mainRow);
			
			table.setValueAt(ctm, mainRow, 1);
			((GUI)this.gui).getTable().rebuildTable();
		}
	}
	
	private void dropOffer()
	{
		
	}
	
	/**
	 * Transfer update.
	 * @param serviceId
	 * @param userId
	 * @param progress
	 */
	public void updateTransfer(int serviceId, int userId, int progress)
	{
		// Cannot simulate both buyer and seller at the same time.
		if (this.username.getId() != userId)
		{
			MainTable table = ((GUI)this.gui).getTable();
			MainTableModel mtm = (MainTableModel) table.getModel();
	
			int serviceRow = mtm.findRowByServiceId(serviceId);
			if (serviceRow >= 0)
			{
				CellTableModel ctm = (CellTableModel) table.getValueAt(serviceRow, 1);
				int userRow = ctm.findRowByUserId(userId);
	
				if (progress != 100)
				{
					ctm.setValueAt("Transfer in Progress", userRow, 1);
				}
				else
				{
					ctm.setValueAt("Transfer Completed", userRow, 1);
				}
				ctm.setValueAt(progress, userRow, 2);
	
				ctm.fireTableDataChanged();
				table.rebuildTable();
			}
		}
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
		hm.put("transfer failed", new Integer(8));
		hm.put("transfer completed", new Integer(9));

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
