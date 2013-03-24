package GUI.components;

import javax.swing.DefaultListModel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import GUI.GUIMediator;

public class MainTable extends JTable
{
	GUIMediator gui;
	
	public MainTable(MainTableModel model, GUIMediator gui)
	{
		super(model);
		this.gui = gui;
	}
	
	public String getSelectedStatus()
	{
		return ((MainTableModel)this.getModel()).getStatusAt(this.getSelectedRow());
	}
	
	public Integer getSelectedId()
	{
		return ((MainTableModel)this.getModel()).getIdAt(this.getSelectedRow());
	}
	
	public Integer getSelectedUserId()
	{
		CellTable ct = (CellTable)this.getEditorComponent();
		return ct.getSelectedId();
	}
	
	public String getSelectedUserStatus()
	{
		CellTable ct = (CellTable)this.getEditorComponent();
		return ct.getSelectedStatus();
	}
	
	public TableCellEditor getCellEditor(int row, int column)
    {		
		Object data = (Object)((MainTableModel)this.getModel()).getValueAt(row, column);
	
		MainTableCellEditor cellEditor = new MainTableCellEditor(gui, data);
		
		return cellEditor;
    }
	
	public TableCellRenderer getCellRenderer(int row, int column)
	{
		return new MainTableCellRenderer();
	}
}