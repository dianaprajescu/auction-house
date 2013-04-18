package Network;

public enum NetworkMethods {
	START_TRANSFER(-1),
	TRANSFER(0),
	LOGIN(1),
	REGISTER_SERVICE(2),
	NEW_USER(3),
	MAKE_OFFER(4),
	UPDATE_TRANSFER(5);
	
	private int id;
	
	NetworkMethods(int id)
	{
		this.id = id;
	}
	
	public int getInt()
	{
		return id;
	}
}
