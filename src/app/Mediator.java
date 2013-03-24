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
import GUI.GUI;

/**
 * @author diana
 *
 */
public class Mediator implements IGUIMediator, INetworkMediator, IWSClientMediator {
	private IGUI gui;
	private INetwork netwrok;
	private IWSClient client;

	public void newOnlineSeller(String seller, int buyer_id, int service_id)
	{
		// TODO add this seller to the list in gui.
		String status = (String) ((GUI) gui).getTable().getModel().getValueAt(((GUI) gui).getTable().getSelectedRow(), 2);
		System.out.println("sdfsdf " + status);
	}

	public void registerGUI(IGUI gui) {
		this.gui = gui;
	}
	public void registerNetwork(INetwork network) {
		this.netwrok = network;
	}
	public void registerWSClient(IWSClient client) {
		this.client = client;
	}

}
