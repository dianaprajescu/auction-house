package GUI.components;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import GUI.GUIMediator;

public class MainTableCellEditor extends AbstractCellEditor implements TableCellEditor{

	GUIMediator guiMed;
	Object model;

	public MainTableCellEditor(GUIMediator guiMed, Object model) {
		this.guiMed = guiMed;
		this.model = model;
	}

	public Object getCellEditorValue() {
		return this.model;
	}

	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		// TODO Auto-generated method stub

		if (column == 1)
		{
			CellTable tableX = new CellTable((CellTableModel) model);

			tableX.addMouseListener(new CellTableMouseListener(this.guiMed));

			return tableX;
		}
		else
		{
			JLabel label = new JLabel((String) model);

			return label;
		}
	}

}
