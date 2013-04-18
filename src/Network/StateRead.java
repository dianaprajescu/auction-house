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

	public StateRead(SocketChannel channel, INetwork network) {
		this.channel = channel;
		this.network = network;
		this.message = new ServerMessage();
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
		if (message.getMethod() == NetworkMethods.UPDATE_TRANSFER.getInt())
		{
			processUpdateTransfer();
		}
	}

	public void processNewUser(){
		buffer =  message.getBuffer();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				network.newUser(buffer.getInt(), buffer.getInt(), "lalala");
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
		
		System.out.println("processUpdateTransfer");
		
		buffer =  message.getBuffer();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				network.transfer(buffer.getInt());
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

}
