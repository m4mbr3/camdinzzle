package Test;

import java.io.File;
import java.util.Hashtable;

import Client.ClientMessageBroker;
import Server.*;
import junit.framework.TestCase;

public class TestServerLogic extends TestCase
{
	private ServerLogic sl;
	private Server server;
	private Hashtable<String, Player> players;
	private int size;
	private String actual;
	private String token;
	private Game currentSession;
	private String dinoId;
	private String[] dinoList;
	private Dinosaur dino;
	private Hashtable<String, Dinosaur> myDinosaurs;
	
	public void setUp()
	{
		File f = new File("server.ser");
		f.delete();
		sl = new ServerLogic();
	}
	
	public void tearDown()
	{
		
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
			sl.addNewSpecies(token, "specie"+i, "v");
			sl.gameAccess(token);
		}
		sl.login("provaUser", "pass");
		token = sl.getTokenFromUsername("provaUser");
		actual = sl.gameAccess(token);
		assertEquals("@no,@troppiGiocatori",actual);
		
	}
	
	public void testGameExit()
	{

		server = new Server(4567, sl, "1099", "server");
		sl.setServer(server);
		sl.add_new_user("provaUser", "pass");
		
		//tenta di uscire dalla partita non essendo loggato
		actual = sl.gameExit("tokenProva");
		assertEquals("@no,@tokenNonValido", actual);
		
		sl.login("provaUser", "pass");
		//tenta di uscire dalla partita non essendo entrato
		token = sl.getTokenFromUsername("provaUser");
		actual = sl.gameExit(token);
		assertEquals("@no,@tokenNonValido", actual);
		
		sl.addNewSpecies(token, "provaSpecie", "c");
		actual = sl.gameAccess(token);
		//esce correttamente dalla partita
		actual = sl.gameExit(token);
		assertEquals("@ok", actual);
		
		sl.setCurrentSession(null);
		//manda in try catch il metodo
		actual = sl.gameExit(token);
		assertEquals("@no", actual);
	}
	
	public void testPlayerList()
	{
		sl.add_new_user("provaUser", "pass");
		
		//chiede la lista dinosauri non essendo loggato
		actual = sl.playerList("tokenProva");
		assertEquals("@no,@tokenNonValido", actual);
		
		sl.login("provaUser", "pass");
		token = sl.getTokenFromUsername("provaUser");
		//restituisce correttamente la lista giocatori con nessuno in partita
		actual = sl.playerList(token);
		assertEquals("@listaGiocatori", actual);
		
		sl.addNewSpecies(token, "provaSpecie", "c");
		sl.gameAccess(token);
		//restituisce correttamente la lista giocatori con dei giocatori in partita
		actual = sl.playerList(token);
		assertEquals("@listaGiocatori,provaUser", actual);
	}
	
	public void testRanking()
	{
		sl.add_new_user("provaUser", "pass");
		
		//chiede la classifica non essendo loggato
		actual = sl.ranking("tokenProva");
		assertEquals("@no,@tokenNonValido", actual);
		
		//restituisce la classifica corretta vuota
		sl.login("provaUser", "pass");
		token = sl.getTokenFromUsername("provaUser");
		actual = sl.ranking(token);
		assertEquals("@classifica", actual);
		
		//restituisce la classifica corretta con un utente
		sl.addNewSpecies(token, "provaSpecie", "c");
		actual = sl.ranking(token);
		assertEquals("@classifica,{provaUser,provaSpecie,0,s}", actual);
		
		sl.setRank(null);
		//manda in try catch il metodo
		actual = sl.ranking(token);
		assertEquals("@no", actual);
	}
	
	public void testLogout()
	{
		sl.add_new_user("provaUser", "pass");
		
		//chiede la classifica non essendo loggato
		actual = sl.logout("tokenProva");
		assertEquals("@no,@tokenNonValido", actual);
		
		//effettua logout correttamente
		sl.login("provaUser", "pass");
		token = sl.getTokenFromUsername("provaUser");
		actual = sl.logout(token);
		assertEquals("@ok", actual);
		
		sl.login("provaUser", "pass");
		token = sl.getTokenFromUsername("provaUser");
		sl.setCurrentSession(null);
		//manda in try catch il metodo
		actual = sl.logout(token);
		assertEquals("@no", actual);
	}
	
	public void testGeneralMap()
	{
		sl.add_new_user("provaUser", "pass");
		
		//chiede la mappa generale non essendo loggato
		actual = sl.generalMap("tokenProva");
		assertEquals("@no,@tokenNonValido", actual);
		
		//chiede la mappa generale non essendo in partita
		sl.login("provaUser", "pass");
		token = sl.getTokenFromUsername("provaUser");
		actual = sl.generalMap(token);
		assertEquals("@no,@nonInPartita", actual);
		
		sl.login("provaUser", "pass");
		token = sl.getTokenFromUsername("provaUser");
		sl.setCurrentSession(null);
		//manda in try catch il metodo
		actual = sl.generalMap(token);
		assertEquals("@no", actual);
		
		//non e' stato effettuato il test sulla risposta positiva della mappa
		//perche' viene generata casualmente
		
	}
	
	public void testDinosaursList()
	{
		sl.add_new_user("provaUser", "pass");
		
		//chiede la lista dinosauri non essendo loggato
		actual = sl.dinosaursList("tokenProva");
		assertEquals("@no,@tokenNonValido", actual);
		
		sl.login("provaUser", "pass");
		token = sl.getTokenFromUsername("provaUser");
		//chiede la lista dinosauri non avendo una specie
		actual = sl.dinosaursList(token);
		assertEquals("@listaDinosauri", actual);
		
		sl.addNewSpecies(token, "provaSpecie", "c");
		//chiede la lista dinosauri non essendo in partita
		actual = sl.dinosaursList(token);
		assertEquals("@no,@nonInPartita", actual);
		
		
		sl.gameAccess(token);
		//restituisce correttamente la lista dinosauri
		actual = sl.dinosaursList(token);
		assertEquals("@listaDinosauri,provaSpecie - 1", actual);
		
		currentSession = sl.getCurrentSession();
		currentSession.getPlayer(token).getSpecie().setMyDinosaurs(new Hashtable<String, Dinosaur>());
		//chiede la lista dinosauri non avendo la tabella dei dinosauri nella propria specie
		actual = sl.dinosaursList(token);
		assertEquals("@listaDinosauri", actual);
		
		sl.setCurrentSession(null);
		//manda in try catch il metodo
		actual = sl.dinosaursList(token);
		assertEquals("@no", actual);
	}
	
	public void testDinoZoom()
	{
		sl.add_new_user("provaUser", "pass");
		
		//chiede la mappa locale non essendo loggato
		actual = sl.dinoZoom("tokenProva","dinoIdProva");
		assertEquals("@no,@tokenNonValido", actual);
		
		//chiede la mappa locale non essendo in partita
		sl.login("provaUser", "pass");
		token = sl.getTokenFromUsername("provaUser");
		actual = sl.dinoZoom(token,"dinoIdProva");
		assertEquals("@no,@nonInPartita", actual);
		
		//chiede la mappa locale con un id sbagliato
		sl.addNewSpecies(token, "provaSpecie", "c");
		sl.gameAccess(token);
		actual = sl.dinoZoom(token,"dinoIdProva");
		assertEquals("@no,@idNonValido", actual);
		
		sl.setCurrentSession(null);
		//manda in try catch il metodo
		actual = sl.generalMap(token);
		assertEquals("@no", actual);
		
		//non e' stato effettuato il test sulla risposta positiva della mappa
		//locale perche' e' dipendente dalla mappa generale casuale
	}
	
	public void testDinoState()
	{
		sl.add_new_user("provaUser", "pass");
		
		//chiede lo stato dinosauro non essendo loggato
		actual = sl.dinoState("tokenProva","dinoIdProva");
		assertEquals("@no,@tokenNonValido", actual);
		
		//chiede lo stato dinosauro non essendo in partita
		sl.login("provaUser", "pass");
		token = sl.getTokenFromUsername("provaUser");
		actual = sl.dinoState(token,"dinoIdProva");
		assertEquals("@no,@nonInPartita", actual);
		
		//chiede lo stato dinosauro di un id non valido
		sl.addNewSpecies(token, "provaSpecie", "c");
		sl.gameAccess(token);
		actual = sl.dinoState(token, "dinoIdTest");
		assertEquals("@no,@idNonValido", actual);
		
		//chiede lo stato dinosauro non avendo la tabella dinosauri nella propria specie
		currentSession = sl.getCurrentSession();
		myDinosaurs = currentSession.getPlayer(token).getSpecie().getMyDinosaurs();
		currentSession.getPlayer(token).getSpecie().setMyDinosaurs(null);
		actual = sl.dinoState(token, "dinoIdTest");
		assertEquals("@no", actual);
		currentSession.getPlayer(token).getSpecie().setMyDinosaurs(myDinosaurs);
		
		//chiede lo stato di un dinosauro che e' nella vista locale del proprio dinosauro
		//ma non appartiene ne all'utente e nemmeno agli avversari
		dinoList = ClientMessageBroker.manageDinoList(sl.dinosaursList(token));
		dinoId = dinoList[0];
		dino = currentSession.getPlayer(token).getSpecie().getDino(dinoId);
		int row = dino.getPosRow();
		int col = dino.getPosCol();
		boolean positioned = false;
		for(int i=row-1; i<row+2; i++ )
		{
			for(int j=col-1; j<col+2; j++)
			{
				if((i>=0)&&(i<Game.maxRow)&&(j>=0)&&(j<Game.maxCol))
				{					
					if((!(Game.getCell(i, j) instanceof String))||(((String)Game.getCell(i, j)).compareTo("a")!=0))
					{
						Carnivorous dinoNew = new Carnivorous("provaDino", i, j, currentSession.getPlayer(token).getSpecie());
						Game.setCellMap(dinoNew, i, j);
						positioned = true;
						break;
					}
					
				}
			}
			if(positioned) break;
		}
		actual = sl.dinoState(token, "provaDino");
		assertEquals("@no,@idNonValido", actual);
		
	}
	
	public void testDinoMove()
	{
		//manda in try catch il metodo
		actual = sl.dinoMove(null, null, null, null);
		assertEquals("@no", actual);
		
		sl.add_new_user("provaUser", "pass");
		//cerca di muovere un dinosauro non essendo loggato
		actual = sl.dinoMove(token, "dinoId", "5", "5");
		assertEquals("@no,@tokenNonValido", actual);
		
		//cerca di muovere un dinosauro non avendo effettuato l'accesso
		sl.login("provaUser", "pass");
		token = sl.getTokenFromUsername("provaUser");
		actual = sl.dinoMove(token, "dinoId", "5", "5");
		assertEquals("@no,@nonInPartita", actual);
		
		//cerca di muovere un dinosauro ma non e' il suo turno
		token = sl.getTokenFromUsername("provaUser");
		sl.addNewSpecies(token, "provaSpecie", "c");
		sl.gameAccess(token);
		sl.setTokenOfCurrentPlayer("provaToken");
		actual = sl.dinoMove(token, "dinoId", "5", "5");
		assertEquals("@no,@nonIlTuoTurno", actual);
		
		sl.setTokenOfCurrentPlayer(token);
		//cerca di muovere un dinosauro con id sbagliato
		actual = sl.dinoMove(token, "dinoId", "5", "5");
		assertEquals("@no,@idNonValido", actual);
		
		dinoList = ClientMessageBroker.manageDinoList(sl.dinosaursList(token));
		dinoId = dinoList[0];
		//cerca di muovere un dinosauro fuori dalla mappa
		actual = sl.dinoMove(token, dinoId, "45", "45");
		assertEquals("@no,@destinazioneNonValida", actual);
		
		currentSession = sl.getCurrentSession();
		dino = currentSession.getPlayer(token).getSpecie().getDino(dinoId);
		//cerca di muovere un dinosauro in una destinazione non raggiungibile
		int dinoRow = dino.getPosRow();
		int dinoCol = dino.getPosCol();
		int offSetRow,offSetCol;
		if(dinoRow<30)
			offSetRow = +4;
		else
			offSetRow = -4;
		if(dinoCol<30)
			offSetCol = +4;
		else
			offSetCol = -4;
		actual = sl.dinoMove(token, dinoId, String.valueOf(dinoRow+offSetRow), String.valueOf(dinoCol+offSetCol));
		assertEquals("@no,@destinazioneNonValida", actual);
		
		//cerca di muovere un vegetariano su un altro vegetariano
		
		//cerca di muovere il dinosauro ma non ha abbastanza energia
		
		//cerca di muovere un dinosauro avendolo gia mosso
		//nello stesso turno
		
		//muove il dinosauro su un altro dinosauro, combatte e vince
		
		//muove il dinosauro su un altro dinosauro, combatte e perde
		
		//muove il dinosauro
	}
	
	public void testDinoGrowUp()
	{	
		sl.add_new_user("provaUser", "pass");
		
		//cerca di far crescere un dinosauro senza essere loggato
		actual = sl.dinoGrowUp("provaToken", "dinoIdTest");  
		assertEquals("@no,@tokenNonValido", actual);
		
		//cerca di far crescere un dinosauro non avendo effettuato l'accesso
		sl.login("provaUser", "pass");
		token = sl.getTokenFromUsername("provaUser");
		actual = sl.dinoGrowUp(token, "dinoIdTest");
		assertEquals("@no,@nonInPartita", actual);
		
		//manda in try catch il metodo
		currentSession = sl.getCurrentSession();
		sl.setCurrentSession(null);
		actual = sl.dinoGrowUp(token, dinoId);  
		assertEquals("@no", actual);
		
		sl.setCurrentSession(currentSession);
		//cerca di far crescere un dinosauro ma non e' il suo turno
		sl.addNewSpecies(token, "provaSpecie", "c");
		sl.gameAccess(token);
		sl.setTokenOfCurrentPlayer("provaToken");
		actual = sl.dinoGrowUp(token, "dinoIdTest");
		assertEquals("@no,@nonIlTuoTurno", actual);
		
		sl.setTokenOfCurrentPlayer(token);
		//cerca di far crescere un dinosauro con id sbagliato
		actual = sl.dinoGrowUp(token, "dinoIdTest");
		assertEquals("@no,@idNonValido", actual);
		
		dinoList = ClientMessageBroker.manageDinoList(sl.dinosaursList(token));
		dinoId = dinoList[0];
		//cresce correttamente il dinosauro
		actual = sl.dinoGrowUp(token, dinoId);
		assertEquals("@ok", actual);
		
		//cerca di far crescere un dinosauro dopo aver gia' effettuato una mossa
		actual = sl.dinoGrowUp(token, dinoId);
		assertEquals("@no,@raggiuntoLimiteMosseDinosauro", actual);
		
		currentSession = sl.getCurrentSession();
		dino = currentSession.getPlayer(token).getSpecie().getDino(dinoId);
		//cerca di far crescere un dinosauro ma non ha abbastanza energia
		dino.setEnergy(10);
		dino.setActionTake(false);
		actual = sl.dinoGrowUp(token, dinoId);
		assertEquals("@no,@mortePerInedia", actual);
	}
	
	public void testDinoNewEgg()
	{
		sl.add_new_user("provaUser", "pass");
		
		//cerca di deporre l'uovo senza essere loggato
		actual = sl.dinoNewEgg(token, "dinoIdTest");
		assertEquals("@no,@tokenNonValido", actual);
		
		//cerca di deporre l'uovo non avendo effettuato l'accesso
		sl.login("provaUser", "pass");
		token = sl.getTokenFromUsername("provaUser");
		actual = sl.dinoNewEgg(token, "dinoIdTest");
		
		//manda in try catch il metodo
		currentSession = sl.getCurrentSession();
		sl.setCurrentSession(null);
		actual = sl.dinoNewEgg(token, dinoId);  
		assertEquals("@no", actual);
		
		sl.setCurrentSession(currentSession);
		//cerca di deporre l'uovo ma non e' il suo turno
		sl.addNewSpecies(token, "provaSpecie", "c");
		sl.gameAccess(token);
		sl.setTokenOfCurrentPlayer("provaToken");
		actual = sl.dinoNewEgg(token, "dinoIdTest");
		assertEquals("@no,@nonIlTuoTurno", actual);
		
		sl.setTokenOfCurrentPlayer(token);
		//cerca di deporre l'uovo con id sbagliato
		actual = sl.dinoNewEgg(token, "dinoIdTest");
		assertEquals("@no,@idNonValido", actual);
		
		dinoList = ClientMessageBroker.manageDinoList(sl.dinosaursList(token));
		dinoId = dinoList[0];
		currentSession = sl.getCurrentSession();
		dino = currentSession.getPlayer(token).getSpecie().getDino(dinoId);
		//depone un uovo e crea un nuovo dinosauro
		dino.setEnergy(2000);
		actual = sl.dinoNewEgg(token, dinoId);
		assertEquals("@ok,provaSpecie - 2", actual);
		
		//cerca di deporre l'uovo dopo aver gia' effettuato una mossa
		actual = sl.dinoNewEgg(token, dinoId);
		assertEquals("@no,@raggiuntoLimiteMosseDinosauro", actual);
		
		//cerca di deporre l'uovo ma non ha abbastanza energia
		dino.setActionTake(false);
		actual = sl.dinoNewEgg(token, dinoId);
		assertEquals("@no,@mortePerInedia", actual);
		
		dinoList = ClientMessageBroker.manageDinoList(sl.dinosaursList(token));
		dinoId = dinoList[0];
		dino = currentSession.getPlayer(token).getSpecie().getDino(dinoId);
		int max = currentSession.getPlayer(token).getSpecie().getMaxDino();
		//cerca di deporre l'uovo dopo avendo raggiunto il numero massimo di dinosauri
		for(int i=0; i<max; i++)
		{
			dino.setEnergy(2000);
			dino.setActionTake(false);
			actual = sl.dinoNewEgg(token, dinoId);
		}
		dino.setEnergy(2000);
		dino.setActionTake(false);
		actual = sl.dinoNewEgg(token, dinoId);
		assertEquals("@no,@raggiuntoNumeroMaxDinosauri", actual);
		
	}
	
	public void testRoundConfirm()
	{
		sl.add_new_user("provaUser", "pass");
		
		// conferma il turno non essendo loggato
		actual = sl.roundConfirm(token);
		assertEquals("@no,@tokenNonValido", actual);
		
		// conferma il turno anche se non e' in partita
		sl.login("provaUser", "pass");
		token = sl.getTokenFromUsername("provaUser");
		actual = sl.roundConfirm(token);
		assertEquals("@no,@nonInPartita", actual);
		
		// conferma il turno anche se non e' il suo turno e conferma il turno se e' il suo turno
		sl.add_new_user("provaUser2", "pass");
		sl.login("provaUser2", "pass");
		sl.addNewSpecies(sl.getTokenFromUsername("provaUser2"), "provaUser2Specie", "c");
		sl.gameAccess(sl.getTokenFromUsername("provaUser2"));
		actual = sl.roundConfirm(sl.getTokenFromUsername("provaUser2"));
		assertEquals("@ok", actual);
		
		sl.addNewSpecies(token, "provaUser1Specie", "c");
		sl.gameAccess(token);
		actual = sl.roundConfirm(token);
		assertEquals("@no,@nonIlTuoTurno", actual);
		
		
	}
	
	public void testPlayerRoundSwitch()
	{
		sl.add_new_user("provaUser", "pass");
		
		// passa il turno non essendo loggato
		actual = sl.playerRoundSwitch(token);
		assertEquals("@no,@tokenNonValido", actual);
		
		// passa il turno anche se non e' in partita
		sl.login("provaUser", "pass");
		token = sl.getTokenFromUsername("provaUser");
		actual = sl.playerRoundSwitch(token);
		assertEquals("@no,@nonInPartita", actual);
		
		// passa il turno anche se non e' il suo turno e passa il turno se e' il suo turno (due diversi giocatori)
		sl.add_new_user("provaUser2", "pass");
		sl.login("provaUser2", "pass");
		sl.addNewSpecies(sl.getTokenFromUsername("provaUser2"), "provaUser2Specie", "c");
		sl.gameAccess(sl.getTokenFromUsername("provaUser2"));
		actual = sl.playerRoundSwitch(sl.getTokenFromUsername("provaUser2"));
		assertEquals("@ok", actual);
		
		sl.addNewSpecies(token, "provaUser1Specie", "c");
		sl.gameAccess(token);
		actual = sl.playerRoundSwitch(token);
		assertEquals("@no,@nonIlTuoTurno", actual);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
