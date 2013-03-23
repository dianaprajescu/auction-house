package GUI.components;

import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import GUI.GUIMediator;
import app.Command;

public class PopupMenuItem extends JMenuItem implements Command{

	private GUIMediator med;

	public PopupMenuItem(String option, GUIMediator med, ActionListener al)
	{
		super(option);
		this.addActionListener(al);
		this.med = med;
	}
	
	@Override
	public void execute(String command) {
		// TODO Auto-generated method stub
		this.med.userAction(command);
	}
	
}
