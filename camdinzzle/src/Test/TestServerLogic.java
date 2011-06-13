package Test;

import java.util.Hashtable;
import Server.*;
import junit.framework.TestCase;

public class TestServerLogic extends TestCase
{
	private ServerLogic sl;
	private Hashtable<String, Player> players;
	private Hashtable<String, Player> loggedPlayers;
	private int size;
	private String actual;
	private String token;
	private Game currentSession;
	
	public void setUp()
	{
		sl = new ServerLogic();
	}
	
	public void testAddNewUser()
	{
		//manda in try catch il metodo
		actual = sl.add_new_user(null, null);
		assertEquals("@no", actual);
		
		//aggiunge correttamente l'utente
		actual = sl.add_new_user("provaUser", "pass");
		players=sl.getPlayers();
		size=players.size();
		assertEquals("@ok", actual);
		assertEquals(1, size);
		
		//cerca di aggiunge un utente gia' esistente
		actual = sl.add_new_user("provaUser", "pass");
		assertEquals("@no,@usernameOccupato",actual);
	}
	
	public void testLogin()
	{
		//manda in try catch il metodo
		actual = sl.login(null, null);
		assertEquals("@no", actual);
		
		//cerca di loggare un utente non esistente
		actual = sl.login("provaUser", "pass");
		assertEquals("@no,@autenticazioneFallita", actual);
		
		//logga correttamente un utente
		sl.add_new_user("provaUser", "pass");
		actual = sl.login("provaUser", "pass");
		token = sl.getTokenFromUsername("provaUser");
		assertEquals("@ok,"+token, actual);
		
		//cerca di loggare un utente gia' loggato
		actual = sl.login("provaUser", "pass");
		assertEquals("@no,@autenticazioneFallita", actual);
		
		//cerca di loggare un utente con la password sbagliata
		actual = sl.login("provaUser", "passErrata");
		assertEquals("@no,@autenticazioneFallita", actual);
	}
	
	public void testAddNewSpecies()
	{
		//manda in try catch il metodo
		actual = sl.addNewSpecies(null, null, null);
		assertEquals("@no,@tokenNonValido",actual);

		//cerca di creare una specie con l'utente non loggato
		sl.add_new_user("provaUser", "pass");
		token = sl.getTokenFromUsername("provaUser");
		actual = sl.addNewSpecies(token, "provaSpecie", "c");
		assertEquals("@no,@tokenNonValido",actual);
		
		//crea correttamente una nuova specie
		sl.login("provaUser", "pass");
		token = sl.getTokenFromUsername("provaUser");
		actual = sl.addNewSpecies(token, "provaSpecie", "c");
		assertEquals("@ok", actual);
		
		//cerca di creare una nuova specie per lo stesso utente
		actual = sl.addNewSpecies(token, "provaSpecieAltra", "c");
		assertEquals("@no", actual);
	
		//cerca di creare una nuova specie con lo stesso nome di un altra
		sl.add_new_user("provaUser2", "pass");
		sl.login("provaUser2", "pass");
		token = sl.getTokenFromUsername("provaUser2");actual = sl.addNewSpecies(token, "provaSpecie", "c");
		assertEquals("@no,@nomeRazzaOccupato", actual);
	}
	
	public void testGameAccess()
	{
		
		//cerca di accedere non essendo loggato
		sl.add_new_user("provaUser", "pass");
		token = sl.getTokenFromUsername("provaUser");
		actual = sl.gameAccess(token);
		assertEquals("@no,@tokenNonValido",actual);
		
		//cerca di accedere con la specie non creata
		sl.login("provaUser", "pass");
		token = sl.getTokenFromUsername("provaUser");
		actual = sl.gameAccess(token);
		assertEquals("@no,@tokenNonValido",actual);
		
		//manda in try catch il metodo
		currentSession = sl.getCurrentSession();
		sl.setCurrentSession(null);
		actual = sl.gameAccess(token);  
		assertEquals("@no", actual);
		
		sl.setCurrentSession(currentSession);
		//accede correttamente alla partita
		sl.addNewSpecies(token, "specie", "c");
		actual = sl.gameAccess(token);
		assertEquals("@ok",actual);
		
		//cerca di accedere in partita dopo aver gia' effettuato l'accesso
		actual = sl.gameAccess(token);
		assertEquals("@no,@tokenNonValido",actual);
		
		//cerca di accedere alla partita con gia' il numero massimo di giocatori in essa
		for(int i=0; i<8; i++)
		{
			sl.add_new_user("user"+i, "pass");
			sl.login("user"+i, "pass");
			token = sl.getTokenFromUsername("user"+i);
			sl.addNewSpecies(token, "specie"+i, "c");
			sl.gameAccess(token);
		}
		sl.login("provaUser", "pass");
		token = sl.getTokenFromUsername("provaUser");
		actual = sl.gameAccess(token);
		assertEquals("@no,@troppiGiocatori",actual);
		
	}
}
