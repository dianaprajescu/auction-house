package GUI.components;

import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import GUI.GUIMediator;
import app.Command;

public class PopupMenuItem extends JMenuItem implements Command{
	/**
	 * Serializable.
	 */
	private static final long serialVersionUID = -8507498325595636424L;

	private GUIMediator med;

	/**
	 * The selected row in table.
	 */
	private int row;

	public PopupMenuItem(String option, GUIMediator med, ActionListener al, int row)
	{
		super(option);
		this.addActionListener(al);
		this.med = med;
		this.row = row;
	}

	@Override
	public void execute(String command) {
		this.med.userAction(command, row);
	}

}
