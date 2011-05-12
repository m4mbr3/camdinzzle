package Server;

/**
 * 
 * @author Marco
 *
 */

public class Vegetarian extends Dinosaur
{

	public Vegetarian(String dinoId, int posRow, int posCol)
	{
		super(dinoId, posRow, posCol);
	}
	
	@Override
	public boolean eat(Object [][] map)
	{
		if(map[posRow][posCol] instanceof Vegetation)
		{
			energy += ((Vegetation)map[posRow][posCol]).getPower();
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
