package Test;

import java.io.File;

import Server.Carnivorous;
import Server.Dinosaur;
import Server.Game;
import Server.Species;

import junit.framework.TestCase;

public class TestSpecies extends TestCase 
{
	private Species specie;
	private Game game;
	private Object[][] map;
	
	public void setUp()
	{
		File f = new File("server.ser");
		f.delete();
		game = new Game(null);
		specie = new Species("specieProva", Species.getCarnType(), "player");
		map = game.getGeneralMap();
	}
	
	public void tearDown()
	{
		
	}
	
	public void testAddDinosaur()
	{
		Dinosaur actual;
		int row=1;
		int col=1;
		boolean isLand = false;
		for(int i = 0; i < Game.maxRow; i++)
		{
			for(int j = 0; j < Game.maxCol; j++)
			{
				if((map[i][j] instanceof String)&&(map[i][j].equals("t")))
				{
					row = i;
					col = j;
					isLand=true;
					break;
				}
			}
			if(isLand)
			{
				break;
			}
		}
		int dinoNumber = specie.getMyDinosaurs().size();
		//creo un nuovo dinosauro sulla terra e controllo che lo posiziona correttamente
		actual = specie.addDinosaur(row, col);
		assertEquals(Game.getCell(row, col), actual);
		//controllo che ci sia un dino in piu' nella tabella della specie
		assertEquals(dinoNumber+1, specie.getMyDinosaurs().size());
		
	}
	
	public void testIcreaseScore()
	{
		int scoreFinal = specie.getScore() + specie.getDino("specieProva - 1").getDinoDimension() + 1;
		specie.increaseScore();
		assertEquals(scoreFinal, specie.getScore());
	}
	
	public void testKillDino()
	{
		Object cell;
		Dinosaur dino = specie.getDino("specieProva - 1");
		int row = dino.getPosRow();
		int col = dino.getPosCol(); 
		int dinoNumber = specie.getMyDinosaurs().size();
		
		if(((Carnivorous)dino).getVegetation()!=null)
		{
			cell = ((Carnivorous)dino).getVegetation();
		}
		else 
		{
			cell = new String("t");
		}
		
		specie.killDino(dino, false);
		//controllo che ci sia nella cella quello che cera precedentemente
		assertEquals(cell, Game.getCell(row, col));
		//controllo che ci sia un dinosauro in meno nella tabella dei dinosauri
		assertEquals(dinoNumber-1, specie.getMyDinosaurs().size());
		
	}
	
	
}
