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
	private int energy;
	private int dimension;
	private int energyMax;
	private static int eEgg = 1500;
	private int posRig; //Row of dinosaur on the map
	private int posCol; //Column of dinosaur on the map
	
	public abstract int eat();
	public abstract void fight();
	public abstract boolean move();
	
	/**
	 * Create a new dinosaur with age = (30�20%), dimension = 1 and energy = 1000
	 * @param dinoId id del dinosauro da creare
	 * @param posRig riga nella mappa
	 * @param posCol colonna nella mappa
	 * @return null
	 *  
	 */
	public Dinosaur(String dinoId, int posRig, int posCol)
	{
		age = (int) (Math.random() * 13 + 24);
		dimension = 1;
		energy = 1000;
		energyMax = energy * dimension;
		this.posRig = posRig;
		this.posCol = posCol;
	}
	
	/**
	 * controlla che la dimensione non sia gi� massima (=5)
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
			
			return true;
		}
		else 
		{
			dead();
			return false;
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


