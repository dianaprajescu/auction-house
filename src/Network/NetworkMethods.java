package Network;

public enum NetworkMethods {
	LOGIN(1);
	
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
