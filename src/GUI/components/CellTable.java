package GUI.components;

import javax.swing.JTable;

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
}
