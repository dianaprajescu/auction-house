package GUI.components;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class Table extends JTable
{
	public Table(TableModel model)
	{
		super(model);
	}
	
	public TableCellEditor getCellEditor(int row, int column)
    {		
		if (column % 3 == 2)
		{
			JComboBox data = (JComboBox)((TableModel)this.getModel()).getValueAt(row, column);
		
			DefaultCellEditor ce = new DefaultCellEditor(data);
			
			return ce;
		}
		else
		{
			return super.getCellEditor(row, column);
		}
    }
	
	public TableCellRenderer getCellRenderer(int row, int column)
	{
		if (column % 3 == 2)
		{
			JComboBox data = (JComboBox)((TableModel)this.getModel()).getValueAt(row, column);
			
			
			MTableCellRenderer renderer = new MTableCellRenderer();
			
			return renderer;
		}
		else
		{
			return super.getCellRenderer(row, column);
		}
	}
}