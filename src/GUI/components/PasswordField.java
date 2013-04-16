/**
 * 
 */
package GUI.components;

import interfaces.IGUIMediator;

import javax.swing.JPasswordField;

import GUI.InternalGUIMediator;

/**
 * @author diana
 *
 */
public class PasswordField extends JPasswordField {
	private InternalGUIMediator med;

	public PasswordField(InternalGUIMediator med) {
		this.med = med;
		med.registerPassword(this);
	}
}
