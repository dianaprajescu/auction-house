/**
 *
 */
package GUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

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
					// Buyer can accept or reject an offer.
					if (simulateAcceptRejectOffer() == -1)
					{
						// Buyer launches or drop offer requests.
						if (simulateLaunchDropOffer() == -1)
						{
							// User type was changed manually from GUI.
							continue;
						}
					}
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

		if (gui.isActive() == false)
		{
			gui.GUImed.login();
		}
	}

	/**
	 * Simulate logout.
	 * @throws InterruptedException
	 */
	private void simulateLogout() throws InterruptedException
	{
		boolean transfer = true;
		int i, j;

		while (transfer)
		{
			transfer = false;

			// Check if transfer in progress.
			for (i = 0; i < gui.getTable().getRowCount(); i++)
			{
				CellTableModel ctm = (CellTableModel) gui.getTable().getValueAt(i, 1);

				for (j = 0; j < ctm.getRowCount(); j++)
				{
					System.out.println(ctm.getValueAt(j, 1));
					if (((String) ctm.getValueAt(j, 1)).compareToIgnoreCase("transfer in progress") == 0)
					{
						System.out.println("dfgfdg");
						transfer = true;

						// Transfer may take a while. Sleep.
						Thread.sleep(5 * DELAY);
						break;
					}
				}

				if (transfer)
				{
					break;
				}
			}
		}

		gui.GUImed.logout();
	}

	/**
	 * Simulate launch offer.
	 * @throws InterruptedException
	 */
	private int simulateLaunchDropOffer() throws InterruptedException
	{
		// Do everything random.
		Random rand = new Random();

		// How many services to activate?
		int noServices = rand.nextInt(gui.getTable().getRowCount() / 2) + 1;

		int i = 0;
		while (i < noServices)
		{
			Thread.sleep(5 * DELAY);

			if (gui.isActive() == true && gui.GUImed.getType() == UserType.BUYER)
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
				return -1;
			}
		}

		return 1;
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
		Thread.sleep(5 * DELAY);
	}

	/**
	 * Simulate accept or refect offer.
	 *
	 * @return  int  Returns -1 if there are no offers made.
	 *
	 * @throws InterruptedException
	 */
	private int simulateAcceptRejectOffer() throws InterruptedException
	{
		Random rand = new Random();

		// Get services with offers made by sellers.
		HashMap<Integer, ArrayList<Integer>> offers = new HashMap<Integer, ArrayList<Integer>>();
		int i, j;

		// All services.
		for (i = 0; i < gui.getTable().getRowCount(); i++)
		{
			if (((String) gui.getTable().getValueAt(i, 2)).compareToIgnoreCase("active") == 0)
			{
				// Get sellers.
				CellTableModel ctm = (CellTableModel) gui.getTable().getValueAt(i, 1);
				for (j = 0; j < ctm.getRowCount(); j++)
				{
					if (((String) ctm.getValueAt(j, 1)).compareToIgnoreCase("offer made") == 0)
					{
						ArrayList<Integer> sellers = new ArrayList<Integer>();
						sellers.add(j);
						offers.put(i, sellers);
					}
				}
			}
		}

		// No offers were made.
		if (offers.size() == 0)
		{
			return -1;
		}

		Set<Entry<Integer, ArrayList<Integer>>> entries = offers.entrySet();

		// Accept or reject some offers.
		for (Entry<Integer, ArrayList<Integer>> entry : entries)
		{
			Thread.sleep(5 * DELAY);

			// Check if ctm has been modified manually in GUI.
			CellTableModel ctm = (CellTableModel) gui.getTable().getValueAt(entry.getKey(), 1);
			ArrayList<Integer> sellers = entry.getValue();
			for (j = 0; j < ctm.getRowCount(); j++)
			{
				if (sellers.contains(j) && ((String) ctm.getValueAt(j, 1)).compareToIgnoreCase("offer made") != 0)
				{
					sellers.remove(j);
				}
			}
			entry.setValue(sellers);

			if (entry.getValue().size() == 0)
			{
				continue;
			}

			int cellRow = rand.nextInt(entry.getValue().size());
			cellRow = entry.getValue().get(cellRow);
			int accept = rand.nextInt(2);

			if (accept % 2 == 1)
			{
				// Accept offer.
				gui.GUImed.userAction("accept offer", entry.getKey(), cellRow);
			}
			else
			{
				// Refuse offer.
				gui.GUImed.userAction("refuse offer", entry.getKey(), cellRow);
			}
		}

		Thread.sleep(5 * DELAY);
		return 1;
	}

}
