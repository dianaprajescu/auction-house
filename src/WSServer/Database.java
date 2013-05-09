package WSServer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Class used to connect to the DB and execute queries.
 * 
 * @author Stedy
 *
 */
public class Database {
	private Connection connect = null;
	private Statement statement = null;
	private ResultSet resultSet = null;
	
	/**
	 * Constructor. Makes the connection to the db.
	 */
	public Database ()
	{
		try
		{	
			// Load jdbc driver.
			Class.forName("com.mysql.jdbc.Driver");
			
			// Setup the connection with the DB
			connect = DriverManager.getConnection("jdbc:mysql://"+ DatabaseConfig.host +"/auction_house?" +
					"user=" + DatabaseConfig.username + "&password=" + DatabaseConfig.password);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method used to make queries to the db
	 * 
	 * @param   String  query  The query to execute.
	 * 
	 * @return  ResultSet  The result of the query execution.
	 */
	public ResultSet query (String query)
	{
		try {
			// Statements allow to issue SQL queries to the database
			statement = connect.createStatement();
			
			// Result set get the result of the SQL query
			statement.execute(query);
			
			resultSet = statement.getResultSet();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultSet;
	}
}
