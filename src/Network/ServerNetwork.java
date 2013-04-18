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

public class ServerNetwork extends Thread{
	public static int port = 30000;
	public static String url = "127.0.0.1";

	private ServerSocketChannel channel;
	private Selector selector;
	private UsersServer users;

	public ServerNetwork()
	{
		users = new UsersServer();
	}

	public void accept(SelectionKey key) throws IOException
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

	public void read(SelectionKey key) throws IOException
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

			process(message, clientChannel);
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("close connection");
			users.remove(clientChannel);
			clientChannel.close();
		}
	}

	public void process(ServerMessage message, SocketChannel channel) throws IOException
	{
		if (message.getMethod() == NetworkMethods.LOGIN.getInt()){
			processLogin(message, channel);
		}
		else if (message.getMethod() == NetworkMethods.REGISTER_SERVICE.getInt()){
			processRegisterService(message, channel);
		}

		else if (message.getMethod() == NetworkMethods.LOGOUT.getInt())
		{
			processLogout(channel);
		}

		else if (message.getMethod() == NetworkMethods.MAKE_OFFER.getInt()){
			processMakeOffer(message, channel);
		}
		else if (message.getMethod() == NetworkMethods.TRANSFER.getInt()){
			processTransfer(message, channel);
		}
		else if (message.getMethod() == NetworkMethods.REFUSE_OFFER.getInt())
		{
			processRefuseOffer(message, channel);
		}
		else if (message.getMethod() == NetworkMethods.DROP_OFFER_REQUEST.getInt())
		{
			processDropOfferRequest(message, channel);
		}
		else if (message.getMethod() == NetworkMethods.REMOVE_OFFER.getInt())
		{
			processRemoveOffer(message, channel);
		}
		else if (message.getMethod() == NetworkMethods.GOT_TRANSFER.getInt()){
			processGotTransfer(message, channel);
		}
		else if (message.getMethod() == NetworkMethods.ACCEPT_OFFER.getInt()){
			processAcceptOffer(message, channel);
		}
	}

	public void processLogin(ServerMessage message, SocketChannel channel)
	{
		System.out.println("login");
		ByteBuffer buf = message.getBuffer();
		users.addUser(buf.getInt(), buf.getInt(), channel);
	}

	public void processRegisterService(ServerMessage message, SocketChannel channel) throws IOException
	{
		System.out.println("registerService");
		ByteBuffer buf = message.getBuffer();
		int serviceId = buf.getInt();
		int userId = buf.getInt();
		users.addService(serviceId, userId);
	}

	private void processLogout(SocketChannel channel) throws IOException
	{
		System.out.println("logout");
		users.remove(channel);
	}

	public void processMakeOffer(ServerMessage message, SocketChannel channel) throws IOException
	{
		System.out.println("makeOffer");
		ByteBuffer buf = message.getBuffer();
		int serviceId = buf.getInt();
		int buyerId = buf.getInt();
		int sellerId = buf.getInt();
		int price = buf.getInt();
		users.makeOffer(serviceId, buyerId, sellerId, price);
	}

	public void processTransfer(ServerMessage message, SocketChannel channel) throws IOException{
		System.out.println("processTransfer");

		ByteBuffer buf = message.getBuffer();
		int progress = buf.getInt();
		int serviceId = buf.getInt();
		int buyerId = buf.getInt();
		int sellerId = buf.getInt();
		
		users.processTransfer(progress, serviceId, buyerId, sellerId, buf);
		
		//int[] msg = {NetworkMethods.UPDATE_TRANSFER.getInt(), serviceId, buyerId, sellerId};
		//StateWrite state = new StateWrite(channel, msg);
		//state.execute();
	}
	
	public void processGotTransfer(ServerMessage message, SocketChannel channel) throws IOException{
		System.out.println("processGotTransfer");
		
		ByteBuffer buf = message.getBuffer();
		int serviceId = buf.getInt();
		int buyerId = buf.getInt();
		int sellerId = buf.getInt();
		
		users.processGotTransfer(serviceId, buyerId, sellerId);
	}
	
	public void processAcceptOffer(ServerMessage message, SocketChannel channel) throws IOException{
		System.out.println("acceptOffer");
		
		ByteBuffer buf = message.getBuffer();
		int serviceId = buf.getInt();
		int buyerId = buf.getInt();
		int sellerId = buf.getInt();
		
		users.startTransfer(serviceId, buyerId, sellerId);
	}

	private void processRefuseOffer(ServerMessage message, SocketChannel channel) throws IOException
	{
		ByteBuffer buf = message.getBuffer();
		int serviceId = buf.getInt();
		int buyerId = buf.getInt();
		int sellerId = buf.getInt();
		users.refuseOffer(serviceId, buyerId, sellerId);
	}

	private void processDropOfferRequest(ServerMessage message, SocketChannel channel) throws IOException
	{
		ByteBuffer buf = message.getBuffer();
		int serviceId = buf.getInt();
		int userId = buf.getInt();
		users.dropOfferRequest(serviceId, userId);
	}

	private void processRemoveOffer(ServerMessage message, SocketChannel channel) throws IOException
	{
		ByteBuffer buf = message.getBuffer();
		int serviceId = buf.getInt();
		int buyerId = buf.getInt();
		int sellerId = buf.getInt();
		users.removeOffer(serviceId, buyerId, sellerId);
	}

	public void run(){
		try{
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
		catch(Exception e)
		{
		}
	}
}
