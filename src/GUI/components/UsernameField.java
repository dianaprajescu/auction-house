/**
 * 
 */
package GUI.components;

import interfaces.IGUIMediator;

import javax.swing.JTextField;

import GUI.GUIMediator;

/**
 * @author diana
 *
 */
public class UsernameField extends JTextField {
	private GUIMediator med;

	public UsernameField(GUIMediator med) {
		this.med = med;
		med.registerUsername(this);
	}
}
