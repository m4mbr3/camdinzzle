/*
 * 
 */
public abstract class Dinosaur 
{
	private int age;
	private int energy;
	private int dimension;
	private int energyMax;
	private static int eEgg = 1500;
	
	public abstract int eat();
	public abstract void fight();
	public abstract boolean move();
	
	/*
	 * Creats a new dinosaur with age = (30±20%), dimension = 1 and energy = 1000 
	 */
	public Dinosaur()
	{
		age = (int) (Math.random() * 13 + 24);
		dimension = 1;
		energy = 1000;
		energyMax = energy * dimension;
	}
	
	/*
	 * controlla che la dimensione non sia già massima (=5)
	 * controlla che abbia abbanstanza energia per crescere e cresce
	 * altrimenti muore
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
	
	/*
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
	
	public void dead()
	{
		
	}
}
