package GUI.components;

import java.awt.Component;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class ComboCellRenderer extends DefaultTableCellRenderer{
	
	public Component getTableCellRendererComponent(JTable table, Object value,
    		boolean isSelected, boolean hasFocus, int row, int column) {
		DefaultComboBoxModel data = (DefaultComboBoxModel)((TableModel)table.getModel()).getValueAt(row, column);
		JComboBox status = new JComboBox(data);
		
		return status;
    }
}