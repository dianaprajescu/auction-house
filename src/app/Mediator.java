/**
 *
 */
package app;

import java.io.IOException;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import interfaces.IGUI;
import interfaces.IGUIMediator;
import interfaces.INetwork;
import interfaces.INetworkMediator;
import interfaces.IWSClient;
import interfaces.IWSClientMediator;
import GUI.components.CellTableModel;
import GUI.components.MainTableModel;
import Network.ServerNetwork;

/**
 * @author Stedy
 *
 */
public class Mediator implements IGUIMediator, INetworkMediator, IWSClientMediator {
	private IGUI gui;
	private INetwork network;
	private IWSClient client;

	static Logger log = Logger.getLogger(Mediator.class);

	@Override
	public int login(String username, String password, UserType type)
	{
		// Set file appender.
		Logger root = (Logger) Logger.getRootLogger();
		PatternLayout pl = new PatternLayout();
		pl.setConversionPattern("%-5p %l : %m%n");
		FileAppender appender;
		try {
			appender = new FileAppender(pl, "logs/" + username + ".log", false);
			root.addAppender(appender);
		} catch (IOException e) {
		}
			
		int loggedId = this.client.login(username, password, type);
		
		log.debug(loggedId);

		if (loggedId > 0)
		{
			this.network.login(loggedId, type);
		}

		return loggedId;
	}

	@Override
	public boolean logout(int userId)
	{
		boolean logout = this.client.logout(userId);

		if (logout)
		{
			this.network.logout(userId);
		}

		return logout;
	}


	@Override
	public CellTableModel launchOfferRequest(int serviceId, int userId)
	{
		return this.network.launchOfferRequest(serviceId, userId);
	}


	@Override
	public boolean dropOfferRequest(int serviceId, int userId)
	{
		return this.network.dropOfferRequest(serviceId, userId);
	}

	@Override
	public void requestDropped(int serviceId, int buyerId) {
		this.gui.requestDropped(serviceId, buyerId);
	}

	@Override
	public boolean acceptOffer(int serviceId, int buyerId, int sellerId)
	{
		return this.network.acceptOffer(serviceId, buyerId, sellerId);
	}

	@Override
	public boolean refuseOffer(int serviceId, int buyerId, int sellerId)
	{
		return this.network.refuseOffer(serviceId, buyerId, sellerId);
	}

	@Override
	public boolean makeOffer(int serviceId, int buyerId, int sellerId, int price)
	{
		return this.network.makeOffer(serviceId, buyerId, sellerId, price);
	}

	@Override
	public boolean removeOffer(int serviceId, int buyerId, int sellerId)
	{
		return this.network.removeOffer(serviceId, buyerId, sellerId);
	}

	@Override
	public void updateTransfer(int serviceId, int userId, int progress)
	{
		this.gui.updateTransfer(serviceId, userId, progress);
	}

	@Override
	public void offerRefused(int serviceId, int buyerId) {
		this.gui.offerRefused(serviceId, buyerId);
	}

	@Override
	public void offerAccepted(int serviceId, int buyerId) {
		this.gui.offerAccepted(serviceId, buyerId);
	}

	@Override
	public void offerExceeded(int serviceId, int buyerId, int price) {
		this.gui.offerExceeded(serviceId, buyerId, price);
	}

	@Override
	public void removeExceeded(int serviceId, int buyerId, int price) {
		this.gui.removeExceeded(serviceId, buyerId, price);
	}

	@Override
	public void offerMade(int serviceId, int sellerId, int price) {
		this.gui.offerMade(serviceId, sellerId, price);
	}

	@Override
	public void offerRemoved(int serviceId, int sellerId) {
		this.gui.offerRemoved(serviceId, sellerId);
	}

	@Override
	public void newUser(int serviceId, int userId) {
		this.gui.newUser(serviceId, userId, client.getUsername(userId));
	}

	@Override
	public void userLeft(int serviceId, int userId) {
		this.gui.userLeft(serviceId, userId);
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

	@Override
	public CellTableModel getUserList(int serviceId, UserType type) {
		return this.network.getUserList(serviceId, type);
	}

	@Override
	public MainTableModel getServiceList(int userId, UserType type) {
		MainTableModel mtm = this.client.getServiceList(userId, type);

		if (type == UserType.SELLER)
		{
			for (int i=0; i < mtm.getRowCount(); i++){
				this.network.registerService(mtm.getIdAt(i), userId);
			}
		}

		return mtm;
	}

	@Override
	public void transferFailed(int serviceId, int buyerId, int sellerId) {
		this.gui.transferFailed(serviceId, buyerId, sellerId);
	}
}
