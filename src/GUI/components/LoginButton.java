/**
 * 
 */
package GUI.components;

import java.awt.event.ActionListener;

import app.Command;
import interfaces.GUIMediator_interface;

import javax.swing.JButton;

/**
 * @author Stedy
 *
 */
public class LoginButton extends JButton implements Command {
	/**
	 * Serializable class.
	 */
	private static final long serialVersionUID = -2707882266284287580L;
	
	GUIMediator_interface med;
	 
	/**
	 * Constructor.
	 * 
	 * @param   ActionListener         al  The action listener for the button.
	 * @param   GUIMediator_interface  m   The gui mediator.
	 */
    public LoginButton (ActionListener al, GUIMediator_interface m) {
        super("Login");
        addActionListener(al);
        med = m;
    }
 
    /**
     * Execute the command.
     */
    public void execute() {
        med.login();
    }

}
