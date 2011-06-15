package Test;

import java.io.File;

import Server.Game;
import Server.Species;

import junit.framework.TestCase;

public class TestSpecies extends TestCase 
{
	private Species specie;
	private Game game;
	
	public void setUp()
	{
		File f = new File("server.ser");
		f.delete();
		game = new Game(null);
		specie = new Species("specieProva", Species.getCarnType(), "player");
	}
	
	public void tearDown()
	{
		
	}
	
	public void testAddDinosaur()
	{
		
	}
}
