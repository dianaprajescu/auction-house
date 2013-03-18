package interfaces;

import GUI.components.LoginButton;
import GUI.components.LogoutButton;

/**
 * @author Stedy
 *
 */
public interface GUIMediator_interface {
	public void login();
	public void logout();
	public void registerLogin(LoginButton lb);
	public void registerLogout(LogoutButton lb);
}
