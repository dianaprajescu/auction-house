/**
 * 
 */
package GUI.components;

import interfaces.IGUIMediator;

import javax.swing.JCheckBox;
import javax.swing.JRadioButton;

import GUI.InternalGUIMediator;

/**
 * @author diana
 *
 */
public class SellerType extends JRadioButton {
	/**
	 * Serializable.
	 */
	private static final long serialVersionUID = -2758630814478717141L;
	
	private InternalGUIMediator med;

	public SellerType(InternalGUIMediator med) {
		super("Seller");
		this.med = med;
		med.registerSellerType(this);
	}
}
