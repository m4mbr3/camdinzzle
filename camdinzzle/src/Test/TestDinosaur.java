package Test;

import java.io.File;

import Server.Carnivorous;
import Server.Game;
import Server.Species;
import Server.Vegetarian;
import junit.framework.TestCase;

public class TestDinosaur extends TestCase
{
	private Vegetarian dinoVeg;
	private Carnivorous dinoCarn;
	private Species veg;
	private Species carn;
	private Game game;
	
	public void setUp()
	{
		File f = new File("server.ser");
		if(f.exists())
			f.delete();
		
		game = new Game(null);
		game.getFirstPlayer();
		veg = new Species("veg", Species.getVegType(), "player");
		carn = new Species("carn", Species.getCarnType(), "player");
		dinoVeg = new Vegetarian("veg - 1", 5, 5, veg);
		dinoCarn = new Carnivorous("carn - 1", 10, 10, carn);
	}

	public void testGrowUp()
	{
		boolean actual;
		
		//crece e all'energia toglie la meta' di quella massima, la dimensione aumenta, l'energia massima aumenta
		int energyFinal= (dinoVeg.getEnergy()-(dinoVeg.getDinoDimension()*500));
		int dimensionFinal = dinoVeg.getDinoDimension()+1;
		actual = dinoVeg.growUp();
		assertEquals(true, actual);
		assertEquals(energyFinal, dinoVeg.getEnergy());
		assertEquals(dimensionFinal, dinoVeg.getDinoDimension());
		
		dinoVeg.setEnergy(10);
		//non ha abbastanza energia per crescere
		actual = dinoVeg.growUp();
		assertEquals(false, actual);
		assertEquals(dimensionFinal, dinoVeg.getDinoDimension());
		
		
		//non cresce piu' della dimensione 5
		for(int i=0; i<4; i++)
		{
			dinoVeg.setEnergy(dinoVeg.getDinoDimension()*1000);
			dinoVeg.growUp();
		}
		dinoVeg.setEnergy(dinoVeg.getDinoDimension()*1000);
		actual = dinoVeg.growUp();
		assertEquals(false, actual);
	}
	
	public void testNewEgg()
	{
		String actual;
		int dinoNumber;
		
		//crea correttamente un nuovo dinosauro e lo posiziona nella prima terra vicina
		dinoCarn.setEnergy(1600);
		actual = dinoCarn.newEgg();
		dinoNumber = carn.getDinoNumber();
		assertEquals("carn - "+dinoNumber, actual);
	}
}
