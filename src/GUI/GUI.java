package GUI;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

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
		table.setPreferredScrollableViewportSize(new Dimension(300, 70));
        table.setFillsViewportHeight(true);
	}

	/**
	 * Build GUI.
	 */
	public void build() {
		JPanel p = new JPanel();
        p.add(new LoginButton(this, med));
        p.add(new LogoutButton(this, med));
        
        p.add(new JScrollPane(table), "South");
        
        getContentPane().add(p);
        setSize(400, 200);
        setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void actionPerformed(ActionEvent e) {
		Command comd = (Command) e.getSource();
        comd.execute();
	}

}
