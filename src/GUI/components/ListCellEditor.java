package GUI.components;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import GUI.GUIMediator;

public class ListCellEditor extends AbstractCellEditor implements TableCellEditor{
	
	DefaultListModel model;
	GUIMediator gui;
	
	public ListCellEditor(DefaultListModel model, GUIMediator gui)
	{
		this.model = model;
		this.gui = gui;
	}

	public Object getCellEditorValue() {
		return model;
	}
	
	public Component getComponent()
	{
		JList list = new JList(model);
		
		list.addMouseListener(new ListMouseListener(list, gui));
		
		return list;
	}

	public Component getTableCellEditorComponent(JTable arg0, Object arg1,
			boolean arg2, int arg3, int arg4) {
		JList list = new JList(model);
		
		list.addMouseListener(new ListMouseListener(list, gui));
		
		return list;
	}

}
