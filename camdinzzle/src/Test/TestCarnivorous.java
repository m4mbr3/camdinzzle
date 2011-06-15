package Test;

import java.io.File;

import Server.Carnivorous;
import Server.Carrion;
import Server.Dinosaur;
import Server.Game;
import Server.Species;
import Server.Vegetarian;
import Server.Vegetation;
import junit.framework.TestCase;

public class TestCarnivorous extends TestCase
{
	private Game game;
	private Object[][] map;
	
	public void setUp()
	{
		File f = new File("server.ser");
		f.delete();
		game = new Game(null);
		map = game.getGeneralMap();
	}
	
	public void tearDown()
	{
		
	}
	
	public void testEat()
	{
		int finalEnergy = 0;
		boolean isCarrion = false;
		Carnivorous dino = new Carnivorous("dino - 1", 3, 4, new Species("playerSpecies", Species.getCarnType(), "player"));
		
		// mangia una carogna della mappa e controllo se aumenta energia del dinosauro
		for(int i = 0; i < Game.maxRow; i++)
		{
			for(int j = 0; j < Game.maxCol; j++)
			{
				if(map[i][j] instanceof Carrion)
				{
					finalEnergy += ((Carrion)map[i][j]).getPower() + dino.getEnergy();
					dino.eat(map[i][j]);
					isCarrion = true;
					break;
				}
			}
			if(isCarrion)
			{
				break;
			}
		}
		assertEquals(finalEnergy, dino.getEnergy());
		
		boolean isVegetation = false;
		finalEnergy = dino.getEnergy();
		// mangia una casella di vegetazione della mappa e controllo che l'energia rimane uguale a prima
		for(int i = 0; i < Game.maxRow; i++)
		{
			for(int j = 0; j < Game.maxCol; j++)
			{
				if(map[i][j] instanceof Vegetation)
				{
					dino.eat(map[i][j]);
					isVegetation = true;
					break;
				}
			}
			if(isVegetation)
			{
				break;
			}
		}
		assertEquals(finalEnergy, dino.getEnergy());
	}
	
	public void testFight()
	{
		// testa il combattimento fra un carnivoro ed un erbivoro dopo  che il carnivoro e' cresciuto
		Dinosaur dino = new Carnivorous("dino - 1", 3, 4, new Species("playerSpecies", Species.getCarnType(), "player"));
		Dinosaur enemyDino = new Vegetarian("dino - 1", 3, 4, new Species("playerSpecies", Species.getCarnType(), "player"));
		
		dino.growUp();
		boolean actual = dino.fight(enemyDino);
		
		assertEquals(true, actual);
	}
}
