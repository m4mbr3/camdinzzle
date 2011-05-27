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
			System.out.println("Scaduti i " + timeToLive + " secondi!!");
			server.updatePlayer(server.getTokenOfCurrentPlayer());
			server.changeRound();
			server.changeRoundNotify();
		} 
		catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			System.out.println("Thread " + timeToLive + " interrotto");
		}
	}
	
	public void setIsJustUpdate(boolean value)
	{
		setIsJustUpdate = value;
	}

}
