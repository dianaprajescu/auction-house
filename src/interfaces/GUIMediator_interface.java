package interfaces;

import javax.swing.JTextField;

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
public interface GUIMediator_interface {
	public void login();
	public void logout();
	public void registerLogin(Login log);
	public void registerGUI(GUI gui);
	public void registerUsername(UsernameField username);
	public void registerPassword(PasswordField password);
	public void registerBuyerType(BuyerType buyer);
	public void registerSellerType(SellerType seller);
}
