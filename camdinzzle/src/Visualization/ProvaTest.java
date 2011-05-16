package Visualization;

import java.util.HashMap;
import java.util.Hashtable;

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
		ProvaTest a = new ProvaTest();
		//ClientManagerSocket p = new ClientManagerSocket(connection_with_client, server, username)
		
		//a.findMin(a.generateKeyForToken());
		System.out.println(a.generateToken("carlos", new Player("Carlo","Formenti")));

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
