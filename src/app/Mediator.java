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

import java.awt.Component;

import GUI.GUI;
import GUI.components.CellTableModel;
import GUI.components.MainTable;
import GUI.components.MainTableModel;
import Network.MockupNetwork;

/**
 * @author diana
 *
 */
public class Mediator implements IGUIMediator, INetworkMediator, IWSClientMediator {
	private IGUI gui;
	private INetwork network;
	private IWSClient client;

	public void newOnlineSeller(String seller, int buyer_id, int service_id)
	{
		/*
		// TODO add this seller to the list in gui.
		CellTableModel ct = (CellTableModel)((GUI)this.gui).getTable().getValueAt(1, 1);
		Object[] rowx = {10, seller, "No Offer", ""};
		ct.addRow(rowx);
		//ct.setIdAt(10, ct.getRowCount() - 1);

		//ct.fireTableDataChanged();
		((MainTableModel)((GUI)this.gui).getTable().getModel()).fireTableDataChanged();

		MainTable table = ((GUI)this.gui).getTable();

		try {
		    for (int row=0; row<table.getRowCount(); row++) {
		        int rowHeight = table.getRowHeight();

		        for (int column=0; column<table.getColumnCount(); column++) {
		            Component comp = table.prepareRenderer(table.getCellRenderer(row, column), row, column);
		            rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
		        }

		        table.setRowHeight(row, rowHeight);
		    }
		} catch(ClassCastException e) { }

		*/
		//System.out.println("sdfsdf " + status);
	}
	
	public void updateTransfer(int serviceId, int userId, int progress)
	{
		MainTable table = ((GUI)this.gui).getTable();
		MainTableModel mtm = (MainTableModel) table.getModel();
		
		int serviceRow = mtm.findRowByServiceId(serviceId);
		if (serviceRow >= 0)
		{
			CellTableModel ctm = (CellTableModel) table.getValueAt(serviceRow, 1);
			int userRow = ctm.findRowByUserId(userId);
			
			ctm.setValueAt(progress, userRow, 2);
			
			ctm.fireTableDataChanged();
			table.rebuildTable();
		}
	}
	
	public void startTransfer(int serviceId, int userId)
	{
		((MockupNetwork)this.network).initTransfer(serviceId, userId);
	}

	public void registerGUI(IGUI gui) {
		this.gui = gui;
	}
	public void registerNetwork(INetwork network) {
		this.network = network;
	}
	public void registerWSClient(IWSClient client) {
		this.client = client;
	}

}
