package Server;

/**
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
	private int posX; //coordinate X of dinosaur on the map
	private int posY; //coordinate Y of dinosaur on the map
	
	public abstract int eat();
	public abstract void fight();
	public abstract boolean move();
	
	/**
	 * Create a new dinosaur with age = (30±20%), dimension = 1 and energy = 1000 
	 */
	public Dinosaur(String dinoId, int posX, int posY)
	{
		age = (int) (Math.random() * 13 + 24);
		dimension = 1;
		energy = 1000;
		energyMax = energy * dimension;
		this.posX = posX;
		this.posY = posY;
	}
	
	/**
	 * controlla che la dimensione non sia già massima (=5)
	 * controlla che abbia abbanstanza energia per crescere e cresce
	 * altrimenti muore
	 * 
	 * dove mette l'uovo??
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
