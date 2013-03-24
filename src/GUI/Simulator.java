/**
 *
 */
package GUI;

import java.util.List;

import javax.swing.SwingWorker;

import Network.MockupNetwork;
import app.Mediator;

/**
 * @author Stedy
 *
 */
public class Simulator extends SwingWorker<Integer, Integer> {

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
			// System.out.println("aaa");
			if (gui.isActive() == true)
			{
				System.out.println("dfsdfsdfsdf");
				break;
			}
		}
		System.out.println("dfsdfsdfsdf");

		int i = 0;
		while (i < 10)
		{
			i++;
			System.out.println("in while");
			if (i % 2 == 0)
			{
				System.out.println("in if");
				publish(i);
			}
		}

		return new Integer(0);
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

}
