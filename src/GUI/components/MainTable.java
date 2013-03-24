package GUI.components;

import java.awt.Component;

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

	public void rebuildTable()
	{
		try {
		    for (int row=0; row < this.getRowCount(); row++) {
		        int rowHeight = this.getRowHeight();

		        for (int column=0; column < this.getColumnCount(); column++) {
		            Component comp = this.prepareRenderer(this.getCellRenderer(row, column), row, column);
		            rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
		        }

		        this.setRowHeight(row, rowHeight);
		    }
		}
		catch(ClassCastException exception) { }
	}

	public String getSelectedStatus()
	{
		return ((MainTableModel)this.getModel()).getStatusAt(this.getSelectedRow());
	}

	public String getSelectedService()
	{
		return ((MainTableModel)this.getModel()).getServiceAt(this.getSelectedRow());
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

	@Override
	public TableCellEditor getCellEditor(int row, int column)
    {
		Object data = ((MainTableModel)this.getModel()).getValueAt(row, column);

		MainTableCellEditor cellEditor = new MainTableCellEditor(gui, data);

		return cellEditor;
    }

	@Override
	public TableCellRenderer getCellRenderer(int row, int column)
	{
		return new MainTableCellRenderer();
	}

}