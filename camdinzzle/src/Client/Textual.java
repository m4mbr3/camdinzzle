/**
 * @author Forme
 * created		 	28/04/2011
 * last modified	06/05/2011 
 */

package Client;

import java.io.*;
import java.util.ArrayList;



public class Textual implements Visual 
{

	public Textual()
	{
		// TODO: azioni da definire
	}
	
	// Inutile implementarlo
	@Override
	public void drawMap(String msg) 
	{
	
	}

	/**
	 * Stampa a video la vista locale del dinosaruro
	 * @param dinoId Id del dinosaruo di cui stampare la vista locale
	 * @param msg Messaggio del Server
	 */
	@Override
	public void drawDinoZoom(String dinoId, String msg) 
	{ 
		if(ClientMessageBroker.checkMessage(msg))
		{
			ArrayList<String> dinoZoomList = ClientMessageBroker.manageDinoZoom(msg);
			// Alla posizione 2 dell'arraylist c'è il numero delle righe della vista
			int rowsNumber = Integer.parseInt(dinoZoomList.get(3).trim());
			// Alla posizione 3 dell'arraylist c'è il numero delle colonne della vista
			int columnsNumber = Integer.parseInt(dinoZoomList.get(2).trim());
			System.out.print("Dinosaur's " + dinoId + " zoom:");
			
			for(int i = 4; i<dinoZoomList.size(); i++)
			{
				if(((i-1)%columnsNumber) == 0)
				{
					System.out.print("\n _______ _______ _______");
					System.out.print("\n|");
				}
				// Se non è un elemento che ha energia o un ID come parametro lo metto al centro della cella di out
				if(dinoZoomList.get(i).indexOf(',') == -1)
					System.out.print("   " + dinoZoomList.get(i) + "   |");
				else
					// Numero riferente all'energia o al dinoId con 3 cifre sempre
					System.out.print(" " + dinoZoomList.get(i) + " |");
			}
		}
		else
			drawError(msg);
	}

	/**
	 * Stampa a video la lista dei dinosauri del giocatore
	 * @param msg Messaggio del Server
	 */
	@Override
	public void drawDinoList(String msg) 
	{
		if(ClientMessageBroker.checkMessage(msg))
		{
			String[] dinoList = ClientMessageBroker.manageDinoList(msg);
			System.out.println("Dinosaurs list:\n");
			
			for (String dino : dinoList) 
			{
				System.out.println("	" + dino +"\n");
			}
		}
		else
			drawError(msg);
	}
	
	/**
	 * Stampa a video lo stato di un dinosauro
	 * @param dinoId Id del dinosauro
	 * @param msg Messaggio del Server
	 */
	public void drawDinoState(String dinoId, String msg)
	{
		if(ClientMessageBroker.checkMessage(msg))
		{
			String[] dinoState = ClientMessageBroker.manageDinoState(msg);
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
		else
			drawError(msg);
	}

	@Override
	public void drawTime(String msg) 
	{
		
	}

	@Override
	public void drawConnectionState(String msg) 
	{
	
	}

	// TODO
	public void drawDinoMovement(String msg)
	{
		if(ClientMessageBroker.checkMessage(msg))
		{
			String dinoMovement = ClientMessageBroker.manageDinoMove(msg);
		}
		else
			drawError(msg);
	}
	
	/**
	 * Stampa a video la classifica dei giocatori
	 * @param msg Messaggio del Server
	 */
	@Override
	public void showRanking(String msg) 
	{
		if(ClientMessageBroker.checkMessage(msg))
		{
			ArrayList<String> rankingList = ClientMessageBroker.manageRanking(msg);
			System.out.println("Classifica della partita:");
			
			for(int i = 0; i<rankingList.size(); i = i+4) 
			{
				System.out.println("	username: " + rankingList.get(i));
				System.out.println("	razza: " + rankingList.get(i+1));
				System.out.println("	punteggio: " + rankingList.get(i+2));
				System.out.println("	in partita: " + rankingList.get(i+3) + "\n");
			}
		}
		else
			drawError(msg);
	}
	
	/**
	 * Stampa a video l'errore avvenuto durante la comunicazione con il Server. In particolare viene 
	 * stampato il nome dell'errore ricevuto nella stringa ricevuta dal Server
	 * @param msg Messaggio del Server
	 */
	public void drawError(String msg)
	{
		String[] errorString = ClientMessageBroker.splitMessage(msg);
		System.out.println("Errore: " + errorString[1].substring(1));
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
	 * @return Un array contenente in ordine in nome e il tipom della razza inseriti dall'utente
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
				System.out.println("Inserire il tipo della razza dei dinosauri('c' o 'e':\n");
				race[1] = dataInput.readLine();
			}
			while((!race[1].equals("c")) || (!race[1].equals("e")));
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
	 * @param msg Messaggio del Server
	 */
	public void drawPlayerList(String msg)
	{		
		if(ClientMessageBroker.checkMessage(msg))
		{
			String[] playerList = ClientMessageBroker.managePlayerList(msg);
			System.out.println("Giocatori in partita:");
			
			for(int i = 0; i<playerList.length; i++) 
			{
				System.out.println("	" + playerList[i]);
			}
		}
		else
			drawError(msg);
	}
	
}
