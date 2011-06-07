/**
 * 
 */
package Server;

import java.util.ArrayList;

/**
 * @author Andrea
 *
 */
public class ClientManagerLocal implements ClientManager{

	private ServerLogic serverLogic;
	/**
	 * 
	 */
	public ClientManagerLocal(ServerLogic serverLogic) {
		// TODO Auto-generated constructor stub
		this.serverLogic = serverLogic;
	}

	@Override
	public boolean sendChangeRound(String msg) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getIsInGame() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setIsInGame(boolean isInGame) {
		// TODO Auto-generated method stub
		
	}
	public synchronized String creaUtente(String username, String password)
	{
		return serverLogic.add_new_user(username, password);
	}
	public synchronized String Login (String username, String password)
	{
		return serverLogic.login(username, password);
	}
	public synchronized String creaRazza (String token, String name, String type)
	{
		return serverLogic.addNewSpecies(token, name, type);
	}
	public synchronized String accessoPartita (String token)
	{
		return serverLogic.gameAccess(token);
	}
	public synchronized String uscitaPartita (String token)
	{
		return serverLogic.gameExit(token);
	}
	public synchronized String[] listaGiocatori(String token)
	{
		return ServerMessageBroker.managePlayerList(serverLogic.playerList(token));
	}
	public synchronized String[] Classifica (String token)
	{
		return ServerMessageBroker.manageRanking(serverLogic.ranking(token));
	}
	public synchronized String[]
	
	public ServerLogic getServerLogic() {
		return serverLogic;
	}

	public void setServerLogic(ServerLogic serverLogic) {
		this.serverLogic = serverLogic;
	}
	

}
