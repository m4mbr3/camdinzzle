package Server;

import java.util.Iterator;
import java.util.Map;

/**
 * 
 * @author Marco
 * 
 */

public abstract class Dinosaur 
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
	private int turniVissuti;
	private int distMax;
	private boolean moveTake=false;
	private boolean actionTake=false;
	
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
		age = (int) (Math.random() * 13 + 24);
		dimension = 1;
		energy = 1000;
		this.dinoId = dinoId;
		energyMax = energy * dimension;
		this.posRow = posRow;
		this.posCol = posCol;
		turniVissuti = 0;
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
				for(int i=posRow-offSet; i<=posRow+offSet; i++ )
				{
					for(int j=posCol-offSet; i<=posCol+offSet; j++)
					{
						if((i>=0)&&(i<Game.maxRow)&&(j>=0)&&(j<Game.maxCol))
						{					
							if((Game.getCell(i, j) instanceof String)&&(((String)Game.getCell(i, j)).compareTo("t")==0))
							{
								newDino=nameSpecie.addDinosaur(i, j);
								idDino=newDino.toString();
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
		return Species.getType();
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
		age -= 1;
		turniVissuti += 1;
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
	 * turni vissuti dal dinosauro
	 * @return int
	 */
	public int getTurniVissuti()
	{
		return turniVissuti;
	}
	
	/**
	 * setta i turni vissuti dal dinosauro
	 * @param t
	 */
	public void setTurniVissuti(int t)
	{
		turniVissuti = t;
	}
	
	/**
	 * restituisce la dimensione della mappa locale in base alla dimensione del dinosauro
	 * @return
	 */
	public int getSizeLocalMap()
	{
		if(dimension==1)
		{
			return 5;
		}
		else
		{
			if((dimension==2)||(dimension==3))
			{
				return 7;
			}
			else
			{
				return 9;
			}
		}
	}
	
	/**
	 * inizializza la mappa locale del dinosauro
	 */
	public void setLocalMap()
	{
		int size = getSizeLocalMap();

		localMap = new Object[size][size];
		localMap = Game.getLocalMap(posRow, posCol, dimension);
		
	}
	 
	public Object[][] getLocalMap()
	{
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


