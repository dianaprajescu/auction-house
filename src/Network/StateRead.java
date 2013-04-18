package Network;

import interfaces.INetwork;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import app.Mediator;

/**
 * State read for message.
 * 
 * @author Stedy
 *
 */
public class StateRead implements IStateClientNetwork {

	SocketChannel channel;
	INetwork network;
	ByteBuffer buffer;
	ByteBuffer messageBuffer;
	ServerMessage message;
	ClientNetwork clientNetwork;
	int serviceId;
	int buyerId;
	int sellerId;
	int progress;
	
	static Logger log = Logger.getLogger(StateRead.class);

	/**
	 * Constructor.
	 * 
	 * @param channel
	 * @param network
	 * @param clientNetwork
	 */
	public StateRead(SocketChannel channel, INetwork network, ClientNetwork clientNetwork) {
		this.channel = channel;
		this.network = network;
		this.message = new ServerMessage();
		this.clientNetwork = clientNetwork;
	}

	@Override
	public void execute() throws IOException {
		this.prepareBuffer();

		try
		{
			int byteBuffer;

			// Read the size of the message.
			if ((byteBuffer = channel.read(buffer)) > 0) {
				buffer.flip();

				// Message size.
				int messageSize = buffer.getInt();

				buffer.clear();

				// Init server message.
				message.setSize(messageSize);

				// Init buffer for message.
				ByteBuffer messageBuffer = ByteBuffer.allocate(messageSize);

				// Read the message.
				if ((byteBuffer = channel.read(messageBuffer)) > 0) {
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
			if (byteBuffer == -1){
				throw new Exception ("EOF");
			}
		}
		catch(Exception e)
		{
			log.error("Channel closes" + e.getMessage());
			channel.close();
		}

		// No method passed.
		if (message.getMethod() != -1)
		{
			this.processMessage();
		}
	}

	@Override
	public void prepareBuffer() {
		buffer = ByteBuffer.allocate(Integer.SIZE / 8);
	}

	@Override
	public void processMessage() {
		
		log.debug(message.getMethod());
		
		if (message.getMethod() == NetworkMethods.NEW_USER.getInt())
		{
			processNewUser();
		}

		else if (message.getMethod() == NetworkMethods.USER_LEFT.getInt())
		{
			processUserLeft();
		}

		else if (message.getMethod() == NetworkMethods.MAKE_OFFER.getInt())
		{
			processOfferMade();
		}
		else if (message.getMethod() == NetworkMethods.START_TRANSFER.getInt())
		{
			try {
				processStartTransfer();
			} catch (IOException e) {
				log.error("Transfer error " + e.getMessage());
			}
		}
		else if (message.getMethod() == NetworkMethods.UPDATE_TRANSFER.getInt())
		{
			processUpdateTransfer();
		}
		else if (message.getMethod() == NetworkMethods.NEW_TRANSFER.getInt())
		{
			processNewTransfer();
		}
		else if (message.getMethod() == NetworkMethods.OFFER_EXCEEDED.getInt())
		{
			processOfferExceeded();
		}
		else if (message.getMethod() == NetworkMethods.OFFER_REFUSED.getInt())
		{
			processOfferRefused();
		}
		else if (message.getMethod() == NetworkMethods.OFFER_REMOVED.getInt())
		{
			processOfferRemoved();
		}
		else if (message.getMethod() == NetworkMethods.REMOVE_EXCEEDED.getInt())
		{
			processRemoveExceeded();
		}
		else if (message.getMethod() == NetworkMethods.REQUEST_DROPPED.getInt())
		{
			processRequestDropped();
		}
	}

	/**
	 * New user gets online.
	 */
	public void processNewUser(){
		
		log.debug("");
		
		buffer =  message.getBuffer();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				network.newUser(buffer.getInt(), buffer.getInt());
			}
		});
	}

	/**
	 * New offer has been made.
	 */
	public void processOfferMade(){
		log.debug("");
		
		buffer =  message.getBuffer();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				network.offerMade(buffer.getInt(), buffer.getInt(), buffer.getInt());
			}
		});
	}

	/**
	 * Update transfer.
	 */
	public void processUpdateTransfer(){

		log.debug("");

		buffer =  message.getBuffer();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				network.transfer(buffer.getInt(), buffer.getInt(), buffer.getInt());
			}
		});
	}

	/**
	 * User has went offline.
	 */
	private void processUserLeft(){
		
		log.debug("");
		
		buffer =  message.getBuffer();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				network.userLeft(buffer.getInt(), buffer.getInt());
			}
		});
	}

	/**
	 * Offer was exceeeded.
	 */
	private void processOfferExceeded()
	{
		log.debug("");
		
		buffer =  message.getBuffer();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				network.offerExceeded(buffer.getInt(), buffer.getInt(), buffer.getInt());
			}
		});
	}

	/**
	 * Start transfer.
	 * 
	 * @throws IOException
	 */
	private void processStartTransfer() throws IOException
	{
		log.debug("");

		buffer =  message.getBuffer();
		serviceId = buffer.getInt();
		buyerId = buffer.getInt();
		sellerId = buffer.getInt();

		this.clientNetwork.startTransfer(serviceId, buyerId, sellerId);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				network.transfer(serviceId, buyerId, sellerId);
			}
		});
	}

	/**
	 * Process new transfer request.
	 */
	private void processNewTransfer()
	{
		log.debug("");
		
		buffer =  message.getBuffer();
		progress = buffer.getInt();
		serviceId = buffer.getInt();
		buyerId = buffer.getInt();
		sellerId = buffer.getInt();

		// Save transfer data.
		try {
			this.clientNetwork.saveTransfer(serviceId, buyerId, sellerId, buffer);
		} catch (IOException e) {
			log.error("Error in saving transfer data " + e.getMessage());
		}

		// Notify that we GOT_TRANSFER.
		Object[] message = {NetworkMethods.GOT_TRANSFER.getInt(), serviceId, buyerId, sellerId};
		this.clientNetwork.sendMessage(message);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				network.gotTransfer(progress, serviceId, buyerId, sellerId);
			}
		});
	}

	/**
	 * Offer was refused.
	 */
	private void processOfferRefused() {
		
		log.debug("");
		
		buffer =  message.getBuffer();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				network.offerRefused(buffer.getInt(), buffer.getInt());
			}
		});
	}

	/**
	 * Offer was removed.
	 */
	private void processOfferRemoved() {
		
		log.debug("");
		
		buffer =  message.getBuffer();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				network.offerRemoved(buffer.getInt(), buffer.getInt());
			}
		});
	}

	/**
	 * Offer is no longer exceeed.
	 */
	private void processRemoveExceeded() {
		
		log.debug("");
		
		buffer =  message.getBuffer();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				network.removeExceeded(buffer.getInt(), buffer.getInt(), buffer.getInt());
			}
		});
	}

	/**
	 * Request was dropped.
	 */
	private void processRequestDropped() {
		
		log.debug("");

		buffer =  message.getBuffer();
		serviceId = buffer.getInt();
		buyerId = buffer.getInt();
		
		this.clientNetwork.failTransfer(serviceId, buyerId);
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				network.requestDropped(serviceId, buyerId);
			}
		});
	}

}
