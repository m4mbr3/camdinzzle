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
	private int punteggio;
	private String playerUsername;
	
	/**
	 * Create a new species, set name, type, timeOfLive and add one dinosaur
	 */
	public Species(String name, type speciesType, String username)
	{
		int posRig, posCol;
		
		myDinosaurs = new HashMap<String, Dinosaur>();
		
		this.name = name;
		Species.speciesType = speciesType;
		timeOfLive = 120;
		dinoNumber = 0;
		punteggio = 0;
		playerUsername = username;
		do
		{
			posRig = (int) (Math.random() * 40);
			posCol = (int) (Math.random() * 40);
		}while((Game.getCell(posRig, posCol) instanceof String)&&(((String)Game.getCell(posRig, posCol)).compareTo("t")==0));
		

		
		addDinosaur(posRig, posCol);
		startMap();
		updateMap();
		return;
	}
	
	public int getPunteggio() {
		return punteggio;
	}

	public void setPunteggio(int punteggio) {
		this.punteggio = punteggio;
	}
	
	public String getPlayerUsername() {
		return playerUsername;
	}

	public void setPlayerUsername(String playerUsername) {
		this.playerUsername = playerUsername;
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
	 * @param dinoId : ID del dinosauro
	 * @return Il riferimento al dinosauro se esiste un dinosauro avente come id dinoId, null altrimenti
	 */
	public Dinosaur getDino(String dinoId)
	{
		return myDinosaurs.get(dinoId);
	}
	
	/**
	 * add a new dinosaur the same type of my species
	 * at my HashTable of dinosaurs
	 */
	public String addDinosaur( int posRig, int posCol)
	{
		Dinosaur dino;
		addDinoNumber();
		String dinoId = this.name + " - " + getDinoNumber();
		
		if(speciesType == type.Carnivorous) //Control the type of species
		{
			dino = new Carnivorous(dinoId, posRig, posCol, this);

		}
		else
		{
			dino = new Vegetarian(dinoId, posRig, posCol, this);
		}

		myDinosaurs.put(dinoId.toString(), dino);
		Game.setCellMap(dino, posRig, posCol);
		
		return dinoId;
	}
	
	/**
	 * @return Un iteratore contenente i dinosauri delle specie
	 */
	public Iterator<Dinosaur> getDinosaurs()
	{
		synchronized (myDinosaurs) 
		{
			Set set = myDinosaurs.entrySet();
			
			return set.iterator();
		}
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
		}
		timeOfLive -= 1;
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
	 * aggiorna la mappa generale del giocatore (buio) inserendo la vista dei dinosauri
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
					if((i>=0)&&(i<Game.maxRow)&&(j>=0)&&(j<Game.maxCol)&&(h>=0)&&(h<size)&&(k>=0)&&(k<size))
					{
						if(localMap[k][h]!=null)
						{
							map[i][j] = null;
							map[i][j] = localMap[k][h].toString();
						}
					}
					h++;
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
	
	public void stampa()
	{

		for(int i=0; i<Game.maxRow; i++)
		{
			for(int j=0; j<Game.maxCol; j++)
			{
				System.out.print(map[i][j] + " ");
			}
			System.out.print("\n");
		}
	}
	
	public void killDino(Dinosaur dinoId)
	{
		if(speciesType==type.Carnivorous)
		{
			if(((Carnivorous)dinoId).getVegetation()!=null)
			{
				Game.setCellMap(((Carnivorous)dinoId).getVegetation(), dinoId.getPosRow(), dinoId.getPosCol());
			}
			else
			{
				Game.setCellMap("t",dinoId.getPosRow(), dinoId.getPosCol());
			}
		}
		else
		{
			if(((Vegetarian)dinoId).getCarrion()!=null)
			{
				Game.setCellMap(((Vegetarian)dinoId).getCarrion(), dinoId.getPosRow(), dinoId.getPosCol());
			}
			else
			{
				Game.setCellMap("t",dinoId.getPosRow(), dinoId.getPosCol());
			}
		}
		myDinosaurs.remove(dinoId.getDinoId());
	}
}
