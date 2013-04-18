package Network;

import interfaces.INetwork;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import javax.swing.SwingUtilities;

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

			if (byteBuffer == -1){
				throw new Exception ("EOF");
			}
		}
		catch(Exception e)
		{
			channel.close();
		}

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
			System.out.println("Process ST TR");
			try {
				processStartTransfer();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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

	public void processNewUser(){
		buffer =  message.getBuffer();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				network.newUser(buffer.getInt(), buffer.getInt());
			}
		});
	}

	public void processOfferMade(){
		buffer =  message.getBuffer();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				network.offerMade(buffer.getInt(), buffer.getInt(), buffer.getInt());
			}
		});
	}

	public void processUpdateTransfer(){

		System.out.println("StateRead: processUpdateTransfer");

		buffer =  message.getBuffer();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				network.transfer(buffer.getInt(), buffer.getInt(), buffer.getInt());
			}
		});
	}

	private void processUserLeft(){
		buffer =  message.getBuffer();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				network.userLeft(buffer.getInt(), buffer.getInt());
			}
		});
	}

	private void processOfferExceeded()
	{
		buffer =  message.getBuffer();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				network.offerExceeded(buffer.getInt(), buffer.getInt(), buffer.getInt());
			}
		});
	}

	private void processStartTransfer() throws IOException
	{
		System.out.println("StateRead: processStartTransfer");

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

	private void processNewTransfer()
	{
		System.out.println("New: processNewTransfer");
		buffer =  message.getBuffer();
		progress = buffer.getInt();
		serviceId = buffer.getInt();
		buyerId = buffer.getInt();
		sellerId = buffer.getInt();

		Object[] message = {NetworkMethods.GOT_TRANSFER.getInt(), serviceId, buyerId, sellerId};
		this.clientNetwork.sendMessage(message);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				network.gotTransfer(progress, serviceId, buyerId, sellerId);
			}
		});
	}

	private void processOfferRefused() {
		buffer =  message.getBuffer();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				network.offerRefused(buffer.getInt(), buffer.getInt());
			}
		});
	}

	private void processOfferRemoved() {
		buffer =  message.getBuffer();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				network.offerRemoved(buffer.getInt(), buffer.getInt());
			}
		});
	}

	private void processRemoveExceeded() {
		buffer =  message.getBuffer();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				network.removeExceeded(buffer.getInt(), buffer.getInt(), buffer.getInt());
			}
		});
	}

	private void processRequestDropped() {
		buffer =  message.getBuffer();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				network.requestDropped(buffer.getInt(), buffer.getInt());
			}
		});
	}

}
