package Server;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author Marco
 *
 */
public class Species implements Serializable
{
	private static final long serialVersionUID = 1L;
	/**
	 * nome della specie
	 */
	private String name;
	/**
	 * turni mancanti alla estizione
	 */
	private int timeOfLive;
	/**
	 * tabella dei dinosauri in gioco
	 */
	private Hashtable<String, Dinosaur> myDinosaurs;
	/**
	 * razza di dinosauri: Carnivoro o Vegetariano
	 */
	protected static enum type {Carnivorous, Vegetarian}
	/**
	 * razza della specie
	 */
	private type speciesType;
	/**
	 * numero di dinosauri creati
	 */
	private int  dinoNumber;
	/**
	 * mappa della specie con celle visibili solo quelle gia' visitate
	 */
	private Object[][] map;
	/**
	 * punteggio della specie
	 */
	private int score;
	/**
	 * username del giocatore possessore della specie
	 */
	private String playerUsername;
	/**
	 * limite massimo di dinosauri in gioco
	 */
	private final int maxDino=1;
	
	/**
	 * Crea una nuova specie, setta il nome e il timo, imposta timeOfLive a 120, crea un nuovo dinosauro e chiama startMap e l'updateMap
	 * @param name
	 * @param speciesType
	 * @param username
	 */
	public Species(String name, type speciesType, String username)
	{
		int posRig, posCol;
		
		myDinosaurs = new Hashtable<String, Dinosaur>();
		
		this.name = name;
		this.speciesType = speciesType;
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
	}
	
	/**
	 * ritorna il punteggio
	 * @return int
	 */
	public int getScore() {
		return score;
	}

	/**
	 * setta il punteggio
	 * @param score
	 */
	public void setScore(int score) 
	{
		this.score = score;
	}
	
	/**
	 * sottrae uno al tempo di vita della specie
	 */
	public void updateTimeOfLive()
	{
		timeOfLive -= 1;
	}
	
	/**
	 * ritorna il tempo di vita della specie
	 * @return int
	 */
	public int getTimeOfLive()
	{
		return timeOfLive;
	}
	
	/**
	 * incrementa il punteggio
	 */
	public void increaseScore()
	{
		Iterator<Map.Entry<String, Dinosaur>> iter = this.getDinosaurs();
		
		while(iter.hasNext())
		{
			Map.Entry<String, Dinosaur> me =(Map.Entry<String, Dinosaur>)iter.next();
			score = score + 1 + ((Dinosaur)me.getValue()).getDinoDimension();
		}
	}
	
	/**
	 * restituisce l'username del giocatore
	 * @return String
	 */
	public String getPlayerUsername() 
	{
		return playerUsername;
	}

	/**
	 * setta l'username del giocatore
	 * @param playerUsername
	 */
	public void setPlayerUsername(String playerUsername) 
	{
		this.playerUsername = playerUsername;
	}

	/**
	 * aggiunge uno al numero di dinosauri della specie, per l'identificatore
	 */
	public void addDinoNumber()
	{
		dinoNumber += 1;
		return;
	}
	
	/**
	 * ritorna il numero di dinosauri creati
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
	 * crea un nuovo dinosauro, lo aggiunge alla HashTable  e lo setta sulla mappa generale
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
	public Iterator<Map.Entry<String, Dinosaur>> getDinosaurs()
	{
		if(myDinosaurs.size() > 0)
		{
			Set<Map.Entry<String, Dinosaur>> set = myDinosaurs.entrySet();
			
			return set.iterator();
		}
		else
			return null;
	}
	
	/**
	 * controlla se ci sono dinosauri della specie
	 * ritorna true se positivo, false altrimenti
	 * @return boolean
	 */
	public boolean existDinos()
	{
		if(myDinosaurs.size() > 0)
		{
			return true;
		}
		else
			return false;
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
		Set<Map.Entry<String, Dinosaur>> set = myDinosaurs.entrySet();
		Iterator<Map.Entry<String, Dinosaur>> iter = set.iterator();
		
		while(iter.hasNext())
		{
			Map.Entry<String, Dinosaur> me = (Map.Entry<String, Dinosaur>) iter.next();
			((Dinosaur)me.getValue()).updateDinosaurAge();
		}
		
	}

