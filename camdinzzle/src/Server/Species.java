package Server;
import java.util.ArrayList;
/**
 * 
 * @author Marco
 *
 */

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
		int posRig, posCol;
		myDinosaurs = new ArrayList<Dinosaur>();
		this.name = name;
		this.speciesType = speciesType;
		timeOfLive = 120;
		posRig = (int) (Math.random() * 40);
		posCol = (int) (Math.random() * 40);
		//check mappa vuota!!!
		addDinosaur(getDinoNumber(), posRig, posCol);
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
	public void addDinosaur(int dinoNumber, int posRig, int posCol)
	{
		Dinosaur dino;
		addDinoNumber();
		String dinoId = name + " - " + dinoNumber;
		
		if(speciesType == type.Carnivorous) //Control the type of species
		{
			dino = new Carnivorous(dinoId, posRig, posCol);

		}
		else
		{
			dino = new Vegetarian(dinoId, posRig, posCol);
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
