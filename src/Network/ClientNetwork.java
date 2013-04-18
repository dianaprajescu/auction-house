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
	
	// Stop thread.
	boolean off;
	
	// Message queue;
	Queue<Object[]> messages;
	
	// The client state.
	IStateClientNetwork state;
	
	// Transfers.
	HashMap<Integer, HashMap<Integer, File>> transfers;
	
	// Files received.
	HashMap<Integer, ReceivedFile> filesReceived;
	
	// Channel.
	SocketChannel channel;
	
	public ClientNetwork(){
		// Init the message queue.
		this.messages = new LinkedList<Object[]>();
		
		this.off = false;
	}
	
	public ClientNetwork(INetwork network){
		this.network = network;
		
		// Init the message queue.
		this.messages = new LinkedList<Object[]>();
		
		// Init transfers.
		transfers = new HashMap<Integer, HashMap<Integer, File>>();
		
		// Init received files.
		filesReceived = new HashMap<Integer, ReceivedFile>();
	}
	
	public void sendMessage(Object[] message){
		this.messages.add(message);
	}
	
	public SocketChannel getChannel()
	{
		return this.channel;
	}
	
	public INetwork getNetwork(){
		return this.network;
	}
	
	public Object[] netMessage(){
		return this.messages.poll();
	}
	
	public void startTransfer(int serviceId, int buyerId, int sellerId) throws IOException{
		File file = new File(20 + (new Random()).nextInt(61), serviceId, buyerId, sellerId);
		
		HashMap<Integer, File> buyers;
		
		if (this.transfers.containsKey(serviceId))
		{
			buyers = this.transfers.get(serviceId);
		}
		else
		{
			buyers = new HashMap<Integer, File>();
		}
		
		buyers.put(buyerId, file);
		this.transfers.put(serviceId, buyers);
	}
	
	public File getTransfer(int serviceId, int buyerId)
	{
		HashMap<Integer, File> buyers;
		buyers = this.transfers.get(serviceId);
		
		if (buyers != null)
		{
			File f = buyers.get(buyerId);
			
			return f;
		}
		else
		{
			return null;
		}
	}
	
	public ByteBuffer getBufferTransfer(int serviceId, int buyerId) throws IOException{
		File file = this.getTransfer(serviceId, buyerId);
		
		if (file != null)
		{
			return file.getBuffer();
		}
		else
		{
			return null;
		}
	}
	
	public void saveTransfer(int serviceId, int buyerId, int sellerId, ByteBuffer buffer) throws IOException 
	{
		if (!filesReceived.containsKey(serviceId))
		{
			ReceivedFile f = new ReceivedFile(serviceId, buyerId, sellerId);
			filesReceived.put(serviceId, f);
		}
		
		ReceivedFile f = filesReceived.get(serviceId);
		f.write(buffer);
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
	        	if (this.off)
	        	{
	        		break;
	        	}
	        	
		        // Send messages if they exist.
		        if (this.messages.size() > 0) {
		        	state = new StateWrite(channel, this.messages.poll(), this);
		        }
		        else
		        {
		        	state = new StateRead(channel, network, this);
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
	
	public void stopClient()
	{
		this.off = true;
	}
	
}
