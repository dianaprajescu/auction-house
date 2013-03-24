package GUI.components;

import javax.swing.table.AbstractTableModel;

public class CellTableModel extends AbstractTableModel{
	private String[] columnNames;
	private Object[][] data;
	private Integer[] entryIds;

	public CellTableModel()
	{
		this.data = null;
		this.columnNames = null;
		this.entryIds = null;
	}

	public CellTableModel(String[] columnNames, Object[][] data, Integer[] entryIds)
	{
		this.columnNames = columnNames;
		this.data = data;
		this.entryIds = entryIds;
	}

	public int getColumnCount()
	{
		if (this.columnNames != null)
		{
			return columnNames.length;
		}
		return 0;
	}

	public int getRowCount()
	{
		if (this.data != null)
		{
			return data.length;
		}
		return 0;
	}

	public void addRow(Object[] new_object)
	{
		Object[][] x = new Object[this.getRowCount() + 1][this.getColumnCount()];
		Integer[] e = new Integer[this.getRowCount() + 1];
		if (this.data != null)
		{
			System.arraycopy(this.data, 0, x, 0, this.getRowCount());
			System.arraycopy(new_object, 1, x[this.getRowCount()], 0, new_object.length-1);

			System.arraycopy(this.entryIds, 0, e, 0, this.getRowCount());
			e[this.getRowCount()] = (Integer) new_object[0];
		}
		else
		{
			System.arraycopy(new_object, 1, x[this.getRowCount()], 0, new_object.length-1);
			e[this.getRowCount()] = (Integer) new_object[0];
		}

		this.data = x;
		this.entryIds = e;
	}

	public void setColumnNames(String[] columns)
	{
		this.columnNames = columns;
	}

	public void setEntryIds(Integer[] ids)
	{
		this.entryIds = ids;
	}

	@Override
	public String getColumnName(int col)
	{
		return columnNames[col];
	}

	public Object getValueAt(int row, int col)
	{
		return data[row][col];
	}

	@Override
	public void setValueAt(Object value, int row, int col)
	{
		this.data[row][col] = value;
	}

	public Integer getIdAt(int row)
	{
		return entryIds[row];
	}

	public void setIdAt(Integer id, int row)
	{
		this.entryIds[row] = id;
	}

	public String getStatusAt(int row)
	{
		return (String) data[row][1];
	}

	public void setStatusAt(String status, int row)
	{
		data[row][2] = status;
	}

    /*
    * JTable uses this method to determine the default renderer/
    * editor for each cell.  If we didn't implement this method,
    * then the last column would contain text ("true"/"false"),
    * rather than a check box.
    */
    @Override
	public Class getColumnClass(int c) {
    	return getValueAt(0, c).getClass();
    }

	/**
	 * Make cells read-only.
	 */
	@Override
	public boolean isCellEditable(int row, int column)
	{
		return false;
	}
}
