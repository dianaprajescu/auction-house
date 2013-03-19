/**
 * 
 */
package GUI.components;

import interfaces.GUIMediator_interface;

import javax.swing.JTextField;

/**
 * @author diana
 *
 */
public class UsernameField extends JTextField {
	private GUIMediator_interface med;

	public UsernameField(GUIMediator_interface med) {
		this.med = med;
		med.registerUsername(this);
	}
}
