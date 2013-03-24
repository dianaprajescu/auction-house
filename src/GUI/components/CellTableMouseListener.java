package GUI.components;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import GUI.GUIMediator;

public class CellTableMouseListener extends MouseAdapter{

	private GUIMediator gui;

	public CellTableMouseListener(GUIMediator gui)
	{
		this.gui = gui;
	}

	@Override
	public void mousePressed(MouseEvent e)  {check(e);}
	@Override
	public void mouseReleased(MouseEvent e) {check(e);}

    private void check(MouseEvent e) {
    	if (e.isPopupTrigger()) { //if the event shows the menu
    		CellTable source = (CellTable) e.getSource();
    		MainTable mt = (MainTable)source.getParent();
    		JPanel panel = (JPanel)mt.getParent().getParent().getParent();

    		int x = source.getLocation().x - panel.getLocation().x;
    		int y = source.getLocation().y - panel.getLocation().y;
    		Point pt = new Point(e.getX() + x, e.getY() + y);

    		int rowI = mt.rowAtPoint(pt);
    		mt.changeSelection(rowI, 0, false, false);

    		if (source.getRowCount() > 0)
    		{
	            int row = source.rowAtPoint( e.getPoint() );
	            int column = source.columnAtPoint( e.getPoint() );

	        	source.changeSelection(row, column, false, false);
	            source.editCellAt(row, column);

	            gui.showTablePopup(source, e.getX(), e.getY());
    		}
        }
    }

}