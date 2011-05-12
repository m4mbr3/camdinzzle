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
	private static int eEgg = 1500;
	protected int posRow; //Row of dinosaur on the map
	protected int posCol; //Column of dinosaur on the map
	
	public abstract boolean eat(Object[][] map);
	public abstract void fight();
	public abstract boolean move();
	
	/**
	 * Create a new dinosaur with age = (30±20%), dimension = 1 and energy = 1000
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
}


