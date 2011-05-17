package Server;

/**
 * 
 * @author Marco
 * 
 */

public abstract class Dinosaur 
{
	private String dinoId;
	private int age;
	protected int energy;
	private int dimension;
	protected int energyMax;
	private final int eEgg = 1500;
	protected int posRow; //Row of dinosaur on the map
	protected int posCol; //Column of dinosaur on the map
	private Object[][] localMap;
	
	public abstract boolean eat(Object[][] map);
	public abstract boolean fight(Object dino);
	public abstract boolean move();
	
	/**
	 * Create a new dinosaur with age = (30�20%), dimension = 1 and energy = 1000
	 * @param dinoId id del dinosauro da creare
	 * @param posRig riga nella mappa
	 * @param posCol colonna nella mappa
	 * @return null
	 *  
	 */
	public Dinosaur(String dinoId, int posRow, int posCol)
	{
		age = (int) (Math.random() * 13 + 24);
		dimension = 1;
		energy = 1000;
		energyMax = energy * dimension;
		this.posRow = posRow;
		this.posCol = posCol;
		startLocalMap();
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
				energy = energy - energyMax;
				dimension = dimension + 1;
				return true;
			}
			else 
			{
				dead();
				return false;
			}
		}
	}
	
	/**
	 * incompleto 
	 * dove mette l'uovo??
	 */
	public boolean newEgg()
	{
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
							if((Game.getCell(i, j) instanceof String)&&(Game.getCell(i, j)=="t"))
							{
								
								positioned = true;
								break;
							}
						}
					}
					if(positioned) break;
				}
				offSet++;
			}while(!positioned);
			
			return true;
		}
		else 
		{
			return false;	//muore
		}
	}
	
	/**
	 * incompleto
	 */
	public void dead()
	{
		
	}
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
	 * aggiorna stato dino
	 */
	public void updateDinosaurAge()
	{
		age -= 1;
	}
	
	public int getDinoDimension()
	{
		return dimension;
	}
	
	public int getPosRow()
	{
		return posRow;
	}
	
	public int getPosCol()
	{
		return posCol;
	}
	
	public int getEnergy()
	{
		return energy;
	}
	
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
	
	private void startLocalMap()
	{
		int size;
		if(dimension==1)
		{
			size = 5;
		}
		else
		{
			if((dimension==2)||(dimension==3))
			{
				size = 7;
			}
			else
			{
				size = 9;
			}
		}
		localMap = new Object[size][size];
		localMap = Game.getLocalMap(posRow, posCol, dimension);
	}
	
	private void udateLocalMap()
	{
		localMap = Game.getLocalMap(posRow, posCol, dimension);
	}
	 
	public Object[][] getLocalMap()
	{
		 return localMap;
	}		
}


