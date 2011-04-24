package Server;
import java.util.ArrayList;

/**
 * gruppo di dinosauri appartenenti a un giocatore
 */
public class Species 
{
	private String name;
	private int timeOfLive;
	private ArrayList<Dinosaur> myDinosaurs;
	private enum type {Carnivorous, Vegetarian}
	private type speciesType;
	
	/**
	 * Create a new species, set name, type, timeOfLive and add one dinosaur
	 */
	public Species(String name, type speciesType)
	{
		myDinosaurs = new ArrayList<Dinosaur>();
		this.name = name;
		this.speciesType = speciesType;
		timeOfLive = 120;
		addDinosaur();
	}
	
	/**
	 * add a new dinosaur the same type of my species
	 * at my array of dinosaurs
	 */
	public void addDinosaur()
	{
		Dinosaur dino;
		
		if(speciesType == type.Carnivorous) //Control the type of species
		{
			dino = new Carnivorous();

		}
		else
		{
			dino = new Vegetarian();
		}

		myDinosaurs.add(dino);
		return;
	}
}
