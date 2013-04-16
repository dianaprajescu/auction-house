package GUI.components;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;

import GUI.GUI;
import GUI.InternalGUIMediator;

public class MainTableMouseListener extends MouseAdapter{

	private InternalGUIMediator med;
	private GUI gui;

	public MainTableMouseListener(InternalGUIMediator med, GUI gui)
	{
		this.med = med;
		this.gui = gui;
	}

	@Override
	public void mousePressed(MouseEvent e)  {check(e);}
	@Override
	public void mouseReleased(MouseEvent e) {check(e);}

    private void check(MouseEvent e) {
    	if (e.isPopupTrigger()) { //if the event shows the menu
    		MainTable source = (MainTable) e.getSource();
            int row = source.rowAtPoint( e.getPoint() );
            int column = source.columnAtPoint( e.getPoint() );

            try{
	            if (column == 0)
	            {
	            	source.changeSelection(row, column, false, false);
		            source.editCellAt(row, column);

		            med.showServicePopup(source, e.getX(), e.getY());
	            }
	            if (column == 1)
	            {
	            	source.changeSelection(row, column, false, false);
	            	source.editCellAt(row, column);

	            	e.translatePoint(gui.getMainPanel().getLocation().x - source.getEditorComponent().getLocation().x, gui.getMainPanel().getLocation().y - source.getEditorComponent().getLocation().y);
	            	e.setSource(source.getEditorComponent());

	            	((JTable)e.getSource()).dispatchEvent(e);
	            }
            }
            catch(Exception ex)
            {

            }
        }
    }

}
