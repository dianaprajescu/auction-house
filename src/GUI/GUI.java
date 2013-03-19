package GUI;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import GUI.components.LoginButton;
import GUI.components.LogoutButton;
import GUI.components.TableModel;
import app.Command;

import interfaces.GUIMediator_interface;
import interfaces.GUI_interface;

/**
 * @author Stedy
 *
 */
public class GUI extends JFrame implements GUI_interface, ActionListener {
	/**
	 * Serializable class.
	 */
	private static final long serialVersionUID = 5803021471507830897L;
	
	private TableModel model;
	private JTable table;
	private JLabel item;
	private GUIMediator_interface med = new GUIMediator();
	
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
		
		Object[][] data = {
		        {"Kathy", "Buyer", "sports"},
		        {"John", "Seller", "sports"},
		        {"Sue", "Seller", "programming"},
		        {"Sue", "Seller", "programming"},
		        {"Sue", "Seller", "programming"},
		        {"Jane", "Buyer", "application"}
		        };
		
		// Initialize model.
		model = new TableModel(columnNames, data);	
		
		table = new JTable(model);
		table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);
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
        comd.execute();
	}

}
