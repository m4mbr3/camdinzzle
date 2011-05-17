package Server;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
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
	
	protected static enum type {Carnivorous, Vegetarian}
	private static type speciesType;
	private int  dinoNumber;
	private Object[][] map;
	
	/**
	 * Create a new species, set name, type, timeOfLive and add one dinosaur
	 */
	public Species(String name, type speciesType)
	{
		int posRig, posCol;
		
		myDinosaurs = new HashMap<String, Dinosaur>();
		
		this.name = name;
		Species.speciesType = speciesType;
		timeOfLive = 120;
		dinoNumber = 0;
		posRig = (int) (Math.random() * 40);
		posCol = (int) (Math.random() * 40);
		//check mappa vuota!!!
		// TODO Game.getLocalMap da sistemare che non funzionano
		addDinosaur(posRig, posCol);
		startMap();
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
		Iterator iter = set.iterator();
		
		while(iter.hasNext())
		{
			Map.Entry<String, Dinosaur> me = (Map.Entry<String, Dinosaur>) iter.next();
			me.getValue().updateDinosaurAge();
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
	
	public static type getType()
	{
		return speciesType;
	}

	
	
	public String getName() 
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	/**
	 * crea una mappa generale del giocatore tutta buia
	 */
	public void startMap()
	{
		map = new Object[Game.maxRow][Game.maxCol];
		
		for(int i=0; i<Game.maxRow; i++)
		{
			for(int j=0; j<Game.maxCol; j++)
			{
				map[i][j] = new String ("b");
			}
		}		
	}
	
	/**
	 * aggiorna la mappa generale del giocatore inserendo la vista dei dinosauri
	 */
	public void updateMap()
	{
		Set set = myDinosaurs.entrySet();
		Iterator iter = set.iterator();
		Dinosaur dino;
		while(iter.hasNext())
		{
			Map.Entry<String, Dinosaur> me = (Map.Entry<String, Dinosaur>) iter.next();
			dino = me.getValue();
			Object[][] localMap = dino.getLocalMap();
			int size = dino.getSizeLocalMap();
			int posRow = dino.getPosRow();
			int posCol = dino.getPosCol();
			int k=0;
			for(int i=posRow-size/2; i<=posRow+size/2; i++)
			{
				int h=0;
				for(int j=posCol-size/2; j<=posCol+size/2; j++)
				{
					if((k>=0)&&(k<Game.maxRow)&&(h>=0)&&(h<Game.maxCol))
					{
						map[i][j] = localMap[k][h].toString();
						h++;
					}
				}
				k++;
			}
		}
	}
	
	/**
	 * 
	 * @return map mappa generale del player con celle visibili solo quelle giˆ visitate.
	 */
	public Object[][] getPlayerMap()
	{
		return map;
	}
}
