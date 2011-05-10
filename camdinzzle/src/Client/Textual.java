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

	@Override
	/**
	 * @param msg: messaggio del Server
	 */
	public void drawDinoZoom(String dinoId, String msg) 
	{ 
		if(ClientMessageBroker.checkMessage(msg))
		{
			ArrayList<String> dinoZoomList = ClientMessageBroker.manageDinoZoom(msg);
			// Alla posizione 2 dell'arraylist c'è il numero delle righe della vista
			int rowsNumber = Integer.parseInt(dinoZoomList.get(2).trim());
			// Alla posizione 3 dell'arraylist c'è il numero delle colonne della vista
			int columnsNumber = Integer.parseInt(dinoZoomList.get(3).trim());
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

	public void drawDinoMovement(String msg)
	{
		if(ClientMessageBroker.checkMessage(msg))
		{
			String dinoMovement = ClientMessageBroker.manageDinoMove(msg);
		}
		else
			drawError(msg);
	}
	
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
	
	public void drawError(String msg)
	{
		String[] errorString = ClientMessageBroker.splitMessage(msg);
		System.out.println("Errore: " + errorString[1].substring(1));
	}
	
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
	
	public String drawRaceCreation()
	{
		BufferedReader dataInput = new BufferedReader(new InputStreamReader(System.in));
		String race = null;
		
		try
		{
			System.out.println("Inserire il nome della razza dei dinosauri:\n");
			race = dataInput.readLine();
		}
		catch(IOException ex)
		{
			System.out.println("Errore nella ricezione dell'input.");
			ex.printStackTrace();
		}
		
		return race;
	}
	
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
