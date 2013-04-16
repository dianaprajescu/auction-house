package app;

public enum UserType {
	BUYER(1),
	SELLER(2);
	
	private int type;
	
	private UserType(int type)
	{
		this.type = type;
	}
	
	public int getType()
	{
		return this.type;
	}
}
