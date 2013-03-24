package GUI;
import interfaces.IGUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import GUI.components.CellTableModel;
import GUI.components.LogoutButton;
import GUI.components.MainTable;
import GUI.components.MainTableModel;
import GUI.components.MainTableMouseListener;
import app.Command;
import app.Database;
import app.Mediator;

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

	private GUIMediator GUImed = new GUIMediator();
	private Mediator med;

	public GUI (Mediator med)
	{
		super("Auction House");
		this.setLocationRelativeTo(null);
		setSize(900, 500);

		// First display login window.
        setVisible(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        GUImed.registerGUI(this);

        this.med = med;
        med.registerGUI(this);

		init();
		build();
	}

	/**
	 * Getter for mediator.
	 *
	 * @return  Mediator
	 */
	public Mediator getMediator()
	{
		return med;
	}

	/**
	 * Initialize model.
	 */
	@Override
	public void init() {
		// Populate model.
		model = new MainTableModel();

		// Get services from DB.
		Database db = new Database();
		ResultSet rs = db.query("SELECT * FROM service");

		try {
			while (rs.next())
			{
				Object[] row = {rs.getInt("id"), rs.getString("name"),  new CellTableModel(), "Inactive"};
				model.addRow(row);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		table = new MainTable(model, GUImed);
		table.addMouseListener(new MainTableMouseListener(GUImed));
		table.rebuildTable();

		table.setPreferredScrollableViewportSize(new Dimension(800, 400));
        table.setFillsViewportHeight(true);
	}

	/**
	 * Getter for table.
	 *
	 * @return  MainTable The table.
	 */
	public MainTable getTable()
	{
		return this.table;
	}

	/**
	 * Build GUI.
	 */
	@Override
	public void build() {

		new Login(this, GUImed);

		// Main panel.
		JPanel mainPanel = new JPanel();
		//mainPanel.add(new JLabel("Welcome to Auction House!"), "North");

        mainPanel.add(new LogoutButton(this, GUImed));
        mainPanel.add(new JScrollPane(table), "South");

        getContentPane().add(BorderLayout.CENTER, mainPanel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Command comd = (Command) e.getSource();
        comd.execute(e.getActionCommand());
	}

}
