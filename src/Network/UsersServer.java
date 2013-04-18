package Network;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;

import app.UserType;

/**
 * Collection of users.
 *
 * @author Stedy
 *
 */
public class UsersServer {
	ArrayList<Integer> ids;
	ArrayList<Integer> types;
	ArrayList<ArrayList<Integer>> services;
	ArrayList<SocketChannel> channels;
	ArrayList<ArrayList<HashMap<Integer, Integer>>> offers;

	/**
	 * Constructor.
	 */
	public UsersServer(){
		ids = new ArrayList<Integer>();
		types = new ArrayList<Integer>();
		channels = new ArrayList<SocketChannel>();
		services = new ArrayList<ArrayList<Integer>>();
		offers = new ArrayList<ArrayList<HashMap<Integer, Integer>>>();
	}

	/**
	 * New user gets online.
	 *
	 * @param id
	 * @param type
	 * @param channel
	 */
	public void addUser(int id, Integer type, SocketChannel channel){
		ids.add(id);
		types.add(type);
		channels.add(channel);
		services.add(new ArrayList<Integer>());
		offers.add(new ArrayList<HashMap<Integer, Integer>>());
	}

	/**
	 * Remove user.
	 *
	 * @param channel
	 * @throws IOException
	 */
	public void remove(SocketChannel channel) throws IOException{
		// Find the index with channel passed.
		int idx = channels.indexOf(channel);

		if (idx < 0)
		{
			return;
		}

		int type;

		// Change type to opposite.
		if (types.get(idx) == UserType.SELLER.getType()){
			type = UserType.BUYER.getType();
		}
		else{
			type = UserType.SELLER.getType();
		}

		// Go through the user list and notify about the user that left.
		for (int i = 0; i < ids.size(); i++)
		{
			if (types.get(i) == type)
			{
				for (int j = 0; j < services.get(i).size(); j++)
				{
					if (services.get(idx).contains(services.get(i).get(j)))
					{
						int[] message = {NetworkMethods.USER_LEFT.getInt(), services.get(i).get(j), ids.get(idx)};
						StateWrite state = new StateWrite(channels.get(i), message);
						state.execute();
					}
				}
			}
		}

		// Close channel for the leaving user.
		channels.get(idx).close();

		// Remove the user data.
		ids.remove(idx);
		types.remove(idx);
		channels.remove(idx);
		services.remove(idx);
		offers.remove(idx);
	}

	/**
	 * User registers his service.
	 *
	 * @param serviceId
	 * @param userId
	 * @throws IOException
	 */
	public void addService(int serviceId, int userId) throws IOException{

		// Find the index with the userId passed.
		int idx = ids.indexOf(userId);

		if (idx >= 0){

			// Add service.
			services.get(idx).add(serviceId);
			offers.get(idx).add(new HashMap<Integer, Integer>());

			int type;

			// Change type to opposite.
			if (types.get(idx) == UserType.SELLER.getType()){
				type = UserType.BUYER.getType();
			}
			else{
				type = UserType.SELLER.getType();
			}

			// Go through user list and notify about getting online.
			for (int i = 0; i < ids.size(); i++){
				if (types.get(i) == type){
					for (int j = 0; j<services.get(i).size(); j++){

						// User has service with the current id.
						if (services.get(i).get(j) == serviceId){
							int[] messageSeller = {NetworkMethods.NEW_USER.getInt(), serviceId, userId};
							StateWrite stateSeller = new StateWrite(channels.get(i), messageSeller);
							stateSeller.execute();

							int[] messageBuyer = {NetworkMethods.NEW_USER.getInt(), serviceId, ids.get(i)};
							StateWrite stateBuyer = new StateWrite(channels.get(idx), messageBuyer);
							stateBuyer.execute();

							break;
						}
					}
				}
			}
		}
	}

	/**
	 * Seller makes an offer.
	 *
	 * @param serviceId
	 * @param buyerId
	 * @param sellerId
	 * @param price
	 *
	 * @throws IOException
	 */
	public void makeOffer(int serviceId, int buyerId, int sellerId, int price) throws IOException{
		int idxBuyer = ids.indexOf(buyerId);

		// Save offer.
		int idxSeller = ids.indexOf(sellerId);
		int idxService = services.get(idxSeller).indexOf(serviceId);
		offers.get(idxSeller).get(idxService).put(buyerId, price);

		// Inform the buyer about the offer.
		int[] messageBuyer = {NetworkMethods.MAKE_OFFER.getInt(), serviceId, sellerId, price};
		StateWrite state = new StateWrite(channels.get(idxBuyer), messageBuyer);
		state.execute();

		// Go through the list of sellers and announce if offer exceeded.
		for (int i = 0; i < ids.size(); i++)
		{
			// If user is seller.
			if (types.get(i) == UserType.SELLER.getType())
			{
				// If offer was made to the same buyer.
				if (offers.get(i).get(idxService).containsKey(buyerId))
				{
					// If offer was exceeded.
					if (offers.get(i).get(idxService).get(buyerId) > price)
					{
						// Inform the seller about the offer.
						int[] messageSeller = {NetworkMethods.OFFER_EXCEEDED.getInt(), serviceId, buyerId, price};
						state = new StateWrite(channels.get(i), messageSeller);
						state.execute();
					}
				}
			}
		}
	}

	/**
	 * Buyer refuses an offer made by a seller.
	 *
	 * @param serviceId
	 * @param buyerId
	 * @param sellerId
	 * @throws IOException
	 */
	public void refuseOffer(int serviceId, int buyerId, int sellerId) throws IOException
	{
		int idxSeller = ids.indexOf(sellerId);

		// Inform the seller.
		int[] messageSeller = {NetworkMethods.OFFER_REFUSED.getInt(), serviceId, buyerId};
		StateWrite state = new StateWrite(channels.get(idxSeller), messageSeller);
		state.execute();
	}

	/**
	 * Buyer drops offer request.
	 *
	 * @param serviceId
	 * @param userId
	 * @throws IOException
	 */
	public void dropOfferRequest(int serviceId, int userId) throws IOException
	{
		int idx = ids.indexOf(userId);

		// Go thrgough the list with sellers and refuse offers.
		for (int i = 0; i < ids.size(); i++)
		{
			// If the user is seller.
			if (types.get(i) == UserType.SELLER.getType())
			{
				// If the seller offers this service.
				if (services.get(i).contains(serviceId))
				{
					int idxService = services.get(i).indexOf(serviceId);

					// If the seller made an offer.
					if (offers.get(i).get(idxService).containsKey(userId))
					{
						// Refuse offer.
						int[] messageSeller = {NetworkMethods.OFFER_REFUSED.getInt(), serviceId, userId};
						StateWrite state = new StateWrite(channels.get(i), messageSeller);
						state.execute();
					}
				}
			}
		}
	}
}
