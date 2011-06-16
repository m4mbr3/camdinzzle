package Test;

import java.io.File;

import Server.Carrion;
import Server.Game;
import Server.Player;
import Server.Vegetation;
import junit.framework.TestCase;

public class TestGame extends TestCase
{
	private Game game;
	
	public void setUp()
	{
		File f = new File("server.ser");
		f.delete();
		game = new Game(null);
	}
	
	public void tearDown()
	{
		
	}
	
	public void testCreateMap()
	{
		Object[][] map = game.getGeneralMap();
		
		int water = 0;
		int carrion = 0;
		int vegetation = 0;
		int land = 0;
		
		// cicla la mappa e controlla che ci sia il numero giusto di caselle di acqua, di carogne e di vegetazione
		for(int i = 0; i < Game.maxRow; i++)
		{
			for(int j = 0; j < Game.maxCol; j++)
			{
				if(map[i][j] instanceof String)
				{
					if(map[i][j].equals("a"))
					{
						water++;
					}
					else
					{
						land++;
					}
				}
				else if(map[i][j] instanceof Carrion)
				{
					carrion++;
				}
				else if(map[i][j] instanceof Vegetation)
				{
					vegetation++;
				}
			}
		}
		System.out.println(land + ", " + vegetation + ", " + carrion + ", " + water);
		assertEquals(320, water);
		assertEquals(20, carrion);
		assertEquals(512, vegetation);
	}
	
	public void testAddPlayer()
	{
		String token = "token1";
		boolean check = game.addPlayer(token, new Player("provaUser1", "pass"));
		
		assertEquals(true, check);
	}
	
	public void testRemovePlayer()
	{
		String token = "token1";
		game.addPlayer(token, new Player("provaUser1", "pass"));
		
		boolean check = game.removePlayer(token);
		
		assertEquals(true, check);
	}
}
