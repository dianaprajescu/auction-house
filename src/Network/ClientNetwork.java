package Network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ClientNetwork {
	
	public static void main(String[] args) throws IOException {

        SocketChannel channel = SocketChannel.open();
 
        // we open this channel in non blocking mode
        channel.configureBlocking(false);
        channel.connect(new InetSocketAddress(ServerNetwork.url, ServerNetwork.port));
 
        while (!channel.finishConnect()) {
            System.out.println("still connecting");
        }
        
        if (true) {
            // see if any message has been received
            ByteBuffer buffer = ByteBuffer.allocate(ServerNetwork.BUF_SIZE);
            
            buffer.putInt(100);
            buffer.putInt(200);
            buffer.flip();
 
            if (true) {
                
                while (buffer.hasRemaining()) {
                    channel.write(buffer);
                }
            }
 
        }
        
        while(true);
	}
	
}
