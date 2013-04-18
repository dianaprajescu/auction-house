package Network;

import interfaces.INetwork;

import java.net.InetSocketAddress;
import java.net.SocketOption;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.Queue;

public class ClientNetwork extends Thread {
	// The associate network.
	INetwork network;
	
	// Message queue;
	Queue<int[]> messages;
	
	// The client state.
	IStateClientNetwork state;
	
	public ClientNetwork(){
		// Init the message queue.
		this.messages = new LinkedList<int[]>();
	}
	
	public ClientNetwork(INetwork network){
		this.network = network;
		
		// Init the message queue.
		this.messages = new LinkedList<int[]>();
	}
	
	public void sendMessage(int[] message){
		this.messages.add(message);
	}

	@Override
	public void run() {
		try
		{
			// Open new communication channel.
			SocketChannel channel = SocketChannel.open();
			 
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
		        	state = new StateWrite(channel, this.messages.poll());
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
