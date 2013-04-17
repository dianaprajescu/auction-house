package Network;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class ServerMessage {
	int messageType;
	ByteBuffer bytes;
	
	public ServerMessage(){
		bytes = null;
		messageType = -1;
	}
	
	public void setSize(int size){
		bytes = ByteBuffer.allocate(size - Integer.SIZE / 8);
	}
	
	public int getType(){
		return this.messageType;
	}
	
	public void setType(int type){
		this.messageType = type;
	}
	
	public void addByte(byte b){
		this.bytes.put(b);
	}
	
	public String toString(){
		this.bytes.flip();
		System.out.println(this.bytes.getInt());
		System.out.println(this.bytes.getInt());
		
		return null;
	}
}
