/**
 * 
 */
package GUI.components;

import java.awt.GridLayout;
import java.awt.event.KeyEvent;

import interfaces.IGUIMediator;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import GUI.GUIMediator;

/**
 * @author diana
 *
 */
public class BuyerType extends JRadioButton {
	/**
	 * Serializable
	 */
	private static final long serialVersionUID = 1208827009188534789L;
	
	private GUIMediator med;

	public BuyerType(GUIMediator med) {
		super("Buyer");
		this.setActionCommand("Buyer");
		this.med = med;
		med.registerBuyerType(this);
	}
}
