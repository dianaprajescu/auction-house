package Network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import org.apache.log4j.*;

/**
 * 
 * Server Network component.
 *
 * @author Stedy
 *
 */
public class ServerNetwork extends Thread{
	
	// Server port.
	public static int port = 30000;
	
	// Server url.
	public static String url = "127.0.0.1";

	// Server channel.
	private ServerSocketChannel channel;

	// Server selector.
	private Selector selector;
	
	// List of current users in network.
	private UsersServer users;
	
	static Logger log = Logger.getLogger(ServerNetwork.class);
	
	public ServerNetwork()
	{
		// Init user list.
		users = new UsersServer();
	}

	public void accept(SelectionKey key) throws IOException
	{
		// The new connection.
		ServerSocketChannel serverSocketChannel = (ServerSocketChannel)key.channel();

		// Accept connection on server.
		SocketChannel clientSocketChannel = serverSocketChannel.accept();
		
		log.debug("Accept new client from " + clientSocketChannel.getRemoteAddress());

		// Configure connection to nonblocking.
		clientSocketChannel.configureBlocking(false);

		// Wait for client to send message.
		clientSocketChannel.register(key.selector(), SelectionKey.OP_READ, null);
	}

	public void read(SelectionKey key) throws IOException
	{
		// Init client channel.
		SocketChannel clientChannel = (SocketChannel) key.channel();
		
		log.debug("Read from " + clientChannel.getRemoteAddress());

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
				
				log.debug("Message size " + messageSize);

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

			// EOF.
			if (bytesRead == -1){
				throw new IOException("EOF");
			}

			// Process message.
			process(message, clientChannel);
		}
		catch(Exception e){
			
			log.debug("Channel closed " + clientChannel.getRemoteAddress());
			log.error("Channel closed " + clientChannel.getRemoteAddress());
			
			// Remove user from server.
			users.remove(clientChannel);
			clientChannel.close();
		}
	}

	/**
	 * Process a message received eighter from buyer or seller.
	 * 
	 * @param message
	 * @param channel
	 * @throws IOException
	 */
	public void process(ServerMessage message, SocketChannel channel) throws IOException
	{
		if (message.getMethod() == NetworkMethods.LOGIN.getInt()){
			processLogin(message, channel);
		}
		else if (message.getMethod() == NetworkMethods.REGISTER_SERVICE.getInt()){
			processRegisterService(message, channel);
		}
		else if (message.getMethod() == NetworkMethods.LOGOUT.getInt()){
			processLogout(channel);
		}
		else if (message.getMethod() == NetworkMethods.MAKE_OFFER.getInt()){
			processMakeOffer(message, channel);
		}
		else if (message.getMethod() == NetworkMethods.TRANSFER.getInt()){
			processTransfer(message, channel);
		}
		else if (message.getMethod() == NetworkMethods.REFUSE_OFFER.getInt()){
			processRefuseOffer(message, channel);
		}
		else if (message.getMethod() == NetworkMethods.DROP_OFFER_REQUEST.getInt()){
			processDropOfferRequest(message, channel);
		}
		else if (message.getMethod() == NetworkMethods.REMOVE_OFFER.getInt()){
			processRemoveOffer(message, channel);
		}
		else if (message.getMethod() == NetworkMethods.GOT_TRANSFER.getInt()){
			processGotTransfer(message, channel);
		}
		else if (message.getMethod() == NetworkMethods.ACCEPT_OFFER.getInt()){
			processAcceptOffer(message, channel);
		}
	}

	/**
	 * Process new user login.
	 * 
	 * @param message
	 * @param channel
	 */
	public void processLogin(ServerMessage message, SocketChannel channel)
	{
		ByteBuffer buf = message.getBuffer();
		
		int userId = buf.getInt();
		int type = buf.getInt();
		
		log.debug(userId + " " + type);
		
		users.addUser(userId, type, channel);
	}

	/**
	 * Process user registers new service.
	 * 
	 * @param message
	 * @param channel
	 * @throws IOException
	 */
	public void processRegisterService(ServerMessage message, SocketChannel channel) throws IOException
	{
		ByteBuffer buf = message.getBuffer();
		int serviceId = buf.getInt();
		int userId = buf.getInt();
		
		log.debug(serviceId + " " + userId);
		
		users.addService(serviceId, userId);
	}

	/**
	 * Process logout.
	 * 
	 * @param channel
	 * @throws IOException
	 */
	private void processLogout(SocketChannel channel) throws IOException
	{
		log.debug("logout");
		
		users.remove(channel);
	}

	/**
	 * Process seller has made and offer.
	 * 
	 * @param message
	 * @param channel
	 * @throws IOException
	 */
	public void processMakeOffer(ServerMessage message, SocketChannel channel) throws IOException
	{
		ByteBuffer buf = message.getBuffer();
		int serviceId = buf.getInt();
		int buyerId = buf.getInt();
		int sellerId = buf.getInt();
		int price = buf.getInt();
		
		log.debug(price + " " + serviceId + " " + buyerId + " " + sellerId);
		
		users.makeOffer(serviceId, buyerId, sellerId, price);
	}

	/**
	 * Process transfer from seller to buyer.
	 * 
	 * @param message
	 * @param channel
	 * @throws IOException
	 */
	public void processTransfer(ServerMessage message, SocketChannel channel) throws IOException{
		ByteBuffer buf = message.getBuffer();
		int progress = buf.getInt();
		int serviceId = buf.getInt();
		int buyerId = buf.getInt();
		int sellerId = buf.getInt();
		
		log.debug(progress + " " + serviceId + " " + buyerId + " " + sellerId);
		
		users.processTransfer(progress, serviceId, buyerId, sellerId, buf);
	}
	
	/**
	 * Process buyer has got transfer.
	 * 
	 * @param message
	 * @param channel
	 * @throws IOException
	 */
	public void processGotTransfer(ServerMessage message, SocketChannel channel) throws IOException{		
		ByteBuffer buf = message.getBuffer();
		int serviceId = buf.getInt();
		int buyerId = buf.getInt();
		int sellerId = buf.getInt();
		
		log.debug(serviceId + " " + buyerId + " " + sellerId);
		
		users.processGotTransfer(serviceId, buyerId, sellerId);
	}
	
	/**
	 * Process buyer accepts offer.
	 * 
	 * @param message
	 * @param channel
	 * @throws IOException
	 */
	public void processAcceptOffer(ServerMessage message, SocketChannel channel) throws IOException{
		ByteBuffer buf = message.getBuffer();
		int serviceId = buf.getInt();
		int buyerId = buf.getInt();
		int sellerId = buf.getInt();
		
		log.debug(serviceId + " " + buyerId + " " + sellerId);
		
		users.startTransfer(serviceId, buyerId, sellerId);
	}

	/**
	 * Process buyer refuses offer.
	 * 
	 * @param message
	 * @param channel
	 * @throws IOException
	 */
	private void processRefuseOffer(ServerMessage message, SocketChannel channel) throws IOException
	{
		ByteBuffer buf = message.getBuffer();
		int serviceId = buf.getInt();
		int buyerId = buf.getInt();
		int sellerId = buf.getInt();
		
		log.debug(serviceId + " " + buyerId + " " + sellerId);
		
		users.refuseOffer(serviceId, buyerId, sellerId);
	}

	/**
	 * Process buyer drops offer request.
	 * 
	 * @param message
	 * @param channel
	 * @throws IOException
	 */
	private void processDropOfferRequest(ServerMessage message, SocketChannel channel) throws IOException
	{
		ByteBuffer buf = message.getBuffer();
		int serviceId = buf.getInt();
		int userId = buf.getInt();
		
		log.debug(serviceId + " " + userId);
		
		users.dropOfferRequest(serviceId, userId);
	}

	/**
	 * Process seller removes offer.
	 * 
	 * @param message
	 * @param channel
	 * @throws IOException
	 */
	private void processRemoveOffer(ServerMessage message, SocketChannel channel) throws IOException
	{
		ByteBuffer buf = message.getBuffer();
		int serviceId = buf.getInt();
		int buyerId = buf.getInt();
		int sellerId = buf.getInt();
		
		log.debug(serviceId + " " + buyerId + " " + sellerId);
		
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
