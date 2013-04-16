/**
 * 
 */
package GUI.components;

import java.awt.event.ActionListener;

import GUI.InternalGUIMediator;
import app.Command;
import interfaces.IGUIMediator;

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
	
	InternalGUIMediator med;
	 
	/**
	 * Constructor.
	 * 
	 * @param   ActionListener         al  The action listener for the button.
	 * @param   IGUIMediator  m   The gui mediator.
	 */
    public LoginButton (ActionListener al, InternalGUIMediator m) {
        super("Login");
        addActionListener(al);
        med = m;
    }
 
    /**
     * Execute the command.
     */
    public void execute(String command) {
        med.login();
    }

}
