package Network;

import java.nio.ByteBuffer;

public class ServerMessage {
	int messageMethod;
	ByteBuffer bytes;
	
	public ServerMessage(){
		bytes = null;
		messageMethod = -1;
	}
	
	public void setSize(int size){
		bytes = ByteBuffer.allocate(size - Integer.SIZE / 8);
	}
	
	public int getMethod(){
		return this.messageMethod;
	}
	
	public void setMethod(int method){
		this.messageMethod = method;
	}
	
	public void addByte(byte b){
		this.bytes.put(b);
	}
	
	public ByteBuffer getBuffer(){
		this.bytes.flip();
		return bytes;
	}
}
