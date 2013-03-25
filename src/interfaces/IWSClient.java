package interfaces;

import app.Mediator;
import app.UserType;

/**
 * @author Stedy
 *
 */
public interface IWSClient {
	
	public int login(String username, String password, UserType type);
}
