package Server;

/**
 * 
 * @author Marco
 *
 */

public class Carnivorous extends Dinosaur
{

	public Carnivorous(String dinoId, int posRow, int posCol) 
	{
		super(dinoId, posRow, posCol);
	}
	
	@Override
	/**
	 * @param map
	 */
	public boolean eat(Object [][] map)
	{
		if(map[posRow][posCol] instanceof Carrion)
		{
			energy += ((Carrion)map[posRow][posCol]).getPower();
			if(energy > energyMax)
			{
				energy = energyMax;
			}
			return true;
		}
		return false;
	}

	@Override
	public void fight() 
	{

		
	}

	@Override
	public boolean move() 
	{

		return false;
	}

}
