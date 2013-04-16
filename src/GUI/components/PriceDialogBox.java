/**
 *
 */
package GUI.components;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;

import GUI.InternalGUIMediator;

/**
 * @author Stedy
 *
 */
public class PriceDialogBox extends JDialog {
	private ActionListener al;
	private InternalGUIMediator med;
	PriceField price;
	JButton offer;
	CellTableModel ctm;
	int serviceId;
	int buyerId;

	/**
	 * Contructor.
	 *
	 * @param ctm
	 * @param serviceId
	 * @param buyerId
	 * @param al
	 * @param med
	 */
	public PriceDialogBox(CellTableModel ctm, int serviceId, int buyerId, ActionListener al, InternalGUIMediator med)
	{
		super();

		this.ctm = ctm;
		this.serviceId = serviceId;
		this.buyerId = buyerId;
		this.al = al;
		this.med = med;

		this.init();
	}

	public void init()
	{
		this.setTitle("Make Offer");
		this.setModal(true);

		this.getContentPane().setLayout(new FlowLayout());

		price = new PriceField(med);
		price.setPreferredSize(new Dimension(150, 25));
		this.add(price);

		offer = new SubmitPriceButton(al, med, ctm, serviceId, buyerId);
		offer.setPreferredSize(new Dimension(75, 25));
		this.add(offer);

		this.pack();
	}
}
