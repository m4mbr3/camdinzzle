package Server;
import java.util.ArrayList;
import java.util.Hashtable;
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
	private Hashtable<String, Dinosaur> myDinosaurs;
	
	protected static enum type {Carnivorous, Vegetarian}
	private static type speciesType;
	private int  dinoNumber;
	private Object[][] map;
	private int score;
	private String playerUsername;
	private final int maxDino=10;
	
	/**
	 * Create a new species, set name, type, timeOfLive and add one dinosaur
	 */
	public Species(String name, type speciesType, String username)
	{
		int posRig, posCol;
		
		myDinosaurs = new Hashtable<String, Dinosaur>();
		
		this.name = name;
		Species.speciesType = speciesType;
		timeOfLive = 120;
		dinoNumber = 0;
		score = 0;
		playerUsername = username;
		do
		{
			posRig = (int) (Math.random() * 40);
			posCol = (int) (Math.random() * 40);
		}while(!((Game.getCell(posRig, posCol) instanceof String)&&(((String)Game.getCell(posRig, posCol)).compareTo("t")==0)));
		addDinosaur(posRig, posCol);
		
		startMap();
		updateMap();
		return;
	}
	
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	public void updateTimeOfLive()
	{
		timeOfLive -= 1;
	}
	
	public int getTimeOfLive()
	{
		return timeOfLive;
	}
	
	public void increaseScore()
	{
		// TODO: aumentare lo score dove ogni dinosauro vale 1+D punti dove D è la sua dimensione
		Iterator iter = this.getDinosaurs();
		
		while(iter.hasNext())
		{
			Map.Entry me =(Map.Entry)iter.next();
			score = score + 1 + ((Dinosaur)me.getValue()).getDinoDimension();
		}
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
	public Dinosaur addDinosaur( int posRig, int posCol)
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
		dino.setLocalMap();
		
		return dino;
	}
	
	/**
	 * @return Un iteratore contenente i dinosauri delle specie
	 */
	public Iterator getDinosaurs()
	{
		if(myDinosaurs.size() > 0)
		{
			Set set = myDinosaurs.entrySet();
			
			return set.iterator();
		}
		else
			return null;
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
		Set set = myDinosaurs.entrySet();
		Iterator iter = set.iterator();
		
		while(iter.hasNext())
		{
			Map.Entry me = (Map.Entry) iter.next();
			((Dinosaur)me.getValue()).updateDinosaurAge();
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
	
	public type getType()
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
			int sizeRow = dino.getSizeRowLocalMap();
			int sizeCol = dino.getSizeColLocalMap();
			int startRow = dino.getPosRow() - sizeRow/2;
			int endRow = startRow + sizeRow-1;
			int startCol = dino.getPosCol() - sizeCol/2;
			int endCol = startCol + sizeCol-1;
			if(startRow < 0)
				startRow = 0;
			if(startCol < 0)
				startCol = 0;
			if(endRow > Game.maxRow)
				endRow = Game.maxRow;
			if(endCol > Game.maxCol)
				endCol = Game.maxCol;
			int k=0;
			for(int i=startRow; i<=endRow; i++)
			{
				int h=0;
				for(int j=startCol; j<=endCol; j++)
				{
					map[i][j] = localMap[k][h].toString();
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
			else if(((Vegetarian)dinoId).getVegetation()!=null)
			{
				Game.setCellMap(((Vegetarian)dinoId).getVegetation(), dinoId.getPosRow(), dinoId.getPosCol());
			}
			else
			{
				Game.setCellMap("t",dinoId.getPosRow(), dinoId.getPosCol());
			}
		}
		myDinosaurs.remove(dinoId.getDinoId());
	}
	
	/**
	 * imposta una cella della mappa generale del giocatore con la stringa in msg e nella posizione row, col
	 * @param msg
	 * @param row
	 * @param col
	 */
	public void setCellMap(String msg, int row, int col)
	{
		map[row][col] = msg;
	}
	
	public boolean ckeckDinoNumber()
	{
		if(dinoNumber<=maxDino)
			return true;
		else
			return false;
	}
}
