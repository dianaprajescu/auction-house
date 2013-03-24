/**
 *
 */
package GUI;

import java.util.List;

import javax.swing.SwingWorker;

import Network.MockupNetwork;

/**
 * @author Stedy
 *
 */
public class Simulator extends SwingWorker<Integer, Integer> {

	private final GUI gui;

	public Simulator (GUI gui)
	{
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
		MockupNetwork mock = new MockupNetwork();
		mock.newOnlineSeller();
		//System.out.println(chunks);
	}

	@Override
	public void done()
	{
		System.out.println("done");
	}

}
