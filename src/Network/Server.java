package Network;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class Server{
	public static int port = 30000;
	public static String url = "127.0.0.1";

	private ServerSocketChannel channel;
	private Selector selector;
	private static UsersServer users;
	
	public static ServerNetwork SN;
	public static JFrame f;

	public static void main(String[] args) throws IOException, InterruptedException{
		
		f = new JFrame();
		f.setTitle("Network Server");
		f.add(new JLabel("This is the Server for Network component."));
		f.pack();
		f.setVisible(true);
		f.addWindowListener(new WindowAdapter()
		{
		    public void windowClosing(WindowEvent e)
		    {
		    	System.exit(0);
		    }
		});
		
		SN = new ServerNetwork();
		SN.start();
	}
}