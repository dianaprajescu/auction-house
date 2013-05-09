package WSClient;

import org.apache.axis.client.*;

import app.Mediator;

import GUI.components.CellTableModel;
import GUI.components.MainTableModel;

import interfaces.IWSClient;

import javax.xml.namespace.*;
import java.net.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class WSClient implements IWSClient {
	
	private static final String URL = "http://75.101.196.60:8080/axis/services/AuctionHouse";
	
	private URL endpoint;
	private Service service;
	private Mediator med;
	
	public WSClient(Mediator med) throws Exception
	{
		// Register mediator.
		this.med = med;
		med.registerWSClient(this);
		
		endpoint = new URL(WSClient.URL);
		service = new Service();
	}
	
	/**
	 * Invoke method from WS.
	 * 
	 * @param operationName
	 * @param params
	 * @return
	 */
	private Object invoke(String operationName, Object[] params) {
		try
		{
			Call call;
				
			call = (Call) service.createCall();	
			call.setTargetEndpointAddress(endpoint);
			call.setOperationName(new QName(operationName)); // operation name
			
			return call.invoke(params);
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	@Override
	public int login (String username, String password)
	{
		// Format params.
		Object[] params = {(Object)username, (Object)password};
		
		// Get result.
		Integer id = (Integer) this.invoke("login", params);
		
		return id != null ? id : -1 ;
	}
	
	@Override
	public MainTableModel getServiceList()
	{
		// Format params.
		Object[] params = {};
		
		// Get the service list.
		@SuppressWarnings("unchecked")
		HashMap<Integer, String> services = (HashMap<Integer, String>) this.invoke("getServiceList", params);
		
		// Populate model.
		MainTableModel model = new MainTableModel();
		
		// Check if services is not null.
		if (services != null)
		{
			Iterator<Map.Entry<Integer, String>> it = services.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry<Integer, String> pair = (Map.Entry<Integer, String>)it.next();
		        
		        Object[] row = {pair.getValue(), new CellTableModel(), "Inactive", "-"};
				model.addRow(pair.getKey(), row);
		    }
		}
		
		return model;
	}

	@Override
	public String getUsername(int userId) {
		// Format params.
		Object[] params = {(Object)userId};
		
		return (String) this.invoke("getUsername", params);
	}
}
