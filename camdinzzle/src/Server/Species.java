package Server;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Classe che rappresenta la specie di un giocatore. Contiene le informazione di tutti i dinosauri
 * della specie.
 */
public class Species implements Serializable
{
	private static final long serialVersionUID = 1L;
	/*
	 * Nome della specie.
	 */
	private String name;
	/*
	 * Turni mancanti alla estizione.
	 */
	private int timeOfLive;
	/*
	 * Tabella dei dinosauri in gioco.
	 */
	private Hashtable<String, Dinosaur> myDinosaurs;
	/*
	 * Razza di dinosauri: Carnivoro o Vegetariano.
	 */
	protected static enum type {Carnivorous, Vegetarian}
	/*
	 * Razza della specie.
	 */
	private type speciesType;
	/*
	 * Numero di dinosauri creati.
	 */
	private int  dinoNumber;
	/*
	 * Mappa della specie con celle visibili solo quelle gia' visitate.
	 */
	private Object[][] map;
	/*
	 * Punteggio della specie.
	 */
	private int score;
	/*
	 * Username del giocatore possessore della specie.
	 */
	private String playerUsername;
	/*
	 * Limite massimo di dinosauri in gioco.
	 */
	private final int maxDino=1;
	
	/**
	 * Crea una nuova specie, setta il nome e il tipo, imposta timeOfLive a 120, crea un nuovo dinosauro e chiama startMap e l'updateMap
	 * @param name nome della specie
	 * @param speciesType tipo della specie
	 * @param username l'username del giocatore che vuole creare la specie
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
	 * Ritorna il punteggio della specie.
	 * @return punteggio della specie
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Assegna il punteggio alla specie.
	 * @param punteggio
	 */
	public void setScore(int score) 
	{
		this.score = score;
	}
	
	/**
	 * Sottrae uno al tempo di vita della specie.
	 */
	public void updateTimeOfLive()
	{
		timeOfLive -= 1;
	}
	
	/**
	 * Ritorna il tempo di vita della specie.
	 * @return tempo di vita della specie
	 */
	public int getTimeOfLive()
	{
		return timeOfLive;
	}
	
	/**
	 * Incrementa il punteggio della specie.
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
	 * Restituisce l'username del giocatore a cui appartiene la specie.
	 * @return username del giocatore
	 */
	public String getPlayerUsername() 
	{
		return playerUsername;
	}

	/**
	 * Setta l'username del giocatore.
	 * @param username del giocatore
	 */
	public void setPlayerUsername(String playerUsername) 
	{
		this.playerUsername = playerUsername;
	}

	/**
	 * Sggiunge uno al numero di dinosauri della specie. Utilizzato per l'id progressivo dei 
	 * dinosauri della specie.
	 */
	public void addDinoNumber()
	{
		dinoNumber += 1;
		return;
	}
	
	/**
	 * Ritorna il numero di dinosauri creati.
	 */
	public int getDinoNumber()
	{
		return dinoNumber;
	}
	
	/**
	 * Ritorna il riferimento al dinosauro richiesto.
	 * @param dinoId ID del dinosauro
	 * @return riferimento al dinosauro se esiste un dinosauro avente come id dinoId, null altrimenti
	 */
	public Dinosaur getDino(String dinoId)
	{
		return myDinosaurs.get(dinoId);
	}
	
	/**
	 * Crea un nuovo dinosauro, lo aggiunge alla lista dei dinosauri della specie e lo inserisce 
	 * nella mappa generale.
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
	 * Ritorna la lista dei dinosauri della specie.
	 * @return iteratore con la lista dei dinosauri
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
	 * Controlla se ci sono dinosauri della specie.
	 * @return true se esistono dinosauri della specie, false altrimenti
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
	 * Riconosce l'id del dinosauro e restituisce il riferimento.
	 * @param dinoId id del dinosauro
	 * @return riferimento al dinosauro
	 */
	public Dinosaur identifyDinosaur(String dinoId)
	{
		if(myDinosaurs.containsKey(dinoId))
			return myDinosaurs.get(dinoId);
		return null;
	}
	
	/**
	 * Aggiorna stato di ogni dinosauro della specie.
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
	 * Ritorna il type carnivoro.
	 * @return tipo carnivoro 
	 */
	public static type getCarnType()
	{
		return type.Carnivorous;
	}
	
	/**
	 * Ritorna il type vegetariano
	 * @return tipo vegetariano
	 */
	public static type getVegType()
	{
		return type.Vegetarian;
	}
	
	/**
	 * Ritorna il type della specie.
	 * @return tipo della specie
	 */
	public type getType()
	{
		return speciesType;
	}

	
	/**
	 * Ritorna il nome della specie.
	 * @return nome della specie
	 */
	public String getName() 
	{
		return name;
	}

	/**
	 * Setta il nome della specie.
	 * @param name nome della specie
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * Crea la mappa generale del giocatore tutta a buio.
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
	 * Aggiorna la mappa generale del giocatore (buio) inserendo la vista dei dinosauri in gioco.
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
	 * Ritorna la mappa generale della specie con celle visibili solo quelle gia' visitate.
	 * @return mappa generale
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
	
	/**
	 * Setta la cella del dinosauro dove si trovava con quello che c'era prima di esso, 
	 * ed elimina il dinosauro.
	 * @param dinoId id del dinosauro
	 * @param fight indica se la morte e' avvenuta dopo la sconfitta in combattimento
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
	 * Imposta una cella della mappa generale del giocatore con la stringa in msg e nella posizione row, col
	 * @param msg stringa da settare
	 * @param row riga della mappa da settare
	 * @param col colonna della mappa da settare
	 */
	public void setCellMap(String msg, int row, int col)
	{
		map[row][col] = msg;
	}
	
	/**
	 * Controlla se il numero massimo di dinosauri e' stato raggiunto.
	 * @return true se il numero massimo di dinosauri e' stato raggiunto, false altrimenti
	 */
	public boolean checkDinoNumber()
	{
		if(dinoNumber<maxDino)
			return true;
		else
			return false;
	}
}
