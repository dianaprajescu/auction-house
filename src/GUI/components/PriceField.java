/**
 *
 */
package GUI.components;

import javax.swing.JTextField;

import GUI.InternalGUIMediator;

/**
 * @author Stedy
 *
 */
public class PriceField extends JTextField {
	private InternalGUIMediator med;

	public PriceField(InternalGUIMediator med) {
		this.med = med;
		med.registerPrice(this);
	}
}
