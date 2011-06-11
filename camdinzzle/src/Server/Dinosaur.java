package Server;

import java.io.Serializable;

/**
 * 
 * @author Marco
 * 
 */

public abstract class Dinosaur implements Serializable
{
	private static final long serialVersionUID = 1L;
	/**
	 * identificatore del dinosauro
	 */
	private String dinoId;
	/**
	 * eta' massima del dinosauro
	 */
	private int age;
	/**
	 * specie del dinosauro
	 */
	protected Species nameSpecie;
	/**
	 * energia del dinosauro
	 */
	protected int energy;
	/**
	 * dimensione del dinosauro
	 */
	private int dimension;
	/**
	 * energia massima che il dinosauro puo' accumulare
	 */
	protected int energyMax;
	/**
	 * energia per creare un nuovo dinosauro
	 */
	private final int eEgg = 1500;
	/**
	 * riga della posizione del dinosauro
	 */
	protected int posRow;
	/**
	 * colonna della posizione del dinosauro
	 */
	protected int posCol;
	/**
	 * vista locale del dinosauro
	 */
	private Object[][] localMap;
	/**
	 * distanza massima che puo' percorrere il dinosauro
	 */
	private int distMax;
	/**
	 * booleano per controllare se il movimento nel turno  gia' stato effettuato
	 */
	private boolean moveTake=false;
	/**
	 * booleano per controllare se un azione nel turno  gia' stata effettuata
	 */
	private boolean actionTake=false;
	/**
	 * numero di turni gia' vissuti dal dinosauro
	 */
	private int timeOfLive;
	
	/**
	 * controlla cosa c' nella cella e aggiunge l'energia al dinosauro senza superare il massimo
	 * @param cell di destinazione
	 */
	public abstract void eat(Object cell);
	/**
	 * calcola l'energia dei due dinosauri
	 * @param dino avversario
	 * @return true se vince, false altrimenti
	 */
	public abstract boolean fight(Object dino);
	/**
	 * risetta la cella di partenza con la carogna o vegetazione se c'erano, calcola l'energia x spostarsi e in caso positivo setta
	 * la posizione del dinosauro, aggiorna la mappa locale, aggiorna la mappa della specie e setta il moveTake a true
	 * @param rowDest
	 * @param colDest
	 * @return true se ha abbastanza energia per muoversi, false altrimenti.
	 */
	public abstract boolean move(int rowDest, int colDest);
	
	/**
	 * crea un nuovo dinosauro con eta' = (30±20%), dimensione = 1 ed energia = 1000
	 * @param dinoId id del dinosauro da creare
	 * @param posRow riga nella mappa
	 * @param posCol colonna nella mappa 
	 */
	public Dinosaur(String dinoId, int posRow, int posCol, Species nameSpecie)
	{
		timeOfLive=0;
		age = (int) (Math.random() * 13 + 24);
		dimension = 1;
		energy = 1000;
		this.dinoId = dinoId;
		energyMax = 1000 * dimension;
		this.posRow = posRow;
		this.posCol = posCol;
		distMax=0;
		this.nameSpecie = nameSpecie;
		actionTake=false;
		moveTake=false;
		setLocalMap();
	}
	
	/**
	 * controlla che la dimensione non sia gia' massima (=5)
	 * controlla che abbia abbanstanza energia per crescere, cresce , toglie l'energia necessaria, aumenta l'energia massima
	 * e setta actionTake a true
	 * altrimenti muore
	 * @return true se cresce, false altrimenti
	 */
	public boolean growUp()
	{
		if(dimension == 5) return false;
		else
		{
			if(energy > (energyMax/2))
			{
				energy = energy - energyMax/2;
				dimension = dimension + 1;
				energyMax = dimension * 1000;
				setActionTake(true);
				return true;
			}
			else 
			{
				return false;
			}
		}
	}
	
