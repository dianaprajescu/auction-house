package GUI;
import interfaces.IGUI;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.Dialog;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import GUI.components.CellTableModel;
import GUI.components.LogoutButton;
import GUI.components.MainTable;
import GUI.components.MainTableModel;
import GUI.components.MainTableMouseListener;
import app.Command;
import app.Database;
import app.Mediator;
import app.UserType;

/**
 * @author Stedy
 *
 */
public class GUI extends JFrame implements IGUI, ActionListener {

	private MainTableModel model;
	private MainTable table;

	/**
	 * Public only for simulator, it will be made private.
	 */
	public InternalGUIMediator GUImed = new InternalGUIMediator();
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
				Object[] row = {rs.getString("name"), new CellTableModel(), "Inactive", "-"};
				model.addRow(rs.getInt("id"), row);
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

	@Override
	public void startTransfer(int serviceId, int buyerId, int sellerId)
	{
		this.mainMed.startTransfer(serviceId, buyerId, sellerId);
	}

	@Override
	public void updateTransfer(int serviceId, int userId, int progress)
	{
		this.GUImed.updateTransfer(serviceId, userId, progress);
	}

	@Override
	public int login(String username, String password, UserType type)
	{
		return this.mainMed.login(username, password, type);
	}

	@Override
	public boolean logout(int userId)
	{
		return this.mainMed.logout(userId);
	}

	@Override
	public CellTableModel launchOfferRequest(int serviceId, int userId)
	{
		return this.mainMed.launchOfferRequest(serviceId, userId);
	}

	@Override
	public boolean dropOfferRequest(int serviceId, int userId)
	{
		return this.mainMed.dropOfferRequest(serviceId, userId);
	}

	@Override
	public boolean acceptOffer(int serviceId, int buyerId, int sellerId)
	{
		return this.mainMed.acceptOffer(serviceId, buyerId, sellerId);
	}

	@Override
	public boolean refuseOffer(int serviceId, int buyerId, int sellerId)
	{
		return this.mainMed.refuseOffer(serviceId, buyerId, sellerId);
	}

	@Override
	public boolean makeOffer(int serviceId, int buyerId, int sellerId, int price)
	{
		return this.mainMed.makeOffer(serviceId, buyerId, sellerId, price);
	}

	@Override
	public boolean removeOffer(int serviceId, int buyerId, int sellerId)
	{
		return this.mainMed.removeOffer(serviceId, buyerId, sellerId);
	}

	@Override
	public void newUser(int serviceId, int userId, String username) {
		this.GUImed.newUser(serviceId, userId, username);
	}

	@Override
	public void dropUser(int userId) {
		this.GUImed.dropUser(userId);
	}

	@Override
	public void offerRefused(int serviceId, int buyerId) {
		this.GUImed.offerRefused(serviceId, buyerId);
	}

	@Override
	public void offerAccepted(int serviceId, int buyerId) {
		this.GUImed.offerAccepted(serviceId, buyerId);
	}

	@Override
	public void offerExceeded(int serviceId, int buyerId, int price) {
		this.GUImed.offerExceeded(serviceId, buyerId, price);
	}

	@Override
	public void removeExceeded(int serviceId, int buyerId) {
		this.GUImed.removeExceeded(serviceId, buyerId);
	}

	@Override
	public void offerMade(int serviceId, int sellerId, int price) {
		this.GUImed.offerMade(serviceId, sellerId, price);
	}

	@Override
	public void offerRemoved(int serviceId, int sellerId) {
		this.GUImed.offerRemoved(serviceId, sellerId);
	}
}
