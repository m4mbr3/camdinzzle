/**
 * 
 */
package Server;

import java.util.ArrayList;

import Client.Client;
import Client.ClientMessageBroker;
import Client.ConnectionManagerLocal;

public class ClientManagerLocal implements ClientManager{

	private ServerLogic serverLogic;
	private boolean isInGame;
	private Client client;
	private ConnectionManagerLocal cmLocal;
	private Server server;
	
	public ClientManagerLocal(ServerLogic serverLogic, Server server) 
	{
		this.serverLogic = serverLogic;
		this.server = server;
		isInGame = false;
		this.client = null;
	}

	@Override
	public boolean sendChangeRound(String msg) 
	{
		msg = ClientMessageBroker.manageChangeRound(msg);
		cmLocal.setChangeRound(msg);
		return true;
	}

	@Override
	public boolean getIsInGame() 
	{
		return isInGame;
	}

	@Override
	public void setIsInGame(boolean isInGame)
	{
		this.isInGame = isInGame;
	}
	public String creaUtente(String username, String password)
	{
		return serverLogic.add_new_user(username, password);
	}
	public String Login (String username, String password)
	{
		return serverLogic.login(username, password);
	}
	public String creaRazza (String token, String name, String type)
	{
		return serverLogic.addNewSpecies(token, name, type);
	}
	public String accessoPartita (String token)
	{
		String msg = serverLogic.gameAccess(token);
		if (RMIMessageBroker.convertGameAccess(msg) !=  null)
		{
			if (RMIMessageBroker.convertGameAccess(msg)[0].equals("ok"))
			{
				setIsInGame(true);
				if(token.equals(serverLogic.getTokenOfCurrentPlayer()))
				{
					serverLogic.changeRoundNotify();
				}
			}
		}
		return msg;
	}
	public String uscitaPartita (String token)
	{
		String tokenBeforeUpdatePlayer = serverLogic.getTokenOfCurrentPlayer();
		String msg = serverLogic.gameExit(token);
		if (RMIMessageBroker.convertGameExit(msg) != null)
		{			
			if (RMIMessageBroker.convertGameExit(msg)[0].equals("ok"))
			{
				setIsInGame(false);
				if(token.equals(tokenBeforeUpdatePlayer))
				{
					serverLogic.changeRoundNotify();
				}
			}
		}
		return msg;
	}
	public String[] listaGiocatori(String token)
	{
		return RMIMessageBroker.convertPlayerList(serverLogic.playerList(token));
	}
	public ArrayList<String> Classifica (String token)
	{
		return RMIMessageBroker.convertRanking(serverLogic.ranking(token));
	}
	public String logout (String token)
	{
		String msg = serverLogic.logout(token);
		
		if(msg.equals("@ok"))
		{
			this.setIsInGame(false);
			token = "";
			server.removeClientLocal(this);
		}
		
		return msg;
	}
	public ArrayList<String> mappaGenerale(String token)
	{
		return RMIMessageBroker.convertGeneralMap(serverLogic.generalMap(token));
	}
	public String[] listaDinosauri(String token)
	{
		return RMIMessageBroker.convertDinoList(serverLogic.dinosaursList(token));
	}
	public ArrayList<String> vistaLocale(String token, String dinoId)
	{
		return RMIMessageBroker.convertDinoZoom(serverLogic.dinoZoom(token, dinoId));
	}
	public String[] statoDinosauro(String token, String dinoId)
	{
		return RMIMessageBroker.convertDinoState(serverLogic.dinoState(token, dinoId));
	}
	public String muoviDinosauro(String token, String dinoId, String row, String col)
	{
		return serverLogic.dinoMove(token, dinoId, row, col);
	}
	public String cresciDinosauro(String token, String dinoId) 
	{
		return  serverLogic.dinoGrowUp(token, dinoId);
	}
	public String deponiUovo(String token, String dinoId)
	{
		return serverLogic.dinoNewEgg(token, dinoId);
	}
	public String confermaTurno(String token)
	{
		return serverLogic.roundConfirm(token);
	}
	public String passaTurno(String token)
	{
		String msg = serverLogic.playerRoundSwitch(token);
		
		if(msg.equals("@ok"))
		{
			serverLogic.changeRoundNotify();
		}
		
		return msg;
	}	
	public String getTokenOfCurrentPlayer()  
	{
		return serverLogic.getTokenOfCurrentPlayer();
	}
	
	public void changeRoundNotify() 
	{
		serverLogic.changeRoundNotify();
	}

	
	public String getUsernameOfCurrentPlayer() 
	{
		return serverLogic.getUsernameOfCurrentPlayer();
	}

	
	public void updatePlayer(String token)  
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

	public Client getClient()
	{
		return client;
	}

	public void setClient(Client client) 
	{
		this.client = client;
		cmLocal = (ConnectionManagerLocal)client.getConnManager();
	}
	

}
