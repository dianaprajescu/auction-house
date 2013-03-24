package GUI.components;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

public class MainTableCellRenderer extends DefaultTableCellRenderer{
	
	public Component getTableCellRendererComponent(JTable table, Object value,
    		boolean isSelected, boolean hasFocus, int row, int column) {
		
		if (column == 1)
		{
			JTable tableX = new JTable((CellTableModel) value);
			tableX.setOpaque(false);
			((DefaultTableCellRenderer)tableX.getDefaultRenderer(Object.class)).setOpaque(false);	
			
			return tableX;
		}
		else
		{
			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}
    }
}
