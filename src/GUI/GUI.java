package GUI;
import interfaces.IGUI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import GUI.components.CellTableModel;
import GUI.components.LogoutButton;
import GUI.components.MainTable;
import GUI.components.MainTableModel;
import GUI.components.MainTableMouseListener;
import app.Command;

/**
 * @author Stedy
 *
 */
public class GUI extends JFrame implements IGUI, ActionListener {
	/**
	 * Serializable class.
	 */
	private static final long serialVersionUID = 5803021471507830897L;

	private MainTableModel model;
	private MainTable table;

	private JLabel item;
	private final GUIMediator med = new GUIMediator();

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
		String[] columnNames = {"Service",
	            "User List",
	            "Status"};

		CellTableModel lmodel = new CellTableModel();
		String[] columns = {"User", "Status", "Progress"};
		lmodel.setColumnNames(columns);
		String[] rowData1 = {"user1", "status1", ""};
		String[] rowData2 = {"user2", "status2", ""};
		lmodel.addRow(rowData1);
		lmodel.addRow(rowData2);
		lmodel.addRow(rowData2);
		Integer[] idsx = {1,2,3};
		lmodel.setEntryIds(idsx);

		Integer[] ids = {1,2,3,4,5,6};

		Object[][] data = {
		        {"Kathy",  lmodel, "inactive"},
		        {"John", lmodel, "inactive"},
		        {"Sue", lmodel, "inactive"},
		        {"Sue", lmodel, "inactive"},
		        {"Sue", lmodel, "inactive"},
		        {"Jane", lmodel, "inactive"}
		        };

		// Initialize model.
		model = new MainTableModel(columnNames, data, ids);

		table = new MainTable(model, med);
		table.addMouseListener(new MainTableMouseListener(med));

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

	public MainTable getTable()
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
