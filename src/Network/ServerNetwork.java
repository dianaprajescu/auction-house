package Network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ServerNetwork {
	public static int port = 30000;
	public static String url = "127.0.0.1";
	public static int BUF_SIZE = 8;
	
	static HashMap<SocketChannel, ServerMessage> channels;
	
	private static ServerSocketChannel channel;
	private static Selector selector;
	
	public static void main(String[] args) throws IOException{
		init();
	}
	
	public static void accept(SelectionKey key) throws IOException
	{	
		// The new connection.
		ServerSocketChannel serverSocketChannel = (ServerSocketChannel)key.channel();
		
		// Accept connection on server.
		SocketChannel clientSocketChannel = serverSocketChannel.accept();
		
		// Configure connection to nonblocking.
		clientSocketChannel.configureBlocking(false);
		
		channels.put(clientSocketChannel, new ServerMessage());
		
		// Wait for client to send message.
		clientSocketChannel.register(key.selector(), SelectionKey.OP_READ, null);
	}
	
	public static void read(SelectionKey key) throws IOException
	{
		// Init client channel.
		SocketChannel clientChannel = (SocketChannel) key.channel();
		
		// Create buffer.
		ByteBuffer buffer = ByteBuffer.allocate(4);
		
		// Number of bytes read.
		int bytesRead = 0;
		
		// Read the size of the message.
		if ((bytesRead = clientChannel.read(buffer)) > 0) {
			buffer.flip();
			
			// Message size.
			int messageSize = buffer.getInt();

			buffer.clear();
			
			// Init server message.
			ServerMessage message = channels.get(clientChannel);
			message.setSize(messageSize);
			
			// Init buffer for message.
			ByteBuffer messageBuffer = ByteBuffer.allocate(messageSize);
			
			// Read the message.
			if (clientChannel.read(messageBuffer) > 0) {
				messageBuffer.flip();
				
				// Set message type.
				message.setType(messageBuffer.getInt());
				
				// Get message content.
				while(messageBuffer.hasRemaining()){
					message.addByte(messageBuffer.get());
				}
				
				// Clear buffer.
				messageBuffer.clear();
			}
			
			message.toString();
		}
		
		if (bytesRead < 0) {
            // Close the client channel.
            clientChannel.close();
        }
	}
	
	public static void process(int method, ArrayList<Integer> params, SocketChannel clientChannel)
	{
		System.out.println("Process message in server + " + params);
	}
	
	public static void write(SelectionKey key) throws IOException
	{
		// Init client channel.
		SocketChannel clientChannel = (SocketChannel) key.channel();
		
		// Init buffer message.
		CharBuffer buffer = CharBuffer.wrap("Hello client");
		
		// Write buffer message.
		while (buffer.hasRemaining())
		{
			clientChannel.write((Charset.defaultCharset().encode(buffer)));
		}
		
		// Clear buffer.
		buffer.clear();
	}
	
	public static void init() throws IOException{
		// Create a new serversocketchannel;
		channel = ServerSocketChannel.open();
		
		// Bind the channel to an address and listen for unbound connections.
		channel.bind(new InetSocketAddress(url, port));
		
		// Set nonblocking.
		channel.configureBlocking(false);
		
		// Create a new selector.
		selector = Selector.open();
		
		// Accept new connections.
		channel.register(selector, SelectionKey.OP_ACCEPT);
		
		channels = new HashMap<SocketChannel, ServerMessage>();
		
		// Wait for connections.
		while (true) {
			
			if (selector.select() == 0){
				continue;
			}
			
			// Iterate through the list of selected keys.
			for (Iterator<SelectionKey> it = selector.selectedKeys().iterator(); it.hasNext(); ) {
				SelectionKey key = it.next();
				it.remove();
				
				// Accept connection.
				if (key.isAcceptable())
					accept(key);
				
				// Read from client.
				else if (key.isReadable())
					read(key);
			}
		}
	}
}
