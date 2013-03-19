/**
 * 
 */
package GUI.components;

import interfaces.GUIMediator_interface;

import java.awt.event.ActionListener;

import javax.swing.JButton;

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
	
	GUIMediator_interface med;
	
	/**
	 * Constructor.
	 * 
	 * @param   ActionListener         al  The action listener for the button.
	 * @param   GUIMediator_interface  m   The gui mediator.
	 */
	public LogoutButton (ActionListener al, GUIMediator_interface m) {
        super("Logout");
        addActionListener(al);
        med = m;
    }

	/**
	 * Execute command.
	 */
	public void execute() {
		med.logout();
	}

}
