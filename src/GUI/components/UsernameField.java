/**
 * 
 */
package GUI.components;

import javax.swing.JTextField;

import app.UserType;

import GUI.InternalGUIMediator;

/**
 * @author diana
 *
 */
public class UsernameField extends JTextField {
	private InternalGUIMediator med;
	
	/**
	 * User id from db.
	 */
	private int id;
	
	/**
	 * User type (buyer = 1, seller = 2).
	 */
	private UserType type;

	public UsernameField(InternalGUIMediator med) {
		this.med = med;
		med.registerUsername(this);
	}
	
	/**
	 * Setter for id.
	 * 
	 * @param   int  id  The id to set.
	 */
	public void setId(int id)
	{
		this.id = id;
	}
	
	/**
	 * Getter for id.
	 * 
	 * @return  int  The id.
	 */
	public int getId()
	{
		return this.id;
	}
	
	/**
	 * Setter for type.
	 * 
	 * @param   int  type  The type to set.
	 */
	public void setType(UserType type)
	{
		this.type = type;
	}
	
	/**
	 * Getter for type.
	 * 
	 * @return  int  The type.
	 */
	public UserType getType()
	{
		return this.type;
	}
}
