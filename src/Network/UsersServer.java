package Network;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

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

	/**
	 * Constructor.
	 */
	public UsersServer(){
		ids = new ArrayList<Integer>();
		types = new ArrayList<Integer>();
		channels = new ArrayList<SocketChannel>();
		services = new ArrayList<ArrayList<Integer>>();
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
	
	public void makeOffer(int serviceId, int buyerId, int sellerId, int price) throws IOException{
		int idxBuyer = ids.indexOf(buyerId);
		
		int[] message = {NetworkMethods.MAKE_OFFER.getInt(), serviceId, sellerId, price};
		StateWrite stateSeller = new StateWrite(channels.get(idxBuyer), message);
		stateSeller.execute();
	}
}
