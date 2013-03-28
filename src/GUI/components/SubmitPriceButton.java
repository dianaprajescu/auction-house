/**
 *
 */
package GUI.components;

import java.awt.event.ActionListener;

import javax.swing.JButton;

import GUI.InternalGUIMediator;
import app.Command;

/**
 * @author Stedy
 *
 */
public class SubmitPriceButton extends JButton implements Command {
	/**
	 *
	 */
	private static final long serialVersionUID = -1051258787285549110L;

	private InternalGUIMediator med;
	private CellTableModel ctm;
	private int serviceId;
	private int buyerId;

	/**
	 * Constructor.
	 *
	 * @param   ActionListener         al  The action listener for the button.
	 * @param   IGUIMediator  m   The gui mediator.
	 */
    public SubmitPriceButton (ActionListener al, InternalGUIMediator m, CellTableModel ctm, int serviceId, int buyerId) {
        super("Submit");
        addActionListener(al);
        med = m;
        this.ctm = ctm;
        this.serviceId = serviceId;
        this.buyerId = buyerId;
    }

	@Override
	public void execute(String command) {
		med.submitPrice(ctm, serviceId, buyerId);
	}

}
