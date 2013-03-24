/**
 * 
 */
package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import interfaces.IGUIMediator;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import GUI.components.LoginButton;
import GUI.components.PasswordField;
import GUI.components.BuyerType;
import GUI.components.SellerType;
import GUI.components.UsernameField;
import java.awt.FlowLayout;
import javax.swing.SwingConstants;
import java.awt.Component;
import javax.swing.Box;
import java.awt.Color;
import javax.swing.JSeparator;

/**
 * @author Stedy
 *
 */
public class Login extends JFrame {
	private GUIMediator med;
	private ActionListener al;
	
	public Login (ActionListener al, GUIMediator med)
	{
		super("Login");
		setResizable(false);
		this.setLocationRelativeTo(null);
		setSize(400, 200); 
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        med.registerLogin(this);
		
		this.med = med;
		this.al = al;
		
		init();
		
		setVisible(true);
	}
	
	/**
	 * Initializa frame.
	 */
	private void init ()
	{
		// Login panel
		JPanel loginPanel = new JPanel();
		loginPanel.setLayout(null);
		
		JLabel welcome = new JLabel("Welcome to Auction House!");
		welcome.setHorizontalAlignment(SwingConstants.CENTER);
		welcome.setBounds(0, 8, 387, 14);
		
		loginPanel.add(welcome);
		
		// Add label for username.
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(82, 47, 78, 14);
		loginPanel.add(lblUsername);
		UsernameField username = new UsernameField(med);
		username.setPreferredSize(new Dimension(100, 20));
		username.setBounds(170,44,130,20);
		loginPanel.add(username);
		
		
		// Add label for password.
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(82, 73, 73, 14);
		loginPanel.add(lblPassword);
		PasswordField password = new PasswordField(med);
		password.setPreferredSize(new Dimension(100, 20));
		password.setBounds(170,70,130,20);
		loginPanel.add(password);
		
		// Select user type.
		JLabel lblUserType = new JLabel("User type:");
		lblUserType.setBounds(82, 99, 78, 14);
		loginPanel.add(lblUserType);
		SellerType sellercb = new SellerType(med);
		sellercb.setBounds(170,96,64,20);
		
		// Group radio buttons.
		ButtonGroup group = new ButtonGroup();
		BuyerType buyercb = new BuyerType(med);
		buyercb.setBounds(236,96,64,20);
		loginPanel.add(buyercb);
		loginPanel.add(sellercb);
		group.add(buyercb);
		group.add(sellercb);
		
        
        // Add login button.
        LoginButton login = new LoginButton(al, med);
        login.setBounds(155,138,78,23);
        loginPanel.add(login);
        
        getContentPane().add(BorderLayout.CENTER, loginPanel);
        
        JSeparator separator = new JSeparator();
        separator.setBounds(20, 25, 355, 2);
        loginPanel.add(separator);
	}
}
