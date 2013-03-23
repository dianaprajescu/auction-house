package GUI.components;

import javax.swing.DefaultListModel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import GUI.GUIMediator;

public class Table extends JTable
{
	GUIMediator gui;
	
	public Table(TableModel model, GUIMediator gui)
	{
		super(model);
		this.gui = gui;
	}
	
	public TableCellEditor getCellEditor(int row, int column)
    {		
		if (column % 3 == 1)
		{
			DefaultListModel data = (DefaultListModel)((TableModel)this.getModel()).getValueAt(row, column);
		
			ListCellEditor listCellEditor = new ListCellEditor(data, gui);
			
			return listCellEditor;
		}
		else
		{
			return super.getCellEditor(row, column);
		}
    }
	
	public TableCellRenderer getCellRenderer(int row, int column)
	{
		if (column % 3 == 1)
		{
			return new ListCellRenderer();
		}
		else
		{
			return super.getCellRenderer(row, column);
		}
	}
}