package GUI.components;

import java.awt.Component;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.UIManager;

public class AutoCloseDialog implements Runnable, ActionListener
{
    private JDialog dialog;

    public AutoCloseDialog(JDialog dialog)
    {
        this.dialog = dialog;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        dialog.dispose();
    }    

    // Show dialog message that closes in 2 seconds.
    static public void showMessageDialog(Component parent, Object message, String title, int messageType) {
    	
      // Create dialog.
      final JOptionPane optionPane = new JOptionPane(message);
      final JDialog dialog = optionPane.createDialog(parent, title);
      
      // Set additional info
      optionPane.setMessageType(messageType);
      
      // Set timer.
      Timer timer = new Timer(2000, new AutoCloseDialog(dialog));
      timer.setRepeats(false);
      timer.start();
      
      if (dialog.isDisplayable())
      {
          dialog.setVisible(true);
      }
      
    }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}