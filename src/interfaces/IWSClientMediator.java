package interfaces;

/**
 * @author Stedy
 *
 */
public interface IWSClientMediator {
	public void registerGUI(IGUI gui);
	public void registerNetwork(INetwork network);
	
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
	public void removeExceeded(int serviceId, int buyerId);
	
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
