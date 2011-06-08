package Server;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

/**
 * 
 * @author Marco
 * 
 */

public abstract class Dinosaur implements Serializable
{
	private String dinoId;
	private int age;
	protected Species nameSpecie;
	protected int energy;
	private int dimension;
	protected int energyMax;
	private final int eEgg = 1500;
	protected int posRow; //Row of dinosaur on the map
	protected int posCol; //Column of dinosaur on the map
	private Object[][] localMap;
	private int distMax;
	private boolean moveTake=false;
	private boolean actionTake=false;
	private int timeOfLive;
	
	public abstract boolean eat(Object cell);
	public abstract boolean fight(Object dino);
	public abstract boolean move(int rowDest, int colDest);
	
	/**
	 * Create a new dinosaur with age = (30±20%), dimension = 1 and energy = 1000
	 * @param dinoId id del dinosauro da creare
	 * @param posRig riga nella mappa
	 * @param posCol colonna nella mappa
	 * @return null
	 *  
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
	 * controlla che abbia abbanstanza energia per crescere e cresce
	 * altrimenti muore
	 * 
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
						}
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
	 * dimensione dinosauro
	 * @return int
	 */
	public int getDinoDimension()
	{
		return dimension;
	}
	
	/**
	 * riga della posizione del dinosauro
	 * @return int
	 */
	public int getPosRow()
	{
		return posRow;
	}

	/**
	 * riga della posizione del dinosauro
	 * @return int
	 */
	public int getPosCol()
	{
		return posCol;
	}
	
	/**
	 * energia dinosauro
	 * @return int
	 */
	public int getEnergy()
	{
		return energy;
	}
	
	/**
	 * turni di vita mancanti al dinosauro
	 * @return int
	 */
	public int getAge()
	{
		return age;
	}
	
	/**
	 * turni vissuti dal dinosauro
	 */
	public int getTimeOfLive()
	{
		return timeOfLive;
	}
	
	/**
	 * restituisce la dimensione della mappa locale in base alla dimensione del dinosauro
	 * @return
	 */
	public int getSizeRowLocalMap()
	{		
		return localMap.length;
	}
	public int getSizeColLocalMap()
	{
		return localMap[0].length;
	}
	
	/**
	 * inizializza la mappa locale del dinosauro
	 */
	public void setLocalMap()
	{
		localMap = Game.getLocalMap(posRow, posCol, dimension);		
	}
	 
	public Object[][] getLocalMap()
	{
		setLocalMap();
		 return localMap;
	}
	
	public String toString()
	{
		return "d";
	}
	public void setDistMax(int distMax)
	{
		this.distMax = distMax;
	}
	
	public int getDistMax()
	{
		return distMax;
	}
	public void setMoveTake(boolean moveTake) 
	{
		this.moveTake = moveTake;
	}
	public boolean getMoveTake() 
	{
		return moveTake;
	}
	public void setActionTake(boolean actionTake) 
	{
		this.actionTake = actionTake;
	}
	public boolean getActionTake() 
	{
		return actionTake;
	}
	public Species getSpecie()
	{
		return nameSpecie;
	}
	
}


