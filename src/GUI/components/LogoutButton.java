/**
 * 
 */
package GUI.components;

import interfaces.IGUIMediator;

import java.awt.event.ActionListener;

import javax.swing.JButton;

import GUI.InternalGUIMediator;
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
	
	InternalGUIMediator med;
	
	/**
	 * Constructor.
	 * 
	 * @param   ActionListener         al  The action listener for the button.
	 * @param   IGUIMediator  m   The gui mediator.
	 */
	public LogoutButton (ActionListener al, InternalGUIMediator m) {
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
