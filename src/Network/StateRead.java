package Network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import interfaces.INetwork;

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

	public void processMessage() {
		if (message.getMethod() == NetworkMethods.NEW_USER.getInt())
		{
			processNewUser();
		}
	}
	
	public void processNewUser(){
		buffer =  message.getBuffer();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				network.newUser(buffer.getInt(), buffer.getInt(), "lalala");
			}
		});
	}

}
