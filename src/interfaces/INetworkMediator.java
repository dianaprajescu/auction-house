package interfaces;

/**
 * @author Stedy
 *
 */
public interface INetworkMediator {
	public void registerGUI(IGUI gui);
	public void registerWSClient(IWSClient client);
	
	/**
	 * Update transfer. 
	 * @param serviceId
	 * @param userId
	 * @param progress
	 */
	public void updateTransfer(int serviceId, int userId, int progress);
	
	/**
	 * New user gets online.
	 * 
	 * @param serviceId
	 * @param userId
	 */
	public void newUser(int serviceId, int userId);
}
