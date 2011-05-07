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
	private int  dinoNumber = 1;
	
	/**
	 * Create a new species, set name, type, timeOfLive and add one dinosaur
	 */
	public Species(String name, type speciesType)
	{
		int posX, posY;
		myDinosaurs = new ArrayList<Dinosaur>();
		this.name = name;
		this.speciesType = speciesType;
		timeOfLive = 120;
		posX = (int) (Math.random() * 40);
		posY = (int) (Math.random() * 40);
		//check mappa vuota!!!
		addDinosaur(getDinoNumber(), posX, posY);
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
	 * at my array of dinosaurs
	 */
	public void addDinosaur(int dinoNumber, int posX, int posY)
	{
		Dinosaur dino;
		addDinoNumber();
		String dinoId = name + " - " + dinoNumber;
		
		if(speciesType == type.Carnivorous) //Control the type of species
		{
			dino = new Carnivorous(dinoId, posX, posY);

		}
		else
		{
			dino = new Vegetarian(dinoId, posX, posY);
		}

		myDinosaurs.add(dino);
		return;
	}
	
	/**
	 * riconosce l'id del dinosauro e restituisce l'oggetto
	 */
	public Dinosaur identifyDinosaur(String dinoId, ArrayList<Dinosaur> myDinosaur)
	{
		for(Dinosaur dino : myDinosaur)
		{
			if(dinoId == dino.getDinoId())
			{
				return dino;
			}
		}
		return null;
	}
	
	/**
	 * aggiorna stato di ogni dinosauro alla fine del turno
	 */
	public void upDateDinosaurStatus(ArrayList<Dinosaur> myDinosaur)
	{
		for(Dinosaur dino : myDinosaur)
		{
			dino.updateDinosaurAge();
			timeOfLive -= 1;
		}
	}
}
