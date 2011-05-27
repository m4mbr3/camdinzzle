package Visualization;


import Client.*;
import Server.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.KeyStore.Entry;
import java.util.ArrayList;
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
		ArrayList<String> requestQueue = new ArrayList<String>();
		
		String [] arr;
		String msg, scelta = "";
		BufferedReader dataInput = new BufferedReader(new InputStreamReader(System.in));
		String token = "";
		
		
		try {
			ServerRMIInterface server = (ServerRMIInterface)Naming.lookup("rmi://127.0.0.1/server:1099");
			System.out.println(server.add_new_user("cio", "io"));
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		do
		{
			System.out.println("PossibilitÓ di azioni:\n");
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
						
						MonitorMessage mm = new MonitorMessage();
						Socket soc = new Socket("localhost", 34567);
						
						requestQueue.add("creaUtente");
						
						cms = new ConnectionManagerSocket(34567, "localhost", arr[0], arr[1], mm, soc);
						pl = new ClientListener(34567, "localhost", arr[0], mm, soc);
						
						System.out.println("Client: " + msg);
						System.out.println("cms: " + cms.creaUtente(arr[0], arr[1]));
					}
					
					else if(scelta.equals("LOGIN"))
					{
						arr = text.drawLogin();
						msg = ClientMessageBroker.createLogin(arr[0], arr[1]);
						
						requestQueue.add("login");
						
						System.out.println("Client: " + msg);
						msg = cms.login(arr[0], arr[1]);
						System.out.println("cms: " + msg);
						
						token = ClientMessageBroker.manageLogin(msg)[1];
					}
					else if(scelta.equals("CR"))
					{
						arr = text.drawRaceCreation();
						msg = ClientMessageBroker.createRace(token, arr[0], arr[1]);
						
						requestQueue.add("creaRazza");
						
						System.out.println("Client: " + msg);
						System.out.println("cms: " + cms.creaRazza(arr[0], arr[1]));
					}
					else if(scelta.equals("AP"))
					{
						// TODO: prima di accedere all partia, imporre la creazione di una razza
						msg = ClientMessageBroker.createGameAccess(token);
						
						requestQueue.add("accessoPartita");
						
						System.out.println("Client: " + msg);
						System.out.println("cms: " + cms.accessoPartita());
					}
					else if(scelta.equals("UP"))
					{
						msg = ClientMessageBroker.createGameExit(token);
						
						requestQueue.add("uscitaPartita");
						
						System.out.println("Client: " + msg);
						System.out.println("cms: " + cms.uscitaPartita());
					}
					else if(scelta.equals("LG"))
					{
						msg = ClientMessageBroker.createPlayerList(token);
						System.out.println("Client: " + msg);
						
						requestQueue.add("listaGiocatori");
						
						String[] response = cms.listaGiocatori();
						
						text.drawPlayerList(ClientMessageBroker.managePlayerList(msg));
					}
					else if(scelta.equals("CG"))
					{
						msg = ClientMessageBroker.createRanking(token);
						System.out.println("Client: " + msg);
						
						requestQueue.add("classifica");
						
						ArrayList<String> a = cms.classifica();
						
						text.drawRanking(ClientMessageBroker.manageRanking(msg));
					}
					else if(scelta.equals("LOGOUT"))
					{
						msg = ClientMessageBroker.createLogout(token);
						System.out.println("Client: " + msg);
						
						requestQueue.add("logout");
						
						msg = cms.logout();
						System.out.println("cms: " + msg);
					}
					else if(scelta.equals("GM"))
					{
						msg = ClientMessageBroker.createGeneralMap(token);
						System.out.println("Client: " + msg);
						
						requestQueue.add("mappaGenerale");
						
						ArrayList<String> a = cms.mappaGenerale();
						System.out.println("cms: " + msg);
						
						text.drawGeneralMap(msg);
					}
					else if(scelta.equals("LD"))
					{
						msg = ClientMessageBroker.createGeneralMap(token);
						System.out.println("Client: " + msg);
						
						requestQueue.add("listaDinosauri");
						
						String[] a = cms.listaDinosauri();
						System.out.println("cms: " + msg);
						
						text.drawDinoList(ClientMessageBroker.manageDinoList(msg));
					}
					else if(scelta.equals("VL"))
					{
						String dinoId = text.getDinoId();
						
						if(dinoId != null)
						{
							msg = ClientMessageBroker.createDinoZoom(token, dinoId);
							System.out.println("Client: " + msg);
							
							requestQueue.add("vistaLocale");
							
							ArrayList<String> a = cms.vistaLocale(msg);
							System.out.println("cms: " + msg);
							text.drawDinoZoom(dinoId, ClientMessageBroker.manageDinoZoom(msg));
						}
						
					}
					else if(scelta.equals("SD"))
					{
						String dinoId = text.getDinoId();
						
						if(dinoId != null)
						{
							msg = ClientMessageBroker.createDinoState(token, dinoId);
							System.out.println("Client: " + msg);
							
							requestQueue.add("statoDinosauro");
							
							String[] a = cms.statoDinosauro(dinoId);
							System.out.println("cms: " + msg);
							text.drawDinoState(dinoId, ClientMessageBroker.manageDinoState(msg));
						}
						
					}
					else if(scelta.equals("MD"))
					{
						arr = text.drawDinoMovement();
						msg = ClientMessageBroker.createDinoMove(token, arr[0], arr[1], arr[2]);
						
						System.out.println("Client: " + msg);
						
						requestQueue.add("muoviDinosauro");
						
						String[] a = cms.muoviDinosauro(arr[0], arr[1], arr[2]);
						System.out.println("cms: " + msg);
					
					}
					else if(scelta.equals("CD"))
					{
						String dinoId = text.getDinoId();
						msg = ClientMessageBroker.createDinoGrowUp(token, dinoId);
						
						System.out.println("Client: " + msg);
						
						requestQueue.add("cresciDinosauro");
						
						String[] a = cms.cresciDinosauro(dinoId);
						System.out.println("cms: " + msg);

					}
					else if(scelta.equals("DU"))
					{
						String dinoId = text.getDinoId();
						msg = ClientMessageBroker.createNewEgg(token, dinoId);
						
						System.out.println("Client: " + msg);
						
						requestQueue.add("deponiUovo");
						
						String[] a = cms.deponiUovo(dinoId);
						System.out.println("cms: " + msg);
					}
					else if(scelta.equals("CT"))
					{
						msg = ClientMessageBroker.createRoundConfirmation(token);
						System.out.println("Client: " + msg);
						
						requestQueue.add("confermaTurno");
						
						String[] a = cms.confermaTurno();
						System.out.println("cms: " + msg);
					}
					else if(scelta.equals("PT"))
					{
						msg = ClientMessageBroker.createPassOffRound(token);
						System.out.println("Client: " + msg);
						
						requestQueue.add("passaTurno");
						
						String[] a = cms.passaTurno();
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
		
	}
}
