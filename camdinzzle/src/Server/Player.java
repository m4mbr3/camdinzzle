package Server;
/**
 * 
 */

/**
 * @author Andrea
 *
 */
public class Player {

	/**
	 * 
	 */
	private String password;
	private String userName;
	private String specie;
	/*
	 * default is offline
	 * can be playing, offline, logged
	 */
	
	public Player(String userName, String password) {
		// TODO Auto-generated constructor stub
		this.userName = userName;
		this.password = password;
		this.specie =  null;
		
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setSpecie(String specie)
	{
		this.specie = specie;
	}
	public String getSpecie()
	{
		return this.specie;
	}

}
