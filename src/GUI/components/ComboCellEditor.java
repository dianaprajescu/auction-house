package GUI.components;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import GUI.GUIMediator;

public class ComboCellEditor extends AbstractCellEditor implements TableCellEditor{
	
	DefaultComboBoxModel model;
	GUIMediator gui;
	
	public ComboCellEditor(DefaultComboBoxModel model, GUIMediator gui)
	{
		this.model = model;
		this.gui = gui;
	}

	public Object getCellEditorValue() {
		return model;
	}

	public Component getTableCellEditorComponent(JTable arg0, Object arg1,
			boolean arg2, int arg3, int arg4) {
		JComboBox combo = new JComboBox(this.model);

		return combo;
	}

}

