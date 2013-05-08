/**
 *
 */
package GUI;

import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

import GUI.components.AuctionTimer;
import GUI.components.AutoCloseDialog;
import GUI.components.BuyerType;
import GUI.components.CellTable;
import GUI.components.CellTableModel;
import GUI.components.MainTable;
import GUI.components.MainTableModel;
import GUI.components.PasswordField;
import GUI.components.PopupMenuItem;
import GUI.components.PriceDialogBox;
import GUI.components.PriceField;
import GUI.components.SellerType;
import GUI.components.UsernameField;
import app.UserType;

/**
 * @author Stedy
 *
 */
public class InternalGUIMediator {
	private GUI gui;
	public Login login;
	private UsernameField username;
	private PasswordField password;
	private BuyerType buyer;
	private SellerType seller;
	private JTextField price = new JTextField();
	public PriceDialogBox pdb;

	/**
	 * Only used for simulation. TODO: delete
	 */
	public void setUsername(String text)
	{
		username.setText(text);
	}

	/**
	 * Only used for simulation. TODO: delete
	 */
	public void setPassword(String text)
	{
		password.setText(text);
	}

	/**
	 * Only used for simulation. TODO: delete
	 */
	public void setType(UserType type)
	{
		if (type == UserType.BUYER)
		{
			buyer.setSelected(true);
			//seller.setSelected(false);
		}
		else
		{
			//buyer.setSelected(false);
			seller.setSelected(true);
		}
	}

	/**
	 * Only used for simulation. TODO: delete
	 */
	public UserType getType()
	{
		return username.getType();
	}

	/**
	 * Only used for simulation. TODO: delete
	 */
	public void setPrice(int price)
	{
		this.price.setText((new Integer(price)).toString());
	}

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

		logged = this.gui.login(username.getText(), new String(password.getPassword()), type);

