package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

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
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			
			// Setup the connection with the DB
			connect = DriverManager.getConnection("jdbc:mysql://localhost/auction_house?" +
					"user=root&password=");
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error connecting to DB!", "DB error", JOptionPane.ERROR_MESSAGE);
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
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error while making a query: " + e.getMessage() + "! Please try again",
					"DB error", JOptionPane.WARNING_MESSAGE);
		}
		
		return resultSet;
	}
}