	/**
	 * ritorna il type carivoro
	 * @return type
	 */
	public static type getCarnType()
	{
		return type.Carnivorous;
	}
	
	/**
	 * ritorna il type vegetariano
	 * @return type
	 */
	public static type getVegType()
	{
		return type.Vegetarian;
	}
	
	/**
	 * ritorna il type della specie
	 * @return type
	 */
	public type getType()
	{
		return speciesType;
	}

	
	/**
	 * ritorna il nome della specie
	 * @return String
	 */
	public String getName() 
	{
		return name;
	}

	/**
	 * setta il nome della specie
	 * @param name
	 */
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
	 * aggiorna la mappa generale del giocatore (buio) inserendo la vista dei dinosauri in gioco
	 */
	public void updateMap()
	{
		Set<Map.Entry<String, Dinosaur>> set = myDinosaurs.entrySet();
		Iterator<Map.Entry<String, Dinosaur>> iter = set.iterator();
		Dinosaur dino;
		while(iter.hasNext())
		{
			Map.Entry<String, Dinosaur> me = (Map.Entry<String, Dinosaur>) iter.next();
			dino = me.getValue();
			Object[][] localMap = dino.getLocalMap();
			int sizeRow = dino.getSizeRowLocalMap();
			int sizeCol = dino.getSizeColLocalMap();
			int startRow = dino.getPosRow() - sizeRow/2;
			int startCol = dino.getPosCol() - sizeCol/2;

			if(startRow < 0)
				startRow = 0;
			int endRow = startRow + sizeRow-1;
			if(startCol < 0)
				startCol = 0;
			int endCol = startCol + sizeCol-1;
			if(endRow >= Game.maxRow)
			{
				startRow += Game.maxRow - endRow - 1;
				endRow = Game.maxRow - 1;
			}
			if(endCol >=Game.maxCol)
			{
				startCol += Game.maxCol - endCol - 1;
				endCol = Game.maxCol - 1;
			}
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
	 * ritorna la mappa generale della specie con celle visibili solo quelle giˆ visitate.
	 * @return Object[][]
	 */
	public Object[][] getPlayerMap()
	{
		return map;
	}
	
	/**
	 * stampa la mappa della specie
	 */
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
	
	/**
	 * setta la cella del dinosauro dove si trovava con quello che c'era prima di esso, ed elimina il dinosauro
	 * @param dinoId
	 * @param fight : boolean che indica se la morte  avvenuta dopo la sconfitta in combattimento
	 */
	public void killDino(Dinosaur dinoId, boolean fight)
	{
		if(!fight)
		{
				if(speciesType==type.Carnivorous)
				{
					if(((Carnivorous)dinoId).getVegetation()!=null)
					{
						Game.setCellMap(((Carnivorous)dinoId).getVegetation(), dinoId.getPosRow(), dinoId.getPosCol());
						setCellMap(((Carnivorous)dinoId).getVegetation().toString(), dinoId.getPosRow(), dinoId.getPosCol());
					}
					else
					{
						Game.setCellMap("t",dinoId.getPosRow(), dinoId.getPosCol());
						setCellMap("t", dinoId.getPosRow(), dinoId.getPosCol());
					}
				}
				else
				{
					if(((Vegetarian)dinoId).getCarrion()!=null)
					{
						Game.setCellMap(((Vegetarian)dinoId).getCarrion(), dinoId.getPosRow(), dinoId.getPosCol());
						setCellMap(((Vegetarian)dinoId).getCarrion().toString(), dinoId.getPosRow(), dinoId.getPosCol());
					}
					else if(((Vegetarian)dinoId).getVegetation()!=null)
					{
						Game.setCellMap(((Vegetarian)dinoId).getVegetation(), dinoId.getPosRow(), dinoId.getPosCol());
						setCellMap(((Vegetarian)dinoId).getVegetation().toString(), dinoId.getPosRow(), dinoId.getPosCol());
					}
					else
					{
						Game.setCellMap("t",dinoId.getPosRow(), dinoId.getPosCol());
						setCellMap("t", dinoId.getPosRow(), dinoId.getPosCol());
					}
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
	
	/**
	 * controlla se il numero massimo di dinosauri e' stato raggiunto
	 * @return boolean
	 */
	public boolean checkDinoNumber()
	{
		if(dinoNumber<maxDino)
			return true;
		else
			return false;
	}
}
