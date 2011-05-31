package Client;

public class ChangeRoundException extends RuntimeException 
{
	private String username;
	
	public ChangeRoundException(String unsername)
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
