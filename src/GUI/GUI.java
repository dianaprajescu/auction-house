package GUI;
import interfaces.IGUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

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
import app.Database;
import app.Mediator;
import app.UserType;

import javax.swing.SwingConstants;
import javax.swing.JSeparator;

/**
 * @author Stedy
 *
 */
public class GUI extends JFrame implements IGUI, ActionListener {
	
	private MainTableModel model;
	private MainTable table;

	private GUIMediator GUImed = new GUIMediator();
	private Mediator mainMed;

	private JPanel mainPanel;

	private JLabel welcome;
	private JSeparator separator;

	public GUI (Mediator med)
	{
		super("Auction House");
		setResizable(false);
		setSize(900, 600);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    int height = screenSize.height/2;
	    int width = screenSize.width/2;
	    int fHeight = this.getHeight()/2;
	    int fWidth = this.getWidth()/2;
	    
	    this.setLocation(width-fWidth, height-fHeight);
		

		// First display login window.
        setVisible(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        GUImed.registerGUI(this);

        this.mainMed = med;
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
		return mainMed;
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
				Object[] row = {rs.getInt("id"), rs.getString("name"), new CellTableModel(), "Inactive"};
				model.addRow(row);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		table = new MainTable(model, GUImed);
		table.addMouseListener(new MainTableMouseListener(GUImed, this));
		table.rebuildTable();

		table.setPreferredScrollableViewportSize(new Dimension(700, 500));
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

	public JPanel getMainPanel()
	{
		return this.mainPanel;
	}

	/**
	 * Build GUI.
	 */
	@Override
	public void build() {

		new Login(this, GUImed);

		// Main panel.
		mainPanel = new JPanel();
		mainPanel.setLayout(null);
		
		welcome = new JLabel();
		welcome.setHorizontalAlignment(SwingConstants.CENTER);
		welcome.setBounds(10, 5, 874, 20);
		mainPanel.add(welcome);

		LogoutButton logout = new LogoutButton(this, GUImed);
		logout.setFocusable(false);
		logout.setBounds(800, 7, 84, 20);
        mainPanel.add(logout);
        
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(10,43,874,508);
        mainPanel.add(scroll);
        
        getContentPane().add(mainPanel);
        
        separator = new JSeparator();
        separator.setBounds(239, 25, 400, 2);
        mainPanel.add(separator);
	}
	
	public JLabel getWelcomeLabel()
	{
		return this.welcome;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Command comd = (Command) e.getSource();
        comd.execute(e.getActionCommand());
	}

	public void startTransfer(int serviceId, int userId)
	{
		this.mainMed.startTransfer(serviceId, userId);
	}
	
	/**
	 * Login user.
	 * 
	 * @param username
	 * @param password
	 * @param type
	 * @return
	 */
	public int login(String username, String password, UserType type)
	{
		return this.mainMed.login(username, password, type);
	}
	
	/**
	 * Logout user.
	 * 
	 * @param userId
	 * @return
	 */
	public boolean logout(int userId)
	{
		return this.mainMed.logout(userId);
	}
}