		// Could not login.
		if (logged == -1)
		{
			AutoCloseDialog.showMessageDialog(null, "Invalid login data.", "Error!", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Login user with the ID got.
		else
		{
			username.setId(logged);
			username.setType(type);
			MainTableModel userServices = this.gui.getServiceList(username.getId(), username.getType());

			if (userServices != null)
			{
				this.gui.getTable().setModel(userServices);

				// Close login frame.
				login.setVisible(false);

				// Add username label in main frame.
				JLabel welcome = gui.getWelcomeLabel();
				welcome.setText("Welcome to Auction House, " + username.getText() + "!");

				// Set gui frame visible.
				gui.setVisible(true);

				MainTable table = this.gui.getTable();
				MainTableModel mtm = (MainTableModel) table.getModel();

				/*
				CellTableModel ctm;

				// Add some services.
				if (username.getType() == UserType.SELLER)
				{
					for (int i=0; i<mtm.getRowCount(); i++)
					{
						int serviceId = mtm.getIdAt(i);

						// Launch offer request in the sistem.
						ctm = gui.getUserList(serviceId, username.getType());

						// There are buyers in the system.
						if (ctm != null)
						{
							// Activate service.
							mtm.setStatusAt("Active", i);

							table.setValueAt(ctm, i, 1);
							this.gui.getTable().rebuildTable();
						}
					}
				}*/
			}
			else
			{
				this.gui.getTable().setModel(new MainTableModel());
			}
		}
	}

	/**
	 * Logout.
	 */
	public void logout() {

		if (this.gui.logout(username.getId()))
		{
			gui.dispose();

			MainTableModel mtm = (MainTableModel) this.gui.getTable().getModel();

			for (int row=0; row < mtm.getRowCount(); row++)
			{
				// Cancel timer.
				mtm.setTimerAt("-", row);

				AuctionTimer timer = mtm.getTimerObjectAt(row);

				if (timer != null)
				{
					timer.cancel();
				}
			}

			this.gui.getTable().setModel(new MainTableModel());
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
				popup.add(new PopupMenuItem("Drop Offer Request", this, this.gui, gui.getTable().getSelectedRow(), 0));
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

					if (intuserStatus == 5)
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
			if (intStatus == 1 || intStatus == 3 || intStatus == 4)
			{
				popup.add(new PopupMenuItem("Make Offer", this, this.gui, gui.getTable().getSelectedRow(), table.getSelectedRow()));
			}

			if (intStatus == 2 || (intStatus >= 5 && intStatus < 9))
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
		MainTable table = this.gui.getTable();
		MainTableModel mtm = (MainTableModel) table.getModel();
		CellTableModel ctm;

		switch (command.toLowerCase())
		{
			case "launch offer request":

				this.launchOfferRequest(mainRow);

				break;

			case "drop offer request":

				this.dropOfferRequest(mainRow);

				break;

			case "accept offer":

				this.acceptOffer(mainRow, cellRow);

				break;

			case "refuse offer":

				this.refuseOffer(mainRow, cellRow);

				break;

			case "make offer":

				this.makeOffer(mainRow, cellRow);

				break;

			case "remove offer":

				this.removeOffer(mainRow, cellRow);

				break;

			case "drop auction":

				this.dropAuction(mainRow, cellRow);

				break;
		}

		((MainTableModel)gui.getTable().getModel()).fireTableDataChanged();
		gui.getTable().rebuildTable();
	}

	/**
	 * Auction time has finished.
	 *
	 * @param row
	 */
	public void auctionExpired(int row)
	{
		MainTable table = this.gui.getTable();
		MainTableModel mtm = (MainTableModel) table.getModel();
		CellTableModel ctm = (CellTableModel) mtm.getValueAt(row, 1);

		// Remove timer.
		mtm.setTimerAt("Expired", row);
		mtm.fireTableCellUpdated(row, 3);

		Integer min = null;
		int rowAccepted = -1;

		//Check if there are offers.
		for (int userRow = 0; userRow < ctm.getRowCount(); userRow++)
		{
			int status = this.getIntStatus(ctm.getStatusAt(userRow));
			if (status == 2)
			{
				if (min == null || ctm.getPriceAt(userRow) < min)
				{
					min = ctm.getPriceAt(userRow);
					rowAccepted = userRow;
				}
			}
		}

		// If there were offers.
		if (min != null)
		{
			this.acceptOffer(row, rowAccepted);
		}
	}

	/**
	 * Launch offer request.
	 *
	 * @param mainRow
	 */
	private void launchOfferRequest(int mainRow)
	{
		MainTable table = this.gui.getTable();
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

			//table.setValueAt(ctm, mainRow, 1);
			//this.gui.getTable().rebuildTable();

			mtm.setTimerObjectAt(new AuctionTimer(this, mtm, mainRow), mainRow);
		}
	}

	/**
	 * Drop offer request.
	 *
	 * @param mainRow
	 */
	private void dropOfferRequest(int mainRow)
	{
		MainTable table = this.gui.getTable();
		MainTableModel mtm = (MainTableModel) table.getModel();

		// Get the service id.
		int serviceId = mtm.getIdAt(mainRow);

		// Drop offer request.
		boolean dropRequest = gui.dropOfferRequest(serviceId, this.username.getId());

		if (!dropRequest)
		{
			JOptionPane.showMessageDialog(null, "Dropping offer request failed.");
		}
		else
		{
			// Deactivate service.
			mtm.setStatusAt("Inactive", mainRow);

			// Cancel timer.
			mtm.setTimerAt("-", mainRow);
			mtm.getTimerObjectAt(mainRow).cancel();

			// Clear user list.
			mtm.setValueAt(new CellTableModel(), mainRow, 1);
		}
	}

	/**
	 * Accept offer.
	 *
	 * @param mainRow
	 * @param cellRow
	 */
	private void acceptOffer(int mainRow, int cellRow)
	{
		MainTable table = this.gui.getTable();
		MainTableModel mtm = (MainTableModel) table.getModel();
		CellTableModel ctm = (CellTableModel) table.getValueAt(mainRow, 1);

		// Get the service id.
		int serviceId = mtm.getIdAt(mainRow);

		// Get the accepted offer id. Seller id.
		int sellerId = ctm.getIdAt(cellRow);

		boolean acceptRequest = gui.acceptOffer(serviceId, this.username.getId(), sellerId);

		if (!acceptRequest)
		{
			JOptionPane.showMessageDialog(null, "Could not accept offer.");
		}
		else
		{
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
						refuseOffer(mainRow, i);
					}
				}
				else
				{
					// Remove timer.
					mtm.setTimerAt("Finished", mainRow);
					mtm.fireTableCellUpdated(mainRow, 3);
					mtm.getTimerObjectAt(mainRow).cancel();

					ctm.setValueAt("Offer Accepted", cellRow, 1);
					//gui.startTransfer(serviceId, this.username.getId(), sellerId);
				}
			}
		}
	}

	/**
	 * Refuse offer.
	 *
	 * @param mainRow
	 * @param cellRow
	 */
	private void refuseOffer(int mainRow, int cellRow)
	{
		MainTable table = this.gui.getTable();
		MainTableModel mtm = (MainTableModel) table.getModel();
		CellTableModel ctm = (CellTableModel) table.getValueAt(mainRow, 1);

		// Get the service id.
		int serviceId = mtm.getIdAt(mainRow);

		// Get the accepted offer id. Seller id.
		int sellerId = ctm.getIdAt(cellRow);

		boolean refuseRequest = gui.refuseOffer(serviceId, this.username.getId(), sellerId);

		// Request failed.
		if (!refuseRequest)
		{
			JOptionPane.showMessageDialog(null, "Could not refuse offer.");
		}
		else
		{
			// Update offer in GUI.
			ctm.setValueAt("Offer Refused", cellRow, 1);

			// Update.
			((MainTableModel)this.gui.getTable().getModel()).fireTableCellUpdated(cellRow, 1);
		}
	}

	/**
	 * Make offer.
	 *
	 * @param mainRow
	 * @param cellRow
	 */
	private void makeOffer(int mainRow, int cellRow)
	{
		MainTable table = this.gui.getTable();
		MainTableModel mtm = (MainTableModel) table.getModel();
		CellTableModel ctm = (CellTableModel) table.getValueAt(mainRow, 1);

		// Get the service id.
		int serviceId = mtm.getIdAt(mainRow);

		// Get the accepted offer id. Seller id.
		int buyerId = ctm.getIdAt(cellRow);

		// Show a price input.
		pdb = new PriceDialogBox(ctm, serviceId, buyerId, gui, this);
		pdb.setLocationRelativeTo(gui);
		pdb.setVisible(true);
	}

	/**
	 * Remove offer.
	 *
	 * @param mainRow
	 * @param cellRow
	 */
	private void removeOffer(int mainRow, int cellRow)
	{
		MainTable table = this.gui.getTable();
		MainTableModel mtm = (MainTableModel) table.getModel();
		CellTableModel ctm = (CellTableModel) table.getValueAt(mainRow, 1);

		// Get the service id.
		int serviceId = mtm.getIdAt(mainRow);

		// Get the accepted offer id. Seller id.
		int buyerId = ctm.getIdAt(cellRow);

		boolean removeRequest = gui.removeOffer(serviceId, buyerId, this.username.getId());

		// Request failed.
		if (!removeRequest)
		{
			JOptionPane.showMessageDialog(null, "Could not remove offer.");
		}
		else
		{
			if (getIntStatus(ctm.getStatusAt(cellRow)) >= 6 && getIntStatus(ctm.getStatusAt(cellRow)) <= 8)
			{
				// Update values.
				ctm.setValueAt("Transfer failed", cellRow, 1);
				ctm.setValueAt("-", cellRow, 2);
			}
			else
			{
				// Update offer in GUI.
				ctm.setValueAt("No Offer", cellRow, 1);
				ctm.setValueAt("-", cellRow, 2);
			}

			// Update.
			((MainTableModel)this.gui.getTable().getModel()).fireTableCellUpdated(cellRow, 1);
			((MainTableModel)this.gui.getTable().getModel()).fireTableCellUpdated(cellRow, 2);
		}
	}

	/**
	 * Drop the auction.
	 *
	 * @param mainRow
	 * @param cellRow
	 */
	private void dropAuction (int mainRow, int cellRow)
	{
		// Same thing as remove offer except it's on "offer exceeded".
		removeOffer(mainRow, cellRow);
	}

	/**
	 * New user gets online.
	 *
	 * @param serviceId
	 * @param userId
	 * @param username
	 */
	public void newUser(int serviceId, int userId, String username)
	{
		MainTable table = this.gui.getTable();
		MainTableModel mtm = (MainTableModel) table.getModel();

		// Get the service id.
		int serviceRow = mtm.findRowByServiceId(serviceId);

		if (serviceRow >= 0)
		{
			// Get the cell table model for service.
			CellTableModel ctm = (CellTableModel) mtm.getValueAt(serviceRow, 1);

			// Add new user.
			Object[] newUser = {username, "No Offer", 0, 0};
			ctm.addRow(userId, newUser);

			// Update.
			ctm.fireTableCellUpdated(serviceRow, 1);

			table.rebuildTable();
		}
	}

	/**
	 * User goes offline.
	 *
	 * @param userId
	 * @param serviceId
	 */
	public void userLeft(int serviceId, int userId)
	{
		MainTable table = this.gui.getTable();
		MainTableModel mtm = (MainTableModel) table.getModel();

		for (int i=0; i<mtm.getRowCount(); i++)
		{
			if (mtm.getIdAt(i) == serviceId)
			{
				// Get the cell table model for service.
				CellTableModel ctm = (CellTableModel) mtm.getValueAt(i, 1);

				// Find the user.
				for (int j = 0; j < ctm.getRowCount(); j++)
				{
					if (ctm.getIdAt(j) == userId)
					{
						// Remove user.
						ctm.removeRow(j);

						break;
					}
				}

				// Update.
				ctm.fireTableDataChanged();
				table.rebuildTable();
			}
		}
	}

	/**
	 * Buyer has dropped request.
	 *
	 * @param serviceId
	 * @param buyerId
	 */
	public void requestDropped(int serviceId, int buyerId)
	{
		MainTable table = this.gui.getTable();
		MainTableModel mtm = (MainTableModel) table.getModel();

		// Get the service id.
		int serviceRow = mtm.findRowByServiceId(serviceId);

		if (serviceRow >= 0)
		{
			// Get the cell table model for service.
			CellTableModel ctm = (CellTableModel) mtm.getValueAt(serviceRow, 1);

			// Get the buyer row.
			int buyerRow = ctm.findRowByUserId(buyerId);

			if (buyerRow >= 0)
			{
				// Remove user.
				ctm.removeRow(buyerRow);

				// Update.
				((MainTableModel)this.gui.getTable().getModel()).fireTableCellUpdated(buyerRow, 1);
			}
		}
	}

	/**
	 * Offer was refused by the buyer.
	 *
	 * @param serviceId
	 * @param buyerId
	 */
	public void offerRefused(int serviceId, int buyerId)
	{
		MainTable table = this.gui.getTable();
		MainTableModel mtm = (MainTableModel) table.getModel();

		// Get the service id.
		int serviceRow = mtm.findRowByServiceId(serviceId);

		if (serviceRow >= 0)
		{
			// Get the cell table model for service.
			CellTableModel ctm = (CellTableModel) mtm.getValueAt(serviceRow, 1);

			// Get the buyer row.
			int buyerRow = ctm.findRowByUserId(buyerId);

			if (buyerRow >= 0)
			{
				ctm.setValueAt("Offer Refused", buyerRow, 1);

				// Update.
				((MainTableModel)this.gui.getTable().getModel()).fireTableCellUpdated(buyerRow, 1);
			}
		}
	}

	/**
	 * Offer was accepted by the buyer.
	 *
	 * @param serviceId
	 * @param buyerId
	 */
	public void offerAccepted(int serviceId, int buyerId)
	{
		MainTable table = this.gui.getTable();
		MainTableModel mtm = (MainTableModel) table.getModel();

		// Get the service id.
		int serviceRow = mtm.findRowByServiceId(serviceId);

		if (serviceRow >= 0)
		{
			// Get the cell table model for service.
			CellTableModel ctm = (CellTableModel) mtm.getValueAt(serviceRow, 1);

			// Get the buyer row.
			int buyerRow = ctm.findRowByUserId(buyerId);

			if (buyerRow >= 0)
			{
				// Mark offer as accepted.
				ctm.setValueAt("Offer Accepted", buyerRow, 1);

				//TODO Start transfer. Review should be set in network probably.
				//this.gui.startTransfer(serviceId, buyerId, username.getId());
			}
		}
	}

	/**
	 * Offer was exceeded.
	 *
	 * @param serviceId
	 * @param buyerId
	 * @param price
	 */
	public void offerExceeded(int serviceId, int buyerId, int price)
	{
		MainTable table = this.gui.getTable();
		MainTableModel mtm = (MainTableModel) table.getModel();

		// Get the service id.
		int serviceRow = mtm.findRowByServiceId(serviceId);

		if (serviceRow >= 0)
		{
			// Get the cell table model for service.
			CellTableModel ctm = (CellTableModel) mtm.getValueAt(serviceRow, 1);

			// Get the buyer row.
			int buyerRow = ctm.findRowByUserId(buyerId);

			if (buyerRow >= 0)
			{
				ctm.setValueAt("Offer Exceeded", buyerRow, 1);
				ctm.setValueAt(price, buyerRow, 2);

				((MainTableModel)this.gui.getTable().getModel()).fireTableCellUpdated(serviceRow, 1);
			}
		}
	}

	/**
	 * The offer is no longer exceeded.
	 *
	 * @param serviceId
	 * @param buyerId
	 */
	public void removeExceeded(int serviceId, int buyerId, int price)
	{
		MainTable table = this.gui.getTable();
		MainTableModel mtm = (MainTableModel) table.getModel();

		// Get the service id.
		int serviceRow = mtm.findRowByServiceId(serviceId);

		if (serviceRow >= 0)
		{
			// Get the cell table model for service.
			CellTableModel ctm = (CellTableModel) mtm.getValueAt(serviceRow, 1);

			// Get the buyer row.
			int buyerRow = ctm.findRowByUserId(buyerId);

			if (buyerRow >= 0)
			{
				// Mark offer as accepted.
				ctm.setValueAt("Offer Made", buyerRow, 1);
				ctm.setValueAt(price, buyerRow, 2);

				((MainTableModel)this.gui.getTable().getModel()).fireTableCellUpdated(serviceRow, 1);
			}
		}
	}

	/**
	 * An offer from seller was made.
	 *
	 * @param serviceId
	 * @param sellerId
	 * @param price
	 */
	public void offerMade(int serviceId, int sellerId, int price)
	{
		MainTable table = this.gui.getTable();
		MainTableModel mtm = (MainTableModel) table.getModel();

		// Get the service id.
		int serviceRow = mtm.findRowByServiceId(serviceId);

		if (serviceRow >= 0)
		{
			// Get the cell table model for service.
			CellTableModel ctm = (CellTableModel) mtm.getValueAt(serviceRow, 1);

			// Get the seller row.
			int sellerRow = ctm.findRowByUserId(sellerId);

			if (sellerRow >= 0)
			{
				ctm.setValueAt("Offer Made", sellerRow, 1);
				ctm.setValueAt(price, sellerRow, 2);
			}
		}
	}

	/**
	 * The offer was removed.
	 *
	 * @param serviceId
	 * @param sellerId
	 */
	public void offerRemoved(int serviceId, int sellerId)
	{
		MainTable table = this.gui.getTable();
		MainTableModel mtm = (MainTableModel) table.getModel();

		// Get the service id.
		int serviceRow = mtm.findRowByServiceId(serviceId);

		if (serviceRow >= 0)
		{
			// Get the cell table model for service.
			CellTableModel ctm = (CellTableModel) mtm.getValueAt(serviceRow, 1);

			// Get the seller row.
			int sellerRow = ctm.findRowByUserId(sellerId);

			if (sellerRow >= 0)
			{
				if (getIntStatus(ctm.getStatusAt(sellerRow)) >= 6 && getIntStatus(ctm.getStatusAt(sellerRow)) <= 8)
				{
					// Update values.
					ctm.setValueAt("Transfer failed", sellerRow, 1);
					ctm.setValueAt("-", sellerRow, 2);
				}
				else
				{
					// Update values.
					ctm.setValueAt("No Offer", sellerRow, 1);
					ctm.setValueAt("-", sellerRow, 2);
				}
				
				// Update.
				((MainTableModel)this.gui.getTable().getModel()).fireTableCellUpdated(sellerRow, 1);
			}
		}
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
			MainTable table = this.gui.getTable();
			MainTableModel mtm = (MainTableModel) table.getModel();

			int serviceRow = mtm.findRowByServiceId(serviceId);
			if (serviceRow >= 0)
			{
				CellTableModel ctm = (CellTableModel) table.getValueAt(serviceRow, 1);
				int userRow = ctm.findRowByUserId(userId);

				if (userRow >= 0)
				{
					if (getIntStatus(ctm.getStatusAt(userRow)) != 8){
						if (progress != 100 )
						{
							ctm.setValueAt("Transfer in Progress", userRow, 1);
						}
						else
						{
							ctm.setValueAt("Transfer Completed", userRow, 1);
						}
						ctm.setValueAt(progress, userRow, 3);
	
						ctm.fireTableDataChanged();
						table.rebuildTable();
					}
				}
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
		hm.put("offer refused", new Integer(4));
		hm.put("offer accepted", new Integer(5));
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

	public void registerPrice(PriceField price) {
		this.price = price;
	}

	/**
	 * Submit a price to the offer.
	 *
	 * @param ctm
	 * @param serviceId
	 * @param buyerId
	 */
	public void submitPrice(CellTableModel ctm, int serviceId, int buyerId)
	{
		int priceOffered = -1;
		try
		{
			priceOffered = Integer.valueOf(this.price.getText());

			if (priceOffered <= 0)
			{
				throw new Exception();
			}
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "Offer should be a positive number.");
		}

		try{
			boolean makeOfferRequest = gui.makeOffer(serviceId, buyerId, username.getId(), priceOffered);

			// Request failed.
			if (!makeOfferRequest)
			{
				JOptionPane.showMessageDialog(null, "Could not make offer.");
			}
			else
			{
				// Update offer in GUI.
				ctm.setValueAt("Offer Made", ctm.findRowByUserId(buyerId), 1);
				ctm.setValueAt(priceOffered, ctm.findRowByUserId(buyerId), 2);

				((MainTableModel)this.gui.getTable().getModel()).fireTableDataChanged();
				this.gui.getTable().rebuildTable();

				pdb.dispose();
			}
		}
		catch(Exception e)
		{

		}

	}
	
	/**
	 * Transfer failed.
	 * 
	 * @param serviceId
	 * @param buyerId
	 * @param sellerId
	 */
	public void transferFailed(int serviceId, int buyerId, int sellerId){
		
	}
}
