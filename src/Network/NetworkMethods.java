package Network;

public enum NetworkMethods {
	TRANSFER(0),
	LOGIN(1),
	REGISTER_SERVICE(2),
	NEW_USER(3),
	MAKE_OFFER(4),
	LOGOUT(5),
	USER_LEFT(6),
	UPDATE_TRANSFER(7),
	OFFER_EXCEEDED(8),
	ACCEPT_OFFER(9),
	START_TRANSFER(10),
	NEW_TRANSFER(11),
	GOT_TRANSFER(12),
	DROP_OFFER_REQUEST(13),
	REFUSE_OFFER(14),
	OFFER_REFUSED(15),
	REMOVE_OFFER(16),
	OFFER_REMOVED(17),
	REMOVE_EXCEEDED(18),
	REQUEST_DROPPED(19),
	STOP_TRANSFER(20);

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
