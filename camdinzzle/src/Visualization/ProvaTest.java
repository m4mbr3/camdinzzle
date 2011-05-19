package Visualization;

import Server.*;
import Client.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyStore.Entry;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.Generated;
import javax.xml.soap.Text;

import Server.ClientManagerSocket;
import Server.Player;

public class ProvaTest {

	/*
	 * Ricerca il minimo all'interno della chiave e lo ritorna
	 */
	public int findMin(String key)
	{
		int min = key.length() + 1;
		
		for(int i = 0; i<key.length(); i++)
		{
			if(Integer.parseInt(String.valueOf(key.charAt(i))) < min)
			{
				min = Integer.parseInt(String.valueOf(key.charAt(i)));
			}
		}
		
		return min;
	}
	
	/* Token generato tramite l'applicazione sulla concatenazione di username e hashcode(del 
	 * riferimento all'oggetto Player) del player l'algoritmo di trasposizione con numeri come chiave
	 */
	public String generateToken(String username, Player p)
	{
		String key = new String(generateKeyForToken());
		int length = key.length();
		String concatenateIdentifier = new String(username + p);
		String token = new String("");
		
		for(int j = 0; j<length; j++)
		{
			int min = findMin(key);
			int positionMin = key.indexOf(String.valueOf(min));
			
			key = key.replaceFirst(String.valueOf(min), String.valueOf(key.length()));

			for(int i = positionMin; i<concatenateIdentifier.length(); i += length)
			{
				token += concatenateIdentifier.charAt(i);
			}
		}
		
		return token;
	}
	
