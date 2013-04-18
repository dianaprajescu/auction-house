package Network;

import interfaces.INetwork;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketOption;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class ClientNetwork extends Thread {
	// The associate network.
	INetwork network;
	
	// Message queue;
	Queue<int[]> messages;
	
	// The client state.
	IStateClientNetwork state;
	
	// Transfers.
	HashMap<Integer, File> transfers;
	
	// Channel.
	SocketChannel channel;
	
	public ClientNetwork(){
		// Init the message queue.
		this.messages = new LinkedList<int[]>();
	}
	
	public ClientNetwork(INetwork network){
		this.network = network;
		
		// Init the message queue.
		this.messages = new LinkedList<int[]>();
		
		// Init transfers.
		transfers = new HashMap<Integer, File>();
	}
	
	public void sendMessage(int[] message){
		this.messages.add(message);
	}
	
	public SocketChannel getChannel()
	{
		return this.channel;
	}
	
	public INetwork getNetwork(){
		return this.network;
	}
	
	public int[] netMessage(){
		return this.messages.poll();
	}
	
	public void startTransfer(int serviceId, int sellerId) throws IOException{
		File file = new File(20 + (new Random()).nextInt(61), serviceId, sellerId);
		this.transfers.put(serviceId, file);
	}
	
	public File getTransfer(int serviceId)
	{
		return this.transfers.get(serviceId);
	}
	
	public ByteBuffer getBufferTransfer(int serviceId) throws IOException{
		File file = this.transfers.get(serviceId);
		return file.getBuffer();
	}

	@Override
	public void run() {
		try
		{
			// Open new communication channel.
			channel = SocketChannel.open();
			 
	        // Open this channel in non blocking mode.
	        channel.configureBlocking(false);
	        
	        // Connect to server.
	        channel.connect(new InetSocketAddress(ServerNetwork.url, ServerNetwork.port));
	 
	        // Wait for connecting.
	        while (!channel.finishConnect());
	        
	        while (true)
	        {
		        // Send messages if they exist.
		        if (this.messages.size() > 0) {
		        	state = new StateWrite(channel, this.messages.poll(), this);
		        }
		        else
		        {
		        	state = new StateRead(channel, network);
		        }
		        
		        // Execute current state.
		        state.execute();
	        }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
