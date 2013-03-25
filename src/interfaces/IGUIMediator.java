package interfaces;

import javax.swing.JTextField;

import app.UserType;

import GUI.GUI;
import GUI.Login;
import GUI.components.LoginButton;
import GUI.components.LogoutButton;
import GUI.components.PasswordField;
import GUI.components.BuyerType;
import GUI.components.SellerType;
import GUI.components.UsernameField;

/**
 * @author Stedy
 *
 */
public interface IGUIMediator {
	public void registerNetwork(INetwork network);
	public void registerWSClient(IWSClient client);
	
	/**
	 * In GUIMediator. Login an user.
	 * 
	 * @param username
	 * @param password
	 * @param type
	 */
	public int login(String username, String password, UserType type);
	
	/**
	 * Logout user with the ID passed in input.
	 * @param userId
	 * @return
	 */
	public boolean logout(int userId);
}