	/*
	 * Chiave per generazione dei token generata in modo casuale ad ogni avvio del Server
	 */
	public String generateKeyForToken()
	{
		// Lunghezza della chiave da 3 a 5
		int keyLength = (int) (Math.random() * 3 + 3);
		String key = new String("");
		
		// Contiene i numeri casuali da 0 a 7 presenti nella chiave univocamente
		HashMap<String, String> registeredPositions = new HashMap<String, String>();
		int i = 0;
		
		while(i<keyLength)
		{
			// Generazione casuale del numero da inserire nella chiave
			int singleCasual = (int) (Math.random() * keyLength); 
			
			// Se non � gi� presente nella chiave, viene inserito
			if(!registeredPositions.containsKey(String.valueOf(singleCasual)))
			{
				registeredPositions.put(String.valueOf(singleCasual), String.valueOf(singleCasual));
				key += String.valueOf(singleCasual);
				i++;
			}
		}
		
		return key;
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Server server = new Server(); 
		Textual text = new Textual();
		
		String [] arr;
		String msg, scelta = "";
		BufferedReader dataInput = new BufferedReader(new InputStreamReader(System.in));
		String token = "";
		
		do
		{
			System.out.println("Possibilit� di azioni:\n");
			System.out.println("CU per creare un utente");
			System.out.println("LOGIN per eseguire il login");
			System.out.println("CR per creare una razza di dinosauri");
			System.out.println("AP per accesso partita");
			System.out.println("UP per uscita partita");
			System.out.println("LG per vedere la lista dei giocatori attualment in partita");
			System.out.println("CG per vedere la classifica generale");
			System.out.println("LOGOUT per eseguire il logout");
			System.out.println("GM per vedere la mappa generale");
			System.out.println("LD per vedere la lista dei dinosauri");
			System.out.println("VL per vedere la vista locale di un dinosauro");
			System.out.println("SD per vedere lo stato di un dinosauro");
			System.out.println("MD per muovere un dinosauro");
			System.out.println("CD per far crescere un dinosauro");
			System.out.println("DU per far deporre un uovo ad un dinosauro");
			System.out.println("CT per donfermare il turno");
			System.out.println("PT per passare il turno");
			System.out.println("E per uscire");
			System.out.print("\n");
			do
			{
				try
				{
					scelta = dataInput.readLine().toUpperCase();
					
					if(scelta.equals("CU"))
					{
						arr = text.drawUserCreation();
						msg = ClientMessageBroker.createUser(arr[0], arr[1]);
						
						System.out.println("Client: " + msg);
						System.out.println("Server: " + server.add_new_user(msg));
					}
					else if(scelta.equals("LOGIN"))
					{
						arr = text.drawLogin();
						msg = ClientMessageBroker.createLogin(arr[0], arr[1]);
						
						System.out.println("Client: " + msg);
						msg = server.login(msg);
						System.out.println("Server: " + msg);
						
						token = ClientMessageBroker.manageLogin(msg)[0];
					}
					else if(scelta.equals("CR"))
					{
						arr = text.drawRaceCreation();
						msg = ClientMessageBroker.createRace(token, arr[0], arr[1]);
						
						System.out.println("Client: " + msg);
						System.out.println("Server: " + server.addNewSpecies(msg));
					}
					else if(scelta.equals("AP"))
					{
						msg = ClientMessageBroker.createGameAccess(token);
						
						System.out.println("Client: " + msg);
						System.out.println("Server: " + server.gameAccess(msg));
					}
					else if(scelta.equals("UP"))
					{
						msg = ClientMessageBroker.createGameExit(token);
						
						System.out.println("Client: " + msg);
						System.out.println("Server: " + server.gameExit(msg));
					}
					else if(scelta.equals("LG"))
					{
						msg = ClientMessageBroker.createPlayerList(token);
						System.out.println("Client: " + msg);
						msg = server.playerList(msg);
						System.out.println("Server: " + msg);
						
						text.drawPlayerList(msg);
					}
					else if(scelta.equals("CG"))
					{
						msg = ClientMessageBroker.createRanking(token);
						System.out.println("Client: " + msg);
						msg = server.ranking(msg);
						System.out.println("Server: " + msg);
						
						text.drawRanking(msg);
					}
					else if(scelta.equals("LOGOUT"))
					{
						msg = ClientMessageBroker.createLogout(token);
						System.out.println("Client: " + msg);
						msg = server.logout(msg);
						System.out.println("Server: " + msg);
					}
					else if(scelta.equals("GM"))
					{
						msg = ClientMessageBroker.createGeneralMap(token);
						System.out.println("Client: " + msg);
						msg = server.generalMap(msg);
						System.out.println("Server: " + msg);
						
						text.drawGeneralMap(msg);
					}
					else if(scelta.equals("LD"))
					{
						msg = ClientMessageBroker.createGeneralMap(token);
						System.out.println("Client: " + msg);
						msg = server.dinosaursList(msg);
						System.out.println("Server: " + msg);
						
						text.drawDinoList(msg);
					}
					else if(scelta.equals("VL"))
					{
						String dinoId = text.getDinoId();
						
						if(dinoId != null)
						{
							msg = ClientMessageBroker.createDinoZoom(token, dinoId);
							System.out.println("Client: " + msg);
							msg = server.dinoZoom(msg);
							System.out.println("Server: " + msg);
							text.drawDinoZoom(dinoId, msg);
						}
						
					}
					else if(scelta.equals("SD"))
					{
						String dinoId = text.getDinoId();
						
						if(dinoId != null)
						{
							msg = ClientMessageBroker.createDinoState(token, dinoId);
							System.out.println("Client: " + msg);
							msg = server.dinoState(msg);
							System.out.println("Server: " + msg);
							text.drawDinoState(dinoId, msg);
						}
						
					}
					else if(scelta.equals("MD"))
					{
						System.out.println("NON ANCORA IMPLEMENTATO SUL SERVER!!");
					}
					else if(scelta.equals("CD"))
					{
						System.out.println("NON ANCORA IMPLEMENTATO SUL SERVER!!");
					}
					else if(scelta.equals("DU"))
					{
						System.out.println("NON ANCORA IMPLEMENTATO SUL SERVER!!");
					}
					else if(scelta.equals("CT"))
					{
						msg = ClientMessageBroker.createRoundConfirmation(token);
						System.out.println("Client: " + msg);
						msg = server.roundConfirm(msg);
						System.out.println("Server: " + msg);
					}
					else if(scelta.equals("PT"))
					{
						msg = ClientMessageBroker.createPassOffRound(token);
						System.out.println("Client: " + msg);
						msg = server.playerRoundSwitch(msg);
						System.out.println("Server: " + msg);
						
						System.out.println("Turno del giocatore: " + server.serverRoundSwitch());
					}
					else if(scelta.equals("E"))
						System.out.println("Uscita dal gioco");
					
					System.out.println("*****FINE INTERAZIONE*****\n");
				}
				catch(IOException ex)
				{
					ex.printStackTrace();
				}
			}
			while((!scelta.equals("CU")) && (!scelta.equals("LOGIN")) && (!scelta.equals("CR"))&& (!scelta.equals("AP")) && (!scelta.equals("UP"))
				&& (!scelta.equals("LG")) && (!scelta.equals("CG")) && (!scelta.equals("LOGOUT")) && (!scelta.equals("GM")) 
				&& (!scelta.equals("LD")) && (!scelta.equals("VL")) && (!scelta.equals("SD")) && (!scelta.equals("MD"))
				&& (!scelta.equals("CD")) && (!scelta.equals("DU")) && (!scelta.equals("CT")) && (!scelta.equals("PT"))
				&& (!scelta.equals("E	")));
		}
		while(!scelta.equals("e"));
		
		
		
		
		/*ProvaTest a1 = new ProvaTest();
		System.out.println(a1.generateToken("formenti", new Player("aznors", "kihidui")));*/
		
		//ClientManagerSocket p = new ClientManagerSocket(connection_with_client, server, username)
		/*
		HashMap<String, String> a = new HashMap<String, String>();
		
		a.put("carlo", "formenti");
		a.put("carlo23441", "formenti3443");
		
		Set set = a.entrySet();
		Iterator  iter = set.iterator();
		
		while(iter.hasNext())
		{
			Map.Entry me = (Map.Entry) iter.next();
			System.out.println(me.getValue());
		}
		//a.findMin(a.generateKeyForToken());
		System.out.println(a.generateToken("carlos", new Player("Carlo","Formenti")));
*/
		/**
		 * prova fuma!!!
		 */
//		Game g= new Game();
//		g.createMap();
//		g.stampa();
/*		System.out.println((int)(Math.random() * 29 + 4));
		System.out.println((int)(Math.random() * 29 + 4));
		System.out.println((int)(Math.random() * 29 + 4));
		System.out.println((int)(Math.random() * 29 + 4));
		System.out.println("");
		System.out.println((int)(Math.random() * 30 + 4));//ok(4 a 33)
		System.out.println((int)(Math.random() * 30 + 4));
		System.out.println((int)(Math.random() * 30 + 4));
		System.out.println((int)(Math.random() * 30 + 4));
		System.out.println("");
		System.out.println((int)(Math.random() * 11 + 5));
		System.out.println((int)(Math.random() * 11 + 5));
		System.out.println((int)(Math.random() * 11 + 5));
		System.out.println((int)(Math.random() * 11 + 5));
		System.out.println((int)(Math.random() * 11 + 5));
		System.out.println((int)(Math.random() * 11 + 5));
		System.out.println((int)(Math.random() * 11 + 5));
		System.out.println((int)(Math.random() * 11 + 5));
*/	
	}
}
