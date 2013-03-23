package GUI.components;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

/**
 * @author Stedy
 *
 */
public class TableModel extends AbstractTableModel {
	/**
	 * Serializable class.
	 */
	private static final long serialVersionUID = 2408010632632242026L;
	
	private String[] columnNames;
	private Object[][] data;
	 
	public TableModel(String[] columnNames, Object[][] data)
	{
		this.columnNames = columnNames;
		this.data = data;
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
		if (column % 3 == 2 || column % 3 == 1)
		{
			return true;
		}
		return false;
	}
}
