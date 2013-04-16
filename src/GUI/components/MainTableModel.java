package GUI.components;
import java.util.ArrayList;

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

	private String[] columnNames = {"Service", "User List", "Status", "Time Left"};
	private ArrayList<Object[]> data;
	private ArrayList<Integer> entryIds;
	private ArrayList<AuctionTimer> timers;

	public MainTableModel()
	{
		this.data = new ArrayList<Object[]>();
		this.entryIds = new ArrayList<Integer>();
		this.timers = new ArrayList<AuctionTimer>();
	}

	public MainTableModel(Object[][] data, Integer[] entryIds)
	{
		for (Object[] object : data)
		{
			this.data.add(object);
		}
		
		for (Integer entry : entryIds)
		{
			this.entryIds.add(entry);
			this.timers.add(null);
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

	public String getStatusAt(int row)
	{
		return (String) data.get(row)[2];
	}

	public void setStatusAt(String status, int row)
	{
		this.data.get(row)[2] = status;
	}
	
	public void setTimerAt(String timer, int row)
	{
		this.data.get(row)[3] = timer;
	}
	
	public void setTimerObjectAt(AuctionTimer timer, int row)
	{
		this.timers.set(row, timer);
	}
	
	public AuctionTimer getTimerObjectAt(int row)
	{
		return this.timers.get(row);
	}

	public String getServiceAt(int row)
	{
		return (String) data.get(row)[0];
	}

	@Override
	public int getRowCount()
	{
		return this.data.size();
	}

	public void addRow(Integer id, Object[] new_object)
	{
		this.data.add(new_object);
		this.entryIds.add(id);
		this.timers.add(null);
	}
	
	public void removeRow(int row)
	{
		this.data.remove(row);
		this.entryIds.remove(row);
		this.timers.remove(row);
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
	
	/**
	 * Return the row with the specified service id.
	 * 
	 * @param serviceId
	 * @return
	 */
	public int findRowByServiceId(int serviceId)
	{
		for (int i = 0; i < entryIds.size(); i++)
		{
			if (serviceId == entryIds.get(i))
			{
				return i;
			}
		}
		
		return -1;
	}
}
