package GUI;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import GUI.components.LoginButton;
import GUI.components.LogoutButton;
import GUI.components.Table;
import GUI.components.TableModel;
import app.Command;
import app.Mediator;

import interfaces.IGUIMediator;
import interfaces.IGUI;

/**
 * @author Stedy
 *
 */
public class GUI extends JFrame implements IGUI, ActionListener {
	/**
	 * Serializable class.
	 */
	private static final long serialVersionUID = 5803021471507830897L;
	
	private TableModel model;
	private Table table;
	private JLabel item;
	private GUIMediator med = new GUIMediator();
	
	public GUI ()
	{
		super("Auction House");
		this.setLocationRelativeTo(null);
		setSize(600, 300);
		
		// First display login window.
        setVisible(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        med.registerGUI(this);
		
		init();
		build();
	}

	/**
	 * Initialize model.
	 */
	public void init() {		
		// Populate model.
		String[] columnNames = {"Name",
	            "Type",
	            "Service"};
		
		String[] test = {"1", "2"};
		
		DefaultListModel lmodel = new DefaultListModel();
		lmodel.addElement("user1");
		lmodel.addElement("user2");
		
		Object[][] data = {
		        {"Kathy", "Buyer", lmodel},
		        {"John", "Seller", lmodel},
		        {"Sue", "Seller", lmodel},
		        {"Sue", "Seller", lmodel},
		        {"Sue", "Seller", lmodel},
		        {"Jane", "Buyer", lmodel}
		        };
		
		// Initialize model.
		model = new TableModel(columnNames, data);	
		
		table = new Table(model, med);
		
		try {
		    for (int row=0; row<table.getRowCount(); row++) {
		        int rowHeight = table.getRowHeight();
		 
		        for (int column=0; column<table.getColumnCount(); column++) {
		            Component comp = table.prepareRenderer(table.getCellRenderer(row, column), row, column);
		            rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
		        }
		 
		        table.setRowHeight(row, rowHeight);
		    }
		} catch(ClassCastException e) { }
        
		table.setPreferredScrollableViewportSize(new Dimension(500, 250));
        table.setFillsViewportHeight(true);
	}
	
	public JTable getTable()
	{
		return this.table;
	}

	/**
	 * Build GUI.
	 */
	public void build() {
		
		new Login(this, med);
		
		// Main panel.
		JPanel mainPanel = new JPanel();  
		//mainPanel.add(new JLabel("Welcome to Auction House!"), "North");
        
        mainPanel.add(new LogoutButton(this, med));
        mainPanel.add(new JScrollPane(table), "South");
        
        getContentPane().add(BorderLayout.CENTER, mainPanel);
	}

	public void actionPerformed(ActionEvent e) {
		Command comd = (Command) e.getSource();
        comd.execute(e.getActionCommand());
	}

}
