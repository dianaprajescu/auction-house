package GUI.components;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JList;
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

	@Override
	public Object getCellEditorValue() {
		return model;
	}

	@Override
	public Component getTableCellEditorComponent(JTable arg0, Object arg1,
			boolean arg2, int arg3, int arg4) {
		JComboBox combo = new JComboBox(this.model);

		return combo;
	}

}

