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

public class ProvaTest1 {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//cms cms = new cms(); 
		Textual text = new Textual();
		ConnectionManagerSocket cms = null;
		ClientListener pl = null;
		
		String [] arr;
		String msg, scelta = "";
		BufferedReader dataInput = new BufferedReader(new InputStreamReader(System.in));
		String token = "";
		
		do
		{
			System.out.println("Possibilità di azioni:\n");
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
			System.out.println("MC per stampare tutta la mappa senza buoio");
			System.out.println("RM per stampare la mappa di raggiungibilita'");
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
						
						cms = new ConnectionManagerSocket(34567, "localhost", arr[0], arr[1]);
						pl = new ClientListener(34567, "localhost", arr[0], arr[1]);
						
						System.out.println("Client: " + msg);
						System.out.println("cms: " + cms.creaUtente(msg));
					}
					else if(scelta.equals("LOGIN"))
					{
						arr = text.drawLogin();
						msg = ClientMessageBroker.createLogin(arr[0], arr[1]);
						
						System.out.println("Client: " + msg);
						msg = cms.login(msg);
						System.out.println("cms: " + msg);
						
						token = ClientMessageBroker.manageLogin(msg)[0];
					}
					else if(scelta.equals("CR"))
					{
						arr = text.drawRaceCreation();
						msg = ClientMessageBroker.createRace(token, arr[0], arr[1]);
						
						System.out.println("Client: " + msg);
						System.out.println("cms: " + cms.creaRazza(msg));
					}
					else if(scelta.equals("AP"))
					{
						// TODO: prima di accedere all partia, imporre la creazione di una razza
						msg = ClientMessageBroker.createGameAccess(token);
						
						System.out.println("Client: " + msg);
						System.out.println("cms: " + cms.accessoPartita(msg));
					}
					else if(scelta.equals("UP"))
					{
						msg = ClientMessageBroker.createGameExit(token);
						
						System.out.println("Client: " + msg);
						System.out.println("cms: " + cms.uscitaPartita(msg));
					}
					else if(scelta.equals("LG"))
					{
						msg = ClientMessageBroker.createPlayerList(token);
						System.out.println("Client: " + msg);
						msg = cms.listaGiocatori();
						System.out.println("cms: " + msg);
						
						text.drawPlayerList(msg);
					}
					else if(scelta.equals("CG"))
					{
						msg = ClientMessageBroker.createRanking(token);
						System.out.println("Client: " + msg);
						msg = cms.classifica();
						System.out.println("cms: " + msg);
						
						text.drawRanking(msg);
					}
					else if(scelta.equals("LOGOUT"))
					{
						msg = ClientMessageBroker.createLogout(token);
						System.out.println("Client: " + msg);
						msg = cms.logout();
						System.out.println("cms: " + msg);
					}
					else if(scelta.equals("GM"))
					{
						msg = ClientMessageBroker.createGeneralMap(token);
						System.out.println("Client: " + msg);
						msg = cms.mappaGenerale();
						System.out.println("cms: " + msg);
						
						text.drawGeneralMap(msg);
					}
					else if(scelta.equals("LD"))
					{
						msg = ClientMessageBroker.createGeneralMap(token);
						System.out.println("Client: " + msg);
						msg = cms.listaDinosauri();
						System.out.println("cms: " + msg);
						
						text.drawDinoList(msg);
					}
					else if(scelta.equals("VL"))
					{
						String dinoId = text.getDinoId();
						
						if(dinoId != null)
						{
							msg = ClientMessageBroker.createDinoZoom(token, dinoId);
							System.out.println("Client: " + msg);
							msg = cms.vistaLocale(msg);
							System.out.println("cms: " + msg);
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
							msg = cms.statoDinosauro(msg);
							System.out.println("cms: " + msg);
							text.drawDinoState(dinoId, msg);
						}
						
					}
					else if(scelta.equals("MD"))
					{
						arr = text.drawDinoMovement();
						msg = ClientMessageBroker.createDinoMove(token, arr[0], arr[1], arr[2]);
						
						System.out.println("Client: " + msg);
						msg = cms.muoviDinosauro(arr[0], arr[1], arr[2]);
						System.out.println("cms: " + msg);
					
					}
					else if(scelta.equals("CD"))
					{
						String dinoId = text.getDinoId();
						msg = ClientMessageBroker.createDinoGrowUp(token, dinoId);
						
						System.out.println("Client: " + msg);
						msg = cms.cresciDinosauro(dinoId);
						System.out.println("cms: " + msg);

					}
					else if(scelta.equals("DU"))
					{
						String dinoId = text.getDinoId();
						msg = ClientMessageBroker.createNewEgg(token, dinoId);
						
						System.out.println("Client: " + msg);
						msg = cms.deponiUovo(dinoId);
						System.out.println("cms: " + msg);
					}
					else if(scelta.equals("CT"))
					{
						msg = ClientMessageBroker.createRoundConfirmation(token);
						System.out.println("Client: " + msg);
						msg = cms.confermaTurno();
						System.out.println("cms: " + msg);
					}
					else if(scelta.equals("PT"))
					{
						msg = ClientMessageBroker.createPassOffRound(token);
						System.out.println("Client: " + msg);
						msg = cms.passaTurno();
						System.out.println("cms: " + msg);
						
						System.out.println("Turno del giocatore: " + msg);
					}
					else if(scelta.equals("MC"))
						Game.stampa();
					
					else if(scelta.equals("RM"))
					{
						BufferedReader dataInput1 = new BufferedReader(new InputStreamReader(System.in));
						int[] returnValues = new int[2];
						System.out.println("Stampa mappa raggiungibilita'");
						System.out.println("Riga:");
						returnValues[0] = Integer.parseInt(dataInput.readLine());
						System.out.println("Colonna:");
						returnValues[1] = Integer.parseInt(dataInput.readLine());
						Game.stampaReachAble(returnValues[0], returnValues[1]);
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
				&& (!scelta.equals("CD")) && (!scelta.equals("DU")) && (!scelta.equals("CT")) && (!scelta.equals("PT")) && (!scelta.equals("MC")) && (!scelta.equals("RM"))
				&& (!scelta.equals("E")));
		}
		while(!scelta.equals("E")); 
		
		
		
		
		/*ProvaTest1 a1 = new ProvaTest1();
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
