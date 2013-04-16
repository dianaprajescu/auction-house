package Network;

import java.util.List;

import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

public class TransferTask extends SwingWorker<Integer, Integer> {
	private static final int DELAY = 1000;	
	private int count;
	
	public TransferTask(int count) {
		this.count = count;
	}

	protected Integer doInBackground() throws Exception {
		int i = 0;
		
		while (i++ < count) {
			Thread.sleep(DELAY);
			setProgress(i*100/count);
		}
		
		setProgress(100);
  
		return new Integer(0);
	}

	protected void done() {
	}
}