package Network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

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
	 * Remove user on logout.
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
						Object[] message = {NetworkMethods.USER_LEFT.getInt(), services.get(i).get(j), ids.get(idx)};
						StateWrite state = new StateWrite(channels.get(i), message);
						state.execute();
					}
				}
			}
		}

		// If the offers made by this user have exceeded other offers undo it.
		if (types.get(idx) == UserType.SELLER.getType())
		{
			// Go through all this user's services.
			for (int i = 0; i < services.get(idx).size(); i++)
			{
				int serviceId = services.get(idx).get(i);
				int idxService = services.get(idx).indexOf(serviceId);

				// Go through all offers made for this service.
				Set<Entry<Integer, Integer>> userOffers = offers.get(idx).get(idxService).entrySet();

				for (Entry<Integer, Integer> offer : userOffers)
				{
					// Go through the list of sellers and announce if their offer is not exceeded anymore.
					for (int j = 0; j < ids.size(); j++)
					{
						// If user is seller.
						if (types.get(j) == UserType.SELLER.getType())
						{
							// If offer was made to the same buyer.
							if (offers.get(j).get(idxService).containsKey(offer.getKey()))
							{
								// If offer was exceeded.
								int price = offers.get(idx).get(idxService).get(offer.getKey());
								if (offers.get(j).get(idxService).get(offer.getKey()) > price)
								{
									// Inform the seller.
									Object[] messageSeller = {NetworkMethods.REMOVE_EXCEEDED.getInt(), serviceId, offer.getKey(), offers.get(j).get(idxService).get(offer.getKey())};
									StateWrite state = new StateWrite(channels.get(j), messageSeller);
									state.execute();
								}
							}
						}
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
							Object[] messageSeller = {NetworkMethods.NEW_USER.getInt(), serviceId, userId};
							StateWrite stateSeller = new StateWrite(channels.get(i), messageSeller);
							stateSeller.execute();

							Object[] messageBuyer = {NetworkMethods.NEW_USER.getInt(), serviceId, ids.get(i)};
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
		Object[] messageBuyer = {NetworkMethods.MAKE_OFFER.getInt(), serviceId, sellerId, price};
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
						Object[] messageSeller = {NetworkMethods.OFFER_EXCEEDED.getInt(), serviceId, buyerId, price};
						state = new StateWrite(channels.get(i), messageSeller);
						state.execute();
					}
				}
			}
		}
	}

	/**
	 * Server announces seller to start the transfer.
	 * 
	 * @param serviceId
	 * @param buyerId
	 * @param sellerId
	 * @throws IOException
	 */
	public void startTransfer(int serviceId, int buyerId, int sellerId) throws IOException{
		int idxSeller = this.ids.indexOf(sellerId);

		if (idxSeller >= 0)
		{
			Object[] message = {NetworkMethods.START_TRANSFER.getInt(), serviceId, buyerId, sellerId};
			StateWrite state = new StateWrite(channels.get(idxSeller), message);
			state.execute();
		}
	}

	/**
	 * Server passes transfer forward to the buyer.
	 * 
	 * @param progress
	 * @param serviceId
	 * @param buyerId
	 * @param sellerId
	 * @param buf
	 * @throws IOException
	 */
	public void processTransfer(int progress, int serviceId, int buyerId, int sellerId, ByteBuffer buf) throws IOException{
		int idxBuyer = this.ids.indexOf(buyerId);

		if (idxBuyer >= 0)
		{
			Object[] message = {NetworkMethods.NEW_TRANSFER.getInt(), progress, serviceId, buyerId, sellerId, buf};
			StateWrite state = new StateWrite(channels.get(idxBuyer), message);
			state.execute();
		}
	}

	/**
	 * Buy notifies server that he got the transfer.
	 * 
	 * @param serviceId
	 * @param buyerId
	 * @param sellerId
	 * @throws IOException
	 */
	public void processGotTransfer(int serviceId, int buyerId, int sellerId) throws IOException
	{
		int idxSeller = this.ids.indexOf(sellerId);

		if (idxSeller >= 0)
		{
			Object[] msg = {NetworkMethods.UPDATE_TRANSFER.getInt(), serviceId, buyerId, sellerId};
			StateWrite state = new StateWrite(channels.get(idxSeller), msg);
			state.execute();
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
		Object[] messageSeller = {NetworkMethods.OFFER_REFUSED.getInt(), serviceId, buyerId};
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

					Object[] messageSeller = {NetworkMethods.REQUEST_DROPPED.getInt(), serviceId, userId};
					StateWrite stateSeller = new StateWrite(channels.get(i), messageSeller);
					stateSeller.execute();
				}
			}
		}

		// Remove service from the list.
		services.get(idx).remove((Object) serviceId);
	}

	/**
	 * Seller removes the offer he made.
	 *
	 * @param serviceId
	 * @param buyerId
	 * @param sellerId
	 * @throws IOException
	 */
	public void removeOffer(int serviceId, int buyerId, int sellerId) throws IOException
	{
		int idxBuyer = ids.indexOf(buyerId);

		// Inform the buyer.
		Object[] message = {NetworkMethods.OFFER_REMOVED.getInt(), serviceId, sellerId};
		StateWrite state = new StateWrite(channels.get(idxBuyer), message);
		state.execute();
	}
}
