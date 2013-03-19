/**
 * 
 */
package GUI.components;

import interfaces.GUIMediator_interface;

import javax.swing.JPasswordField;

/**
 * @author diana
 *
 */
public class PasswordField extends JPasswordField {
	private GUIMediator_interface med;

	public PasswordField(GUIMediator_interface med) {
		this.med = med;
		med.registerPassword(this);
	}
}
