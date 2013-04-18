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

	/**
	 * User goes offline.
	 *
	 * @param serviceId
	 * @param userId
	 */
	public void userLeft(int serviceId, int userId);

	/**
	 * Announce a seller that his offer was refused.
	 * @param serviceId
	 * @param buyerId
	 */
	public void offerRefused(int serviceId, int buyerId);

	/**
	 * Announce a seller that his offer was accepted.
	 * @param serviceId
	 * @param buyerId
	 */
	public void offerAccepted(int serviceId, int buyerId);

	/**
	 * Announce a seller that his offer was exceeded.
	 *
	 * @param serviceId
	 * @param buyerId
	 * @param price
	 */
	public void offerExceeded(int serviceId, int buyerId, int price);

	/**
	 * Announce a seller that his offer is no longer exceeded.
	 *
	 * @param serviceId
	 * @param buyerId
	 * @param price
	 */
	public void removeExceeded(int serviceId, int buyerId, int price);

	/**
	 * Announce a buyer that there was made an offer for a service.
	 *
	 * @param serviceId
	 * @param sellerId
	 * @param price
	 */
	public void offerMade(int serviceId, int sellerId, int price);

	/**
	 * Announce a buyer that a seller removed his offer.
	 *
	 * @param serviceId
	 * @param buyerId
	 */
	public void offerRemoved(int serviceId, int sellerId);
}
