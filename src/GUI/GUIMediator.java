/**
 * 
 */
package GUI;

import javax.swing.JOptionPane;

import GUI.components.LoginButton;
import GUI.components.LogoutButton;
import interfaces.GUIMediator_interface;

/**
 * @author diana
 *
 */
public class GUIMediator implements GUIMediator_interface {
	
	private LoginButton loginb;
	private LogoutButton logoutb;

	public void login() {
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(null, "Login!");
	}

	public void logout() {
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(null, "Logout!");
	}

	public void registerLogin(LoginButton lb) {
		this.loginb = lb;
		
	}

	public void registerLogout(LogoutButton lb) {
		this.logoutb = lb;
	}
}
