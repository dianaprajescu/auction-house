package GUI.components;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JList;

import GUI.GUIMediator;

public class TableMouseListener extends MouseAdapter{
	
	private GUIMediator gui;

	public TableMouseListener(GUIMediator gui)
	{
		this.gui = gui;
	}
	
	public void mousePressed(MouseEvent e)  {check(e);}
	public void mouseReleased(MouseEvent e) {check(e);}
	
    private void check(MouseEvent e) {
    	if (e.isPopupTrigger()) { //if the event shows the menu
    		Table source = (Table) e.getSource();
            int row = source.rowAtPoint( e.getPoint() );
            int column = source.columnAtPoint( e.getPoint() );
            
            if (column == 0)
            {
            	source.changeSelection(row, column, false, false);
	            source.editCellAt(row, column);
	            
	            gui.showServicePopup(source, e.getX(), e.getY());
            }
            if (column == 1)
            {
            	source.changeSelection(row, column, false, false);
            	source.editCellAt(row, column);
            	
            	e.translatePoint(source.getLocation().x - source.getEditorComponent().getLocation().x, source.getLocation().y - source.getEditorComponent().getLocation().y);
            	
            	((JList)source.getEditorComponent()).dispatchEvent(e);
            }
        }
    }
	
}
