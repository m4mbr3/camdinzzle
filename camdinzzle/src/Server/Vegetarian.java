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
	public boolean fight(Object dino) 
	{
		int powerAttack;
		int powerDefense;
		
		powerAttack = energy * this.getDinoDimension();		//calcola la forza del dinosauro che attacca
		
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