	/**
	 * crea un nuovo dinosauro nella prima cella libera vicino al dinosauro
	 * @return dinoId del nuovo dinosauro
	 */
	public String newEgg()
	{
		String idDino=null;
		Dinosaur newDino;
		if(energy > eEgg)
		{
			energy = energy - eEgg;
			int offSet = 1;
			boolean positioned = false;
			do
			{
				for(int i=posRow-offSet; i<posRow+offSet+1; i++ )
				{
					for(int j=posCol-offSet; i<posCol+offSet+1; j++)
					{
						if((i>=0)&&(i<Game.maxRow)&&(j>=0)&&(j<Game.maxCol))
						{					
							if((Game.getCell(i, j) instanceof String)&&(((String)Game.getCell(i, j)).compareTo("t")==0))
							{
								newDino=nameSpecie.addDinosaur(i, j);
								idDino=newDino.getDinoId();
								positioned = true;
								newDino.setActionTake(true);
								newDino.setMoveTake(true);
								break;
							}
							if(positioned) break;
						}
						if(positioned) break;
					}
					if(positioned) break;
				}
				offSet++;
				
			}while(!positioned);
			setActionTake(true);
			return idDino;
		}
		else 
		{
			return idDino;
		}
	}
	
	/**
	 * 
	 * @return tipo della specie: carnivoro o vegetariano
	 */
	public Species.type getType()
	{
		return nameSpecie.getType();
	}
	/**
	 * 
	 * @return dinoId
	 */
	public String getDinoId()
	{
		return dinoId;
	}
	
	/**
	 * aggiorna stato dinosauro
	 * sottrae un anno di vita a quelli rimanenti al dinosauro
	 * aggiunge uno ai turni vissuti dal dinosauro
	 * risetta a false moveTake e ActionTake
	 */
	public void updateDinosaurAge()
	{
		timeOfLive += 1;
		age -= 1;
		setMoveTake(false);
		setActionTake(false);
	}
	
	/**
	 * ritorna la dimensione dinosauro
	 * @return int
	 */
	public int getDinoDimension()
	{
		return dimension;
	}
	
	/**
	 * ritorna la riga della posizione del dinosauro
	 * @return int
	 */
	public int getPosRow()
	{
		return posRow;
	}

	/**
	 * ritorna la colonna della posizione del dinosauro
	 * @return int
	 */
	public int getPosCol()
	{
		return posCol;
	}
	
	/**
	 * ritorna l'energia dinosauro
	 * @return int
	 */
	public int getEnergy()
	{
		return energy;
	}
	
	/**
	 * ritorna turni di vita mancanti al dinosauro
	 * @return int
	 */
	public int getAge()
	{
		return age;
	}
	
	/**
	 * ritorna turni vissuti dal dinosauro
	 * @return int
	 */
	public int getTimeOfLive()
	{
		return timeOfLive;
	}
	
	/**
	 * ritorna il numero di righe della mappa locale
	 * @return  int
	 */
	public int getSizeRowLocalMap()
	{		
		return localMap.length;
	}
	
	/**
	 * ritorna il numero di colonne della mappa locale
	 * @return int
	 */
	public int getSizeColLocalMap()
	{
		return localMap[0].length;
	}
	
	/**
	 * setta la mappa locale del dinosauro prendendola dalla mappa generale
	 */
	public void setLocalMap()
	{
		localMap = Game.getLocalMap(posRow, posCol, dimension);		
	}
	
	/**
	 * ritorna la mappa locale del dinosauro
	 * @return  Object[][]
	 */
	public Object[][] getLocalMap()
	{
		setLocalMap();
		 return localMap;
	}
	
	/**
	 * override di toString
	 * ritorna una "d"
	 * @return String
	 */
	public String toString()
	{
		return "d";
	}
	
	/**
	 * setta la distanza massima percorribile dal dinosauro
	 * @param distMax
	 */
	public void setDistMax(int distMax)
	{
		this.distMax = distMax;
	}
	
	/**
	 * ritorna la distanza massima percorribile dal dinosauro
	 * @return int
	 */
	public int getDistMax()
	{
		return distMax;
	}
	
	/**
	 * setta la variabile moveTake
	 * @param moveTake
	 */
	public void setMoveTake(boolean moveTake) 
	{
		this.moveTake = moveTake;
	}
	
	/**
	 * ritorna la variabile moveTake
	 * @return boolean
	 */
	public boolean getMoveTake() 
	{
		return moveTake;
	}
	
	/**
	 * setta la variabile actionTake
	 * @param actionTake
	 */
	public void setActionTake(boolean actionTake) 
	{
		this.actionTake = actionTake;
	}
	
	/**
	 * ritorna la variabile actionTake
	 * @return boolean
	 */
	public boolean getActionTake() 
	{
		return actionTake;
	}
	
	/**
	 * ritorna l'oggetto specie
	 * @return Species
	 */
	public Species getSpecie()
	{
		return nameSpecie;
	}
	
}


