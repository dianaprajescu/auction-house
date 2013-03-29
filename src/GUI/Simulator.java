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
import GUI.components.MainTableModel;
import Network.MockupNetwork;
import app.Mediator;
import app.UserType;

/**
 * @author Stedy
 *
 */
public class Simulator extends SwingWorker<Integer, Integer> {
	private static final int DELAY = 1000;
	private GUI gui;
	private MockupNetwork network;
	private Mediator med;

	public Simulator (Mediator med, GUI gui, MockupNetwork network)
	{
		this.med = med;
		this.gui = gui;
		this.network = network;
	}

	/* (non-Javadoc)
	 * @see javax.swing.SwingWorker#doInBackground()
	 */
	@Override
	protected Integer doInBackground() throws Exception {
		while (true)
		{
			try
			{
				Thread.sleep(DELAY);

				if (gui.isActive() == true)
				{
					if (gui.GUImed.getType() == UserType.BUYER)
					{
						// No offers were made. Make some offers.
						if (simulateAcceptRejectOffer() == -1)
						{
							// Buyer can accept or reject an offer.
							if (simulateOfferReceived() == -1)
							{
								// No active services. Buyer launches or drop offer requests.
								if (simulateLaunchDropOffer() == -1)
								{
									// User type was changed manually from GUI.
									continue;
								}
							}

							continue;
						}
					}
					else
					{
						// Seller makes offer.
						simulateMakeOffer();

						// Offer was exceeded.
						simulateOfferExceeded();
					}

					Thread.sleep(DELAY);
					simulateLogout();
				}
				else
				{
					simulateLogin();
				}
			}
			catch(Exception e)
			{

			}
		}

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
					if (((String) ctm.getValueAt(j, 1)).compareToIgnoreCase("transfer in progress") == 0)
					{
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
			Thread.sleep(3 * DELAY);

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
		Random rand = new Random();

		// Get active services.
		ArrayList<Integer> mainRows = new ArrayList<Integer>();
		int i;

		for (i = 0; i < gui.getTable().getRowCount(); i++)
		{
			if (((String) gui.getTable().getValueAt(i, 2)).compareToIgnoreCase("active") == 0)
			{
				mainRows.add(i);
			}
		}

		// No active services.
		if (mainRows.size() == 0)
		{
			// No offer to make.
			return;
		}

		// Generate random no of offers.
		int noOffers = rand.nextInt(mainRows.size() - 1) + 1;

		for (i = 0; i < noOffers; i++)
		{
			// Select random active service.
			int service = mainRows.get(rand.nextInt(mainRows.size()));

			// Select random cell.
			CellTableModel ctm = (CellTableModel) gui.getTable().getValueAt(service, 1);
			int cellRow = rand.nextInt(ctm.getRowCount());

			if (((String) ctm.getValueAt(cellRow, 1)).compareToIgnoreCase("offer made") != 0)
			{
				// Make offer.
				//gui.GUImed.userAction("make offer", service, cellRow);

				gui.GUImed.setPrice(rand.nextInt(150) + 1);

				//gui.GUImed.pdb.repaint();
				gui.GUImed.submitPrice(ctm, ((MainTableModel)gui.getTable().getModel()).getIdAt(service), ctm.getIdAt(cellRow));

				Thread.sleep(2 * DELAY);
			}
		}
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

	/**
	 * Simulate offer exceeded.
	 *
	 * @throws InterruptedException
	 */
	private void simulateOfferExceeded() throws InterruptedException
	{
		int i, j;
		Random rand = new Random();

		// Get all offers made.
		for (i = 0; i < gui.getTable().getRowCount(); i++)
		{
			// Get buyers.
			CellTableModel ctm = (CellTableModel) gui.getTable().getValueAt(i, 1);
			for (j = 0; j < ctm.getRowCount(); j++)
			{
				if (((String) ctm.getValueAt(j, 1)).compareToIgnoreCase("offer made") == 0)
				{
					// Get current price.
					int price = (Integer) ctm.getValueAt(j, 2);
					price = price - rand.nextInt(10) - 10;

					// Random offers are exceeded.
					int exceed = rand.nextInt(2);
					if (exceed % 2 == 1)
					{
						network.offerExceeded(((MainTableModel) gui.getTable().getModel()).getIdAt(i), ctm.getIdAt(j), price);
						Thread.sleep(3 * DELAY);
					}
				}
			}
		}
	}

	private int simulateOfferReceived() throws InterruptedException
	{
		Random rand = new Random();

		// Get active services.
		ArrayList<Integer> mainRows = new ArrayList<Integer>();
		int i;

		for (i = 0; i < gui.getTable().getRowCount(); i++)
		{
			if (((String) gui.getTable().getValueAt(i, 2)).compareToIgnoreCase("active") == 0)
			{
				mainRows.add(i);
			}
		}

		// No active services.
		if (mainRows.size() == 0)
		{
			return -1;
		}

		// Generate random no of offers.
		int noOffers = rand.nextInt(mainRows.size() - 1) + 1;

		for (i = 0; i < noOffers; i++)
		{
			// Select random active service.
			int service = mainRows.get(rand.nextInt(mainRows.size()));

			// Select random cell.
			CellTableModel ctm = (CellTableModel) gui.getTable().getValueAt(service, 1);
			int cellRow = rand.nextInt(ctm.getRowCount());

			if (((String) ctm.getValueAt(cellRow, 1)).compareToIgnoreCase("no offer") == 0)
			{
				// Make offer.
				int price = rand.nextInt(200) + 1;
				network.offerMade(((MainTableModel) gui.getTable().getModel()).getIdAt(service), ctm.getIdAt(cellRow), price);

				Thread.sleep(2 * DELAY);
			}
		}

		return 1;
	}

}
