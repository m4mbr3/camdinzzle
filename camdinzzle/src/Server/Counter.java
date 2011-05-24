package Server;

public class Counter implements Runnable {

	private ServerLogic server;
	private boolean setIsJustUpdate;
	private int timeToLive;
	
	public Counter(ServerLogic serverLogic, int timeToLive)
	{
		server = serverLogic;
		setIsJustUpdate = false;
		this.timeToLive = timeToLive;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try 
		{
			Thread.sleep(timeToLive); // da considerare il roundtriptime di rete
			/*
			if(!setIsJustUpdate)
			{
				server.updatePlayer(server.getTokenOfCurrentPlayer());
				server.changeRound();
			}
			*/
			server.updatePlayer(server.getTokenOfCurrentPlayer());
			String msg = server.changeRound();
			// TODO: chiamata al metodo del server che manda il messaggio in broadcast
		} 
		catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setIsJustUpdate(boolean value)
	{
		setIsJustUpdate = value;
	}

}
