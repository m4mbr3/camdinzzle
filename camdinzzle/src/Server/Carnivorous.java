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
	public boolean fight(Object dino) 
	{
		int powerAttack;
		int powerDefense;
		
		powerAttack = 2 * energy * this.getDinoDimension();		//calcola la forza del dinosauro che attacca
		
		if(dino instanceof Carnivorous)						//calcola la forza del dinosauro in difesa
		{
			powerDefense = 2 * ((Carnivorous) dino).energy * ((Dinosaur) dino).getDinoDimension();
		}
		else
		{
			powerDefense = ((Vegetarian) dino).energy * ((Dinosaur) dino).getDinoDimension();
		}
		
		if(powerAttack >= powerDefense)
		{
			energy += ((Dinosaur) dino).getEnergy() * 0.75;
			if(energy > energyMax)
			{
				energy = energyMax;
			}
			return true;
		}
		else
		{
			return false;
		}
		
	}

	@Override
	public boolean move() 
	{

		return false;
	}

}
