/**
 * @author Forme
 * created		 	28/04/2011
 * last modified	06/05/2011 
 */

package Client;

import java.io.*;
import java.util.ArrayList;

import Server.Game;



public class Textual implements Visual 
{

	public Textual()
	{
		// TODO: azioni da definire
	}
	
	// Inutile implementarlo
	@Override
	public void drawMap(ArrayList<String> mapList) 
	{
		
	}

	/**
	 * Stampa a video la richista di id del dinosauro e lo legge
	 * @return Id del dinosauro(inserito dall'utente)
	 */
	public String getDinoId()
	{
		BufferedReader dataInput = new BufferedReader(new InputStreamReader(System.in));
		try
		{
			System.out.println("Inserisci l'id del dinosauro:\n");
			return dataInput.readLine();
		}
		catch(IOException ex)
		{
			System.out.println("Errore nella ricezione dell'input.");
			ex.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Stampa a video la vista locale del dinosaruro
	 * @param dinoId Id del dinosaruo di cui stampare la vista locale
	 * @param msg Messaggio del ServerLogic
	 */
	@Override
	public void drawDinoZoom(String dinoId, ArrayList<String> dinoZoomList) 
	{ 
//		if(ClientMessageBroker.checkMessage(msg))
		{
			//ArrayList<String> dinoZoomList = ClientMessageBroker.manageDinoZoom(msg);
			// Alla posizione 2 dell'arraylist c'è il numero delle righe della vista
			int rowsNumber = Integer.parseInt(dinoZoomList.get(2).trim());
			// Alla posizione 3 dell'arraylist c'è il numero delle colonne della vista
			int columnsNumber = Integer.parseInt(dinoZoomList.get(3).trim());
			int columnsCount = 0;
			System.out.println("Dinosaur's " + dinoId + " zoom:");
			System.out.print("|");
			for(int k = 0; k<columnsCount; k++)
			{
			System.out.print("_______");
			}
			
			for(int i = dinoZoomList.size()-1; i>=4; i--)
			{
				if(dinoZoomList.get(i)!=null)
				{
					if(columnsCount == columnsNumber)
					{
						System.out.print("\n|");
						for(int k = 0; k<columnsCount; k++)
						{
						System.out.print("_______|");
						}
						System.out.print("\n|");
						columnsCount = 0;
					}
					// Se non è un elemento che ha energia o un ID come parametro lo metto al centro della cella di out
					if(dinoZoomList.get(i).indexOf(',') == -1)
						System.out.print("   " + dinoZoomList.get(i) + "   |");
					else
						// Numero riferente all'energia o al dinoId con 3 cifre sempre
						System.out.print("  " + dinoZoomList.get(i) + "  |");
				}
				else
				{
					System.out.print("    b    |");
				}
				columnsCount++;
			}
			System.out.print("\n");
			for(int k = 0; k<columnsCount; k++)
			{
			System.out.print("_______|");
			}
			System.out.print("\n");
		}
//		else
//			drawError(msg);
	}

	/**
	 * Stampa a video la lista dei dinosauri del giocatore
	 * @param msg Messaggio del ServerLogic
	 */
	@Override
	public void drawDinoList(String[] msgDinoList) 
	{
//		if(ClientMessageBroker.checkMessage(msg))
		{
//			String[] dinoList = ClientMessageBroker.manageDinoList(msg);
			System.out.println("Dinosaurs list:\n");
			
			for (String dino : msgDinoList) 
			{
				System.out.println("	" + dino +"\n");
			}
		}
//		else
//			drawError(msg);
	}
	
	/**
	 * Stampa a video lo stato di un dinosauro
	 * @param dinoId Id del dinosauro
	 * @param msg Messaggio del ServerLogic
	 */
	public void drawDinoState(String dinoId, String[] dinoState)
	{
//		if(ClientMessageBroker.checkMessage(msg))
		{
//			String[] dinoState = ClientMessageBroker.manageDinoState(msg);
			System.out.println("Dinosaur's state " + dinoId + " of player " + dinoState[0] + ":");
			System.out.println("	race: " + dinoState[1]);
			System.out.println("	type: " + dinoState[2]);
			System.out.println("	dinosaur's position: " + dinoState[3] + ", " + dinoState[4]);
			System.out.println("	dimension: " + dinoState[5]);
			
			// Se il dinosauro appartiene al giocatore allora ci sono delle informazioni aggiuntive
			if(dinoState.length > 6)
			{
				System.out.println("	energy: " + dinoState[6]);
				System.out.println("	round lived: " + dinoState[7]);
			}
		}
//		else
//			drawError(msg);
	}

	@Override
	public void drawTime(int timeInt) 
	{
		
	}

	@Override
	public void drawConnectionState(String msg) 
	{
	
	}
	
	/**
	 * Stampa a video i messaggi per fare introdurre al giocatore l'id del dinosauro e le coordinate della
	 * nuova posizione
	 */
	public String[] drawDinoMovement()
	{
		BufferedReader dataInput = new BufferedReader(new InputStreamReader(System.in));
		String[] returnValues = new String[3];
		
		try
		{
			System.out.println("Inserisci l'id del dinosauro da spostare: \n");
			returnValues[0] = dataInput.readLine();
			System.out.println("Inserisci le riga su cui spostare il dinosauro: \n");
			returnValues[1] = dataInput.readLine();
			System.out.println("Inserisci le riga su cui spostare il dinosauro: \n");
			returnValues[2] = dataInput.readLine();
		}
		catch(IOException ex)
		{
			System.out.println("Errore nella ricezione dell'input.");
			ex.printStackTrace();
		}
		
		return returnValues;
	}
	
	/**
	 * Stampa a video la classifica dei giocatori
	 * @param msg Messaggio del ServerLogic
	 */
	@Override
	public void drawRanking(ArrayList<String> rankingList) 
	{
		//TODO sistemare estremi for!!! nullPointerException
//		if(ClientMessageBroker.checkMessage(msg))
		{
//			ArrayList<String> rankingList = ClientMessageBroker.manageRanking(msg);
			System.out.println("Classifica della partita:");
			
			if(rankingList != null)
			{
				for(int i = 0; i<rankingList.size(); i = i+4) 
				{
					System.out.println("	username: " + rankingList.get(i));
					System.out.println("	razza: " + rankingList.get(i+1));
					System.out.println("	punteggio: " + rankingList.get(i+2));
					System.out.println("	in partita: " + rankingList.get(i+3) + "\n");
				}
			}
		}
//		else
//			drawError(msg);
	}
	
	/**
	 * Stampa a video l'errore avvenuto durante la comunicazione con il ServerLogic. In particolare viene 
	 * stampato il nome dell'errore ricevuto nella stringa ricevuta dal ServerLogic
	 * @param msg Messaggio del ServerLogic
	 */
	public void drawError(String msg)
	{
		String[] errorString = ClientMessageBroker.splitMessage(msg);
		System.out.println("Errore: " + errorString[0].substring(1));
	}
	
	/**
	 * Permette di inserire l'username e la password dell'utente
	 * @return Un array contenente in ordine l'username e la password inseriti
	 */
	public String[] drawLogin()
	{
		BufferedReader dataInput = new BufferedReader(new InputStreamReader(System.in));
		String[] returnValues = new String[2];
		
		try
		{
			System.out.println("Login");
			System.out.println("Username:");
			returnValues[0] = dataInput.readLine();
			System.out.println("Password:");
			returnValues[1] = dataInput.readLine();
		}
		catch(IOException ex)
		{
			System.out.println("Errore nella ricezione dell'input.");
			ex.printStackTrace();
		}
		
		return returnValues;
	}
	
	/**
	 * Permette di inserire l'username e la password
	 * @return Un array contenente in ordine l'username e la password inseriti
	 */
	public String[] drawUserCreation()
	{
		BufferedReader dataInput = new BufferedReader(new InputStreamReader(System.in));
		String[] returnValues = new String[2];
		
		try
		{
			System.out.println("Creazione nuovo utente");
			System.out.println("Username:");
			returnValues[0] = dataInput.readLine();
			System.out.println("Password:");
			returnValues[1] = dataInput.readLine();
		}
		catch(IOException ex)
		{
			System.out.println("Errore nella ricezione dell'input.");
			ex.printStackTrace();
		}
		
		return returnValues;
	}
	
	/**
	 * Permette di inserire il nome e il tipo della razza scelta 
	 * @return Un array contenente in ordine in nome e il tipo della razza inseriti dall'utente
	 */
	public String[] drawRaceCreation()
	{
		BufferedReader dataInput = new BufferedReader(new InputStreamReader(System.in));
		String[] race = new String[2];
		
		try
		{
			System.out.println("Inserire il nome della razza dei dinosauri:\n");
			race[0] = dataInput.readLine();
			do
			{
				System.out.println("Inserire il tipo della razza dei dinosauri('c' o 'e'):\n");
				race[1] = dataInput.readLine();
			}
			while((!race[1].equals("c")) && (!race[1].equals("e")));
		}
		catch(IOException ex)
		{
			System.out.println("Errore nella ricezione dell'input.");
			ex.printStackTrace();
		}
		
		return race;
	}
	
	/**
	 * Stampa a video la lista degli username dei giocatopri in partita
	 * @param msg Messaggio del ServerLogic
	 */
	public void drawPlayerList(String[] playerList)
	{		
//		if(ClientMessageBroker.checkMessage(msg))
		{
//			String[] playerList = ClientMessageBroker.managePlayerList(msg);
			
			if(playerList == null)
				System.out.println("Non ci sono giocatori in partita.");
			else
			{
				System.out.println("Giocatori in partita:");
				
				for(int i = 0; i<playerList.length; i++) 
				{
					System.out.println("	" + playerList[i]);
				}
			}
		}
//		else
//			drawError(msg);
	}
	
	/**
	 * Stampa a video la mappa generale del giocatore
	 */
	public void drawGeneralMap(String msg)
	{
		if(ClientMessageBroker.checkMessage(msg))
		{
			ArrayList<String> map = new ArrayList<String>();
			int columnsCount = 0;
			
			map = ClientMessageBroker.manageGeneralMap(msg);
			
			for(int i=2; i<Game.maxRow*Game.maxCol+2; i++)
			{			
				if(columnsCount == Game.maxCol)
				{
					System.out.print("\n");
					columnsCount = 0;
				}
				
				System.out.print(map.get(i) + " ");
				columnsCount++;
			}
			
			System.out.print("\n");
		}
		else
			drawError(msg);
	}
	
}
