package GUI.components;
import javax.swing.table.AbstractTableModel;

/**
 * @author Stedy
 *
 */
public class MainTableModel extends AbstractTableModel {
	/**
	 * Serializable class.
	 */
	private static final long serialVersionUID = 2408010632632242026L;

	private String[] columnNames = {"Service", "User List", "Status"};;
	private Object[][] data;
	private Integer[] entryIds;

	public MainTableModel(Object[][] data, Integer[] entryIds)
	{
		this.data = data;
		this.entryIds = entryIds;
	}

	public MainTableModel()
	{
		this.data = null;
		this.entryIds = null;
	}

	@Override
	public String getColumnName(int col)
	{
		return columnNames[col];
	}

	@Override
	public Object getValueAt(int row, int col)
	{
		return data[row][col];
	}

	public Integer getIdAt(int row)
	{
		return entryIds[row];
	}

	public String getStatusAt(int row)
	{
		return (String) data[row][2];
	}

	public void setStatusAt(String status, int row)
	{
		data[row][2] = status;
	}

	public String getServiceAt(int row)
	{
		return (String) data[row][0];
	}

	@Override
	public int getColumnCount()
	{
		if (this.columnNames != null)
		{
			return columnNames.length;
		}
		return 0;
	}

	@Override
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
		if (column % 3 == 1)
		{
			return true;
		}
		return false;
	}
	
	public int findRowByServiceId(int serviceId)
	{
		for (int i = 0; i < entryIds.length; i++)
		{
			if (serviceId == entryIds[i])
			{
				return i;
			}
		}
		
		return -1;
	}
}
