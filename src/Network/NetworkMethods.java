package Network;

public enum NetworkMethods {
	LOGIN(1),
	REGISTER_SERVICE(2),
	NEW_USER(3),
	LOGOUT(5),
	USER_LEFT(6);

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
