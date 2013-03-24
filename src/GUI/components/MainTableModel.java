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
	
	private String[] columnNames;
	private Object[][] data;
	private Integer[] entryIds;
	 
	public MainTableModel(String[] columnNames, Object[][] data, Integer[] entryIds)
	{
		this.columnNames = columnNames;
		this.data = data;
		this.entryIds = entryIds;
	}
	 
	public int getColumnCount()
	{
		return columnNames.length;
	}
	
	public int getRowCount()
	{
		return data.length;
	}
	
	public String getColumnName(int col)
	{
		return columnNames[col];
	}
	
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
	 
    /*
    * JTable uses this method to determine the default renderer/
    * editor for each cell.  If we didn't implement this method,
    * then the last column would contain text ("true"/"false"),
    * rather than a check box.
    */
    public Class getColumnClass(int c) {
    	return getValueAt(0, c).getClass();
    }

	/**
	 * Make cells read-only.
	 */
	public boolean isCellEditable(int row, int column)
	{
		if (column % 3 == 1)
		{
			return true;
		}
		return false;
	}
}
