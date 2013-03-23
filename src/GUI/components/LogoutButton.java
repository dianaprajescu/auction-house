/**
 * 
 */
package GUI.components;

import interfaces.IGUIMediator;

import java.awt.event.ActionListener;

import javax.swing.JButton;

import GUI.GUIMediator;
import app.Command;

/**
 * @author Stedy
 *
 */
public class LogoutButton extends JButton implements Command {
	/**
	 * Serializable class.
	 */
	private static final long serialVersionUID = 1518422999515655012L;
	
	GUIMediator med;
	
	/**
	 * Constructor.
	 * 
	 * @param   ActionListener         al  The action listener for the button.
	 * @param   IGUIMediator  m   The gui mediator.
	 */
	public LogoutButton (ActionListener al, GUIMediator m) {
        super("Logout");
        addActionListener(al);
        med = m;
    }

	/**
	 * Execute command.
	 */
	public void execute(String command) {
		med.logout();
	}

}
