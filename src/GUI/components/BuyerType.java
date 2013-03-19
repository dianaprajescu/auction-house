/**
 * 
 */
package GUI.components;

import java.awt.GridLayout;
import java.awt.event.KeyEvent;

import interfaces.GUIMediator_interface;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * @author diana
 *
 */
public class BuyerType extends JRadioButton {
	/**
	 * Serializable
	 */
	private static final long serialVersionUID = 1208827009188534789L;
	
	private GUIMediator_interface med;

	public BuyerType(GUIMediator_interface med) {
		super("Buyer");
		this.setActionCommand("Buyer");
		this.med = med;
		med.registerBuyerType(this);
	}
}
