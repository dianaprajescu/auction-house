package GUI.components;

import java.awt.Color;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

public class CellTableRenderer extends DefaultTableCellRenderer{
	
	public Component getTableCellRendererComponent(JTable table, Object value,
    		boolean isSelected, boolean hasFocus, int row, int column) {
		
		if (column == 2)
		{
			JProgressBar progress = new JProgressBar(0, 100);
			
			progress.setValue((int) value);
			
			if ((int) value > 0)
			{
				progress.setStringPainted(true);
			}
			
			return progress;
		}
		else
		{
			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}
    }
}
