package GUI.components;

import java.util.Timer;
import java.util.TimerTask;

import GUI.InternalGUIMediator;

public class AuctionTimer extends Timer {
	MainTableModel mtm;
	int row;
	int left;
	Timer timer;
	InternalGUIMediator mediator;

	public AuctionTimer(InternalGUIMediator med, MainTableModel model, int r)
	{
		this.mtm = model;
		this.row = r;
		this.mediator = med;
		left = 35;

		timer = this;

		this.schedule(new TimerTask(){

			@Override
			public void run() {
				if (left > 0)
				{
					mtm.setTimerAt((new Integer(left--)).toString() + " seconds", row);
					mtm.fireTableCellUpdated(row, 3);
				}
				else
				{
					mediator.auctionExpired(row);
					timer.purge();
				}
			}

		}, 0, 1000);
	}
}
