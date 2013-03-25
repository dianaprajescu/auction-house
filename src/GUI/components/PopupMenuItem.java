package GUI.components;

import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import GUI.InternalGUIMediator;
import app.Command;

public class PopupMenuItem extends JMenuItem implements Command{
	/**
	 * Serializable.
	 */
	private static final long serialVersionUID = -8507498325595636424L;

	private InternalGUIMediator med;

	/**
	 * The selected row in table.
	 */
	private int mainRow;
	private int cellRow;

	public PopupMenuItem(String option, InternalGUIMediator med, ActionListener al, int mainRow, int cellRow)
	{
		super(option);
		this.addActionListener(al);
		this.med = med;
		this.mainRow = mainRow;
		this.cellRow = cellRow;
	}

	@Override
	public void execute(String command) {
		this.med.userAction(command, mainRow, cellRow);
	}

}
