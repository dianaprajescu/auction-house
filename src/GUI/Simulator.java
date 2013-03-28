/**
 *
 */
package GUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.SwingWorker;

import GUI.components.CellTableModel;
import app.Mediator;
import app.UserType;

/**
 * @author Stedy
 *
 */
public class Simulator extends SwingWorker<Integer, Integer> {
	private static final int DELAY = 1000;
	private GUI gui;
	private Mediator med;

	public Simulator (Mediator med, GUI gui)
	{
		this.med = med;
		this.gui = gui;
	}

	/* (non-Javadoc)
	 * @see javax.swing.SwingWorker#doInBackground()
	 */
	@Override
	protected Integer doInBackground() throws Exception {
		while (true)
		{
			Thread.sleep(DELAY);

			if (gui.isActive() == true)
			{
				if (gui.GUImed.getType() == UserType.BUYER)
				{
					// Buyer launches or drop offer requests.
					simulateLaunchDropOffer();
				}
				else
				{
					// Seller makes offer.
					simulateMakeOffer();
				}

				Thread.sleep(DELAY);
				simulateLogout();
			}
			else
			{
				simulateLogin();
			}
		}

		/*int i = 0;
		while (i < 10)
		{
			Thread.sleep(DELAY);
			i++;
			System.out.println("in while");
			if (i % 2 == 0)
			{
				System.out.println("in if");
				publish(i);
			}
		}*/

		//return new Integer(0);
	}

	@Override
	protected void process(List<Integer> chunks) {
		// TODO 3.3 - print values received
		//MockupNetwork mock = new MockupNetwork(med);
		//mock.newOnlineUser(null, null);
		//System.out.println(chunks);
	}

	@Override
	public void done()
	{
		System.out.println("done");
	}

	/**
	 * Simulate login.
	 * @throws InterruptedException
	 */
	private void simulateLogin() throws InterruptedException
	{
		// Set valid or invalid login data.
		Random rand = new Random();
		int valid = rand.nextInt(2);

		if (valid % 2 == 1)
		{
			gui.GUImed.setUsername("diana");
			gui.GUImed.setPassword("test");
		}
		else
		{
			gui.GUImed.setUsername("diana");
			gui.GUImed.setPassword("la");
		}

		// Set user type random.
		int type = rand.nextInt(2);
		if (type % 2 == 1)
		{
			gui.GUImed.setType(UserType.BUYER);
		}
		else
		{
			gui.GUImed.setType(UserType.SELLER);
		}

		// Show entered data.
		gui.GUImed.login.repaint();
		Thread.sleep(DELAY);

		gui.GUImed.login();
	}

	/**
	 * Simulate logout.
	 */
	private void simulateLogout()
	{
		gui.GUImed.logout();
	}

	/**
	 * Simulate launch offer.
	 * @throws InterruptedException
	 */
	private void simulateLaunchDropOffer() throws InterruptedException
	{
		// Do everything random.
		Random rand = new Random();

		// How many services to activate?
		int noServices = rand.nextInt(gui.getTable().getRowCount()-1) + 1;

		int i = 0;
		while (i < noServices)
		{
			Thread.sleep(5 * DELAY);

			if (gui.isActive() == true)
			{
				// Select random service.
				int service = rand.nextInt(gui.getTable().getRowCount());

				// Launch or drop offer.
				if (((String) gui.getTable().getValueAt(service, 2)).compareToIgnoreCase("inactive") == 0)
				{
					gui.GUImed.userAction("launch offer request", service, 0);
				}
				else
				{
					gui.GUImed.userAction("drop offer request", service, 0);
				}
				i++;
			}
			else
			{
				return;
			}
		}
	}

	/**
	 * Simulate make offer.
	 *
	 * @throws InterruptedException
	 */
	private void simulateMakeOffer() throws InterruptedException
	{
		// Select random active service.
		Random rand = new Random();
		ArrayList<Integer> mainRows = new ArrayList<Integer>();
		int i;

		for (i = 0; i < gui.getTable().getRowCount(); i++)
		{
			if (((String) gui.getTable().getValueAt(i, 2)).compareToIgnoreCase("active") == 0)
			{
				mainRows.add(i);
			}
		}

		if (mainRows.size() == 0)
		{
			// No offer to make.
			return;
		}

		int service = mainRows.get(rand.nextInt(mainRows.size()));

		// Select random cell.
		CellTableModel ctm = (CellTableModel) gui.getTable().getValueAt(service, 1);
		int cellRow = rand.nextInt(ctm.getRowCount());

		// Make offer.
		gui.GUImed.userAction("make offer", service, cellRow);
		Thread.sleep(2 * DELAY);
	}

}
