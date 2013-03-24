package GUI.components;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JList;
import javax.swing.JTable;

import GUI.GUIMediator;

public class CellTableMouseListener extends MouseAdapter{
	
	private GUIMediator gui;

	public CellTableMouseListener(GUIMediator gui)
	{
		this.gui = gui;
	}
	
	public void mousePressed(MouseEvent e)  {check(e);}
	public void mouseReleased(MouseEvent e) {check(e);}
	
    private void check(MouseEvent e) {
    	if (e.isPopupTrigger()) { //if the event shows the menu
    		JTable source = (JTable) e.getSource();
            int row = source.rowAtPoint( e.getPoint() );
            int column = source.columnAtPoint( e.getPoint() );
            
        	source.changeSelection(row, column, false, false);
            source.editCellAt(row, column);
            
            gui.showTablePopup(source, e.getX(), e.getY());
        }
    }
	
}