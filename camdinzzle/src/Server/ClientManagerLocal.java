/**
 * 
 */
package Server;

import java.rmi.RemoteException;
import java.util.ArrayList;

import Client.Client;

/**
 * @author Andrea
 *
 */
public class ClientManagerLocal implements ClientManager{

	private ServerLogic serverLogic;
	private boolean isInGame;
	Client client;
	/**
	 * 
	 */
	public ClientManagerLocal(ServerLogic serverLogic) {
		// TODO Auto-generated constructor stub
		this.serverLogic = serverLogic;
		isInGame = false;
		this.client = null;
	}

	@Override
	public boolean sendChangeRound(String msg) {
		// TODO Auto-generated method stub
		client.getConnManager().setChangeRound(msg);
		return true;
	}

	@Override
	public boolean getIsInGame() {
		// TODO Auto-generated method stub
		return isInGame;
	}

	@Override
	public void setIsInGame(boolean isInGame) {
		// TODO Auto-generated method stub
		this.isInGame = isInGame;
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
		String msg = serverLogic.gameAccess(token);
		if (RMIMessageBroker.convertGameAccess(msg) !=  null)
		{
			if (RMIMessageBroker.convertGameAccess(msg)[0].equals("ok"))
			{
				setIsInGame(true);
			}
		}
		return msg;
	}
	public synchronized String uscitaPartita (String token)
	{
		String msg = serverLogic.gameExit(token);
		if (RMIMessageBroker.convertGameExit(msg) != null)
		{
			if (RMIMessageBroker.convertGameExit(msg)[0].equals("ok"))
			{
				setIsInGame(false);
			}
		}
		return msg;
	}
	public synchronized String[] listaGiocatori(String token)
	{
		return RMIMessageBroker.convertPlayerList(serverLogic.playerList(token));
	}
	public synchronized ArrayList<String> Classifica (String token)
	{
		return RMIMessageBroker.convertRanking(serverLogic.ranking(token));
	}
	public synchronized String logout (String token)
	{
		return serverLogic.logout(token);
	}
	public synchronized ArrayList<String> mappaGenerale(String token)
	{
		return RMIMessageBroker.convertGeneralMap(serverLogic.generalMap(token));
	}
	public synchronized String[] listaDinosauri(String token)
	{
		return RMIMessageBroker.convertDinoList(serverLogic.dinosaursList(token));
	}
	public synchronized ArrayList<String> vistaLocale(String token, String dinoId)
	{
		return RMIMessageBroker.convertDinoZoom(serverLogic.dinoZoom(token, dinoId));
	}
	public synchronized String[] statoDinosauro(String token, String dinoId)
	{
		return RMIMessageBroker.convertDinoState(serverLogic.dinoState(token, dinoId));
	}
	public synchronized String muoviDinosauro(String token, String dinoId, String row, String col)
	{
		return serverLogic.dinoMove(token, dinoId, row, col);
	}
	public synchronized String cresciDinosauro(String token, String dinoId) 
	{
		return  serverLogic.dinoGrowUp(token, dinoId);
	}
	public synchronized String deponiUovo(String token, String dinoId)
	{
		return serverLogic.dinoNewEgg(token, dinoId);
	}
	public synchronized String confermaTurno(String token)
	{
		return serverLogic.roundConfirm(token);
	}
	public synchronized String passaTurno(String token)
	{
		return serverLogic.playerRoundSwitch(token);
	}	
	public synchronized String getTokenOfCurrentPlayer()  
	{
		return serverLogic.getTokenOfCurrentPlayer();
	}
	
	public synchronized void changeRoundNotify() 
	{
		serverLogic.changeRoundNotify();
	}

	
	public synchronized String getUsernameOfCurrentPlayer() 
	{
		return serverLogic.getUsernameOfCurrentPlayer();
	}

	
	public synchronized void updatePlayer(String token)  
	{
		serverLogic.updatePlayer(token);
	}
	public ServerLogic getServerLogic() {
		return serverLogic;
	}

	public void setServerLogic(ServerLogic serverLogic) {
		this.serverLogic = serverLogic;
	}
	public void sendMessage(String msg)
	{
		client.getConnManager().setChangeRound(msg);
	}

	@Override
	public Client getClient() {
		// TODO Auto-generated method stub
		return client;
	}

	@Override
	public void setClient(Client client) {
		// TODO Auto-generated method stub
		this.client = client;
	}
	

}
