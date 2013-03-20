/**
 * 
 */
package app;

import java.awt.HeadlessException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


import GUI.GUI;
import GUI.Login;
import GUI.components.LoginButton;
import GUI.components.LogoutButton;
import GUI.components.PasswordField;
import GUI.components.BuyerType;
import GUI.components.SellerType;
import GUI.components.UsernameField;
import interfaces.IGUI;
import interfaces.IGUIMediator;
import interfaces.INetwork;
import interfaces.INetworkMediator;
import interfaces.IWSClient;
import interfaces.IWSClientMediator;

/**
 * @author diana
 *
 */
public class Mediator implements IGUIMediator, INetworkMediator, IWSClientMediator {
	private IGUI gui;
	private INetwork netwrok;
	private IWSClient client;
	
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
