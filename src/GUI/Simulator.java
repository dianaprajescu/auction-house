/**
 *
 */
package GUI;

import java.util.List;
import java.util.Random;

import javax.swing.SwingWorker;

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
				//break;
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
		MockupNetwork mock = new MockupNetwork(med);
		mock.newOnlineUser(null, null);
		//System.out.println(chunks);
	}

	@Override
	public void done()
	{
		System.out.println("done");
	}

	/**
	 * Simulate login.
	 */
	private void simulateLogin()
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
			gui.GUImed.setPassword("lala");
		}

		// Set user type random.
		if (valid % 2 == 1)
		{
			gui.GUImed.setType(UserType.BUYER);
		}
		else
		{
			gui.GUImed.setType(UserType.SELLER);
		}

		gui.GUImed.login();
	}

	private void simulateLogout()
	{
		gui.GUImed.logout();
	}

}
