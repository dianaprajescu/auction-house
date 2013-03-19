/**
 * 
 */
package GUI.components;

import interfaces.GUIMediator_interface;

import javax.swing.JCheckBox;
import javax.swing.JRadioButton;

/**
 * @author diana
 *
 */
public class SellerType extends JRadioButton {
	/**
	 * Serializable.
	 */
	private static final long serialVersionUID = -2758630814478717141L;
	
	private GUIMediator_interface med;

	public SellerType(GUIMediator_interface med) {
		super("Seller");
		this.med = med;
		med.registerSellerType(this);
	}
}
