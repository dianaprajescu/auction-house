package GUI.components;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import GUI.GUIMediator;

public class ListMouseListener extends MouseAdapter{

	JList jList;
	GUIMediator gui;
	
	public ListMouseListener(JList jList, GUIMediator gui)
	{
		this.jList = jList;
		this.gui = gui;
	}
	
	public void mousePressed(MouseEvent e)  {check(e);}
	public void mouseReleased(MouseEvent e) {check(e);}
	
    private void check(MouseEvent e) {
    	if (e.isPopupTrigger()) { //if the event shows the menu
            jList.setSelectedIndex(jList.locationToIndex(e.getPoint())); //select the item
            
            gui.showListPopup(jList, e.getX(), e.getY());
        }
    }
	
}
