package Server;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
/**
 * 
 * @author Marco
 *
 */
import java.util.HashMap;

/**
 * gruppo di dinosauri appartenenti a un giocatore
 */
public class Species 
{
	private String name;
	private int timeOfLive;
	private HashMap<String, Dinosaur> myDinosaurs;
	
	private enum type {Carnivorous, Vegetarian}
	private type speciesType;
	private int  dinoNumber;
	
	/**
	 * Create a new species, set name, type, timeOfLive and add one dinosaur
	 */
	public Species(String name, type speciesType)
	{
		int posRig, posCol;
		
		myDinosaurs = new HashMap<String, Dinosaur>();
		
		this.name = name;
		this.speciesType = speciesType;
		timeOfLive = 120;
		dinoNumber = 0;
		posRig = (int) (Math.random() * 40);
		posCol = (int) (Math.random() * 40);
		//check mappa vuota!!!
		addDinosaur(posRig, posCol);
		return;
	}
	
	/**
	 * add one at number of dinosaurs of species
	 */
	public void addDinoNumber()
	{
		dinoNumber += 1;
		return;
	}
	
	/**
	 * return number of dinosaurs of species
	 */
	public int getDinoNumber()
	{
		return dinoNumber;
	}
	
	/**
	 * add a new dinosaur the same type of my species
	 * at my HashTable of dinosaurs
	 */
	public void addDinosaur( int posRig, int posCol)
	{
		Dinosaur dino;
		addDinoNumber();
		String dinoId = name + " - " + getDinoNumber();
		
		if(speciesType == type.Carnivorous) //Control the type of species
		{
			dino = new Carnivorous(dinoId, posRig, posCol);

		}
		else
		{
			dino = new Vegetarian(dinoId, posRig, posCol);
		}

		myDinosaurs.put(dinoId.toString(), dino);
		
		return;
	}
	
	/**
	 * riconosce l'id del dinosauro e restituisce l'oggetto
	 */
	public Dinosaur identifyDinosaur(String dinoId)
	{
		if(myDinosaurs.containsKey(dinoId))
			return myDinosaurs.get(dinoId);
		return null;
	}
	
	/**
	 * aggiorna stato di ogni dinosauro alla fine del turno
	 */
	public void upDateDinosaurStatus()
	{
		Set set = myDinosaurs.keySet();
		Iterator keys = set.iterator();
		
		while(keys.hasNext())
		{
			myDinosaurs.get(keys).updateDinosaurAge();
			timeOfLive -= 1;
		}
	}
	/*
	 * tornano il tipo enum
	 */
	public static type getCarnType()
	{
		return type.Carnivorous;
	}
	
	public static type getVegType()
	{
		return type.Vegetarian;
	}

	
	
	 public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
}
