package interfaces;

/**
 * @author Stedy
 *
 */
public interface INetwork {
	/**
	 * Start a new transfer.
	 * @param serviceId
	 * @param buyerId
	 * @param sellerId
	 */
	public void startTransfer(final int serviceId, final int buyerId, final int sellerId);
	
	/**
	 * New user gets online.
	 * 
	 * @param serviceId
	 * @param userId
	 */
	public void newUser(int serviceId, int userId);
}
