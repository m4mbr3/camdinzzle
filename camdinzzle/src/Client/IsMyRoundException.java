package Client;

public class IsMyRoundException extends RuntimeException 
{
	private String username;
	
	public IsMyRoundException(String username)
	{
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
