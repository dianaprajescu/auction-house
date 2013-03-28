package GUI.components;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class CellTableModel extends AbstractTableModel{
	private String[] columnNames = {"User", "Status", "Progress", "Price"};
	private ArrayList<Object[]> data;
	private ArrayList<Integer> entryIds;

	public CellTableModel()
	{
		this.data = new ArrayList<Object[]>();
		this.entryIds = new ArrayList<Integer>();
	}

	public CellTableModel(Object[][] data, Integer[] entryIds)
	{
		for (Object[] object : data)
		{
			this.data.add(object);
		}
		
		for (Integer entry : entryIds)
		{
			this.entryIds.add(entry);
		}
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
		return data.size();
	}

	public void addRow(Integer id, Object[] new_object)
	{
		this.data.add(new_object);
		this.entryIds.add(id);
	}
	
	public void removeRow(int row)
	{
		this.data.remove(row);
	}

	@Override
	public String getColumnName(int col)
	{
		return columnNames[col];
	}

	public Object getValueAt(int row, int col)
	{
		return data.get(row)[col];
	}

	@Override
	public void setValueAt(Object value, int row, int col)
	{
		this.data.get(row)[col] = value;
	}

	public Integer getIdAt(int row)
	{
		return entryIds.get(row);
	}

	public void setIdAt(Integer id, int row)
	{
		entryIds.add(row, id);
		entryIds.remove(row);
	}

	public String getStatusAt(int row)
	{
		return (String) data.get(row)[1];
	}

	public void setStatusAt(String status, int row)
	{
		this.data.get(row)[2] = status;
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
	
	public int findRowByUserId(int userId)
	{
		for (int i = 0; i < entryIds.size(); i++)
		{
			if (userId == entryIds.get(i))
			{
				return i;
			}
		}
		
		return -1;
	}
}
