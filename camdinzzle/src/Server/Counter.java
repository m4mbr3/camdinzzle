package Server;

import java.io.Serializable;

public class Counter implements Runnable, Serializable 
{
	private static final long serialVersionUID = 1L;
	private ServerLogic server;
	private int timeToLive;
	
	public Counter(ServerLogic serverLogic, int timeToLive)
	{
		server = serverLogic;
		this.timeToLive = timeToLive;
	}
	
	@Override
	public void run() 
	{
		try 
		{
			Thread.sleep(timeToLive); 
			server.updatePlayer(server.getTokenOfCurrentPlayer());
			server.changeRoundNotify();
			server.changeRound();
		} 
		catch (InterruptedException e) 
		{
			
		}
	}
}
