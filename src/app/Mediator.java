/**
 *
 */
package app;

import interfaces.IGUI;
import interfaces.IGUIMediator;
import interfaces.INetwork;
import interfaces.INetworkMediator;
import interfaces.IWSClient;
import interfaces.IWSClientMediator;

import java.util.Random;

import GUI.GUI;
import GUI.components.CellTableModel;
import GUI.components.MainTable;
import GUI.components.MainTableModel;
import Network.MockupNetwork;

/**
 * @author Stedy
 *
 */
public class Mediator implements IGUIMediator, INetworkMediator, IWSClientMediator {
	private IGUI gui;
	private INetwork network;
	private IWSClient client;
	
	/**
	 * In GUIMediator. Login an user.
	 * 
	 * @param username
	 * @param password
	 * @param type
	 */
	public int login(String username, String password, UserType type)
	{
		return this.client.login(username, password, type);
	}
	
	/**
	 * Logout user.
	 * 
	 * @param userId
	 * @return
	 */
	public boolean logout(int userId)
	{
		return this.client.logout(userId);
	}

	/**
	 * A buyer has launched an offer request.
	 *
	 * @param   int  userType  1 for buyer and 2 for seller.
	 * @param
	 */
	public CellTableModel activateService(int serviceId, int userId)
	{
		return this.client.activateService(serviceId, userId);
	}

	/**
	 * Update transfer. 
	 * @param serviceId
	 * @param userId
	 * @param progress
	 */
	public void updateTransfer(int serviceId, int userId, int progress)
	{
		this.gui.updateTransfer(serviceId, userId, progress);
	}

	/**
	 * Start transfer.
	 *  
	 * @param serviceId
	 * @param userId
	 * @param sellerId
	 */
	public void startTransfer(int serviceId, int buyerId, int sellerId)
	{
		this.network.startTransfer(serviceId, buyerId, sellerId);
	}

	public void newOnlineUser(int serviceId, int userId)
	{
		// Find out which row to update in the main table.
		MainTableModel mtm = (MainTableModel) ((GUI)this.gui).getTable().getModel();
		int row = mtm.findRowByServiceId(serviceId);


		// Get cell to update model.
		CellTableModel ct = (CellTableModel)((GUI)this.gui).getTable().getValueAt(row, 1);

		// Add buyer.
		int buyerId = new Random().nextInt(100);
		Object[] rowx = {buyerId, "buyer_name" + buyerId, "No Offer", 0};
		ct.addRow(rowx);

		ct.fireTableDataChanged();
		((GUI)this.gui).getTable().rebuildTable();
	}

	@Override
	public void registerGUI(IGUI gui) {
		this.gui = gui;
	}
	@Override
	public void registerNetwork(INetwork network) {
		this.network = network;
	}
	@Override
	public void registerWSClient(IWSClient client) {
		this.client = client;
	}

}
