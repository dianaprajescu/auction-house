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
		loginPanel.add(new JLabel("Welcome to Auction House!"), "North");
		
		// Add label for username.
		loginPanel.add(new JLabel("username:"));
		UsernameField username = new UsernameField(med);
		username.setPreferredSize(new Dimension(100, 20));
		loginPanel.add(username);
		
		// Add label for password.
		loginPanel.add(new JLabel("password"));
		PasswordField password = new PasswordField(med);
		password.setPreferredSize(new Dimension(100, 20));
		loginPanel.add(password);
		
		// Select user type.
		loginPanel.add(new JLabel("Select user type"));
		BuyerType buyercb = new BuyerType(med);
		SellerType sellercb = new SellerType(med);
		
		// Group radio buttons.
		ButtonGroup group = new ButtonGroup();
		group.add(buyercb);
		group.add(sellercb);
		
		JPanel checkPanel = new JPanel(new GridLayout(0, 1));
        checkPanel.add(buyercb);
        checkPanel.add(sellercb);
        loginPanel.add(checkPanel);
		
        // Add login button.
        loginPanel.add(new LoginButton(al, med));
        
        getContentPane().add(BorderLayout.CENTER, loginPanel);
	}
}
