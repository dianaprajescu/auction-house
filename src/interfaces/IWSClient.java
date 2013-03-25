package interfaces;

import GUI.components.CellTableModel;
import app.Mediator;
import app.UserType;

/**
 * @author Stedy
 *
 */
public interface IWSClient {
	
	public int login(String username, String password, UserType type);
	
	public boolean logout(int userId);
	
	public CellTableModel launchOfferRequest(int serviceId, int userId);
}
