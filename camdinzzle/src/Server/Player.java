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
	private Species specie;
	private String token;
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
	public void setSpecie(Species specie)
	{
		this.specie = specie;
	}
	public Species getSpecie()
	{
		return this.specie;
	}
	
	public void setToken(String t)
	{
		token = t;
	}
	
	public String getToken()
	{
		return token;
	}

}