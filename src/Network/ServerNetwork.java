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
import java.util.HashMap;
import java.util.Iterator;

import app.UserType;

public class ServerNetwork {
	public static int port = 30000;
	public static String url = "127.0.0.1";
	public static int BUF_SIZE = 8;
	
	private static ServerSocketChannel channel;
	private static Selector selector;
	private static UsersServer users;
	
	public static void main(String[] args) throws IOException{
		users = new UsersServer();
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
		
		// Wait for client to send message.
		clientSocketChannel.register(key.selector(), SelectionKey.OP_READ, null);
	}
	
	public static void read(SelectionKey key) throws IOException
	{
		// Init client channel.
		SocketChannel clientChannel = (SocketChannel) key.channel();
		
		// Init server message.
		ServerMessage message = new ServerMessage();
		
		try {
			// Create buffer.
			ByteBuffer buffer = ByteBuffer.allocate(Integer.SIZE / 8);
			
			// Number of bytes read.
			int bytesRead = 0;
			
			// Read the size of the message.
			if ((bytesRead = clientChannel.read(buffer)) > 0) {
				buffer.flip();
				
				// Message size.
				int messageSize = buffer.getInt();
	
				buffer.clear();
				
				message.setSize(messageSize);
				
				// Init buffer for message.
				ByteBuffer messageBuffer = ByteBuffer.allocate(messageSize);
				
				// Read the message.
				if ((bytesRead = clientChannel.read(messageBuffer)) > 0) {
					messageBuffer.flip();
					
					// Set message type.
					message.setMethod(messageBuffer.getInt());
					
					// Get message content.
					while(messageBuffer.hasRemaining()){
						message.addByte(messageBuffer.get());
					}
					
					// Clear buffer.
					messageBuffer.clear();
				}
			}
			
			if (bytesRead == -1){
				throw new IOException("EOF");
			}
		}
		catch(Exception e){
			users.remove(clientChannel);
			clientChannel.close();
		}
		
		process(message, clientChannel);
	}
	
	public static void process(ServerMessage message, SocketChannel channel) throws IOException
	{
		if (message.getMethod() == NetworkMethods.LOGIN.getInt()){
			processLogin(message, channel);
		}
		else if (message.getMethod() == NetworkMethods.REGISTER_SERVICE.getInt()){
			processRegisterService(message, channel);
		}
		else if (message.getMethod() == NetworkMethods.MAKE_OFFER.getInt()){
			processMakeOffer(message, channel);
		}
	}
	
	public static void processLogin(ServerMessage message, SocketChannel channel)
	{
		System.out.println("login");
		ByteBuffer buf = message.getBuffer();
		users.addUser(buf.getInt(), buf.getInt(), channel);
	}
	
	public static void processRegisterService(ServerMessage message, SocketChannel channel) throws IOException
	{
		System.out.println("registerService");
		ByteBuffer buf = message.getBuffer();
		int serviceId = buf.getInt();
		int userId = buf.getInt();
		users.addService(serviceId, userId);
	}
	
	public static void processMakeOffer(ServerMessage message, SocketChannel channel) throws IOException
	{
		System.out.println("makeOffer");
		ByteBuffer buf = message.getBuffer();
		int serviceId = buf.getInt();
		int buyerId = buf.getInt();
		int sellerId = buf.getInt();
		int price = buf.getInt();
		users.makeOffer(serviceId, buyerId, sellerId, price);
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
