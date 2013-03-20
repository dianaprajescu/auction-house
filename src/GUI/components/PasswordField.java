/**
 * 
 */
package GUI.components;

import interfaces.IGUIMediator;

import javax.swing.JPasswordField;

import GUI.GUIMediator;

/**
 * @author diana
 *
 */
public class PasswordField extends JPasswordField {
	private GUIMediator med;

	public PasswordField(GUIMediator med) {
		this.med = med;
		med.registerPassword(this);
	}
}
