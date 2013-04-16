package GUI.components;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class CellTable extends JTable{
	
	public CellTable(CellTableModel model) {
		this.setModel(model);
	}

	public String getSelectedStatus()
	{
		return ((CellTableModel)this.getModel()).getStatusAt(this.getSelectedRow());
	}
	
	public Integer getSelectedId()
	{
		return ((CellTableModel)this.getModel()).getIdAt(this.getSelectedRow());
	}
	
	public TableCellRenderer getCellRenderer(int row, int column)
	{
		return new CellTableRenderer();
	}
}
