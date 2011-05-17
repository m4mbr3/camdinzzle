package Server;

/**
 * 
 * @author Marco
 *
 */

public class Vegetarian extends Dinosaur
{
	private final int distMax = 2;

	public Vegetarian(String dinoId, int posRow, int posCol)
	{
		super(dinoId, posRow, posCol);
	}
	
	@Override
	public boolean eat(Object cell)
	{
		if(cell instanceof Vegetation)
		{
			energy += ((Vegetation)cell).getPower();
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
	public boolean move(int rowDest, int colDest)
	{
		int posRow = this.posRow;
		int posCol = this.posCol;
		if(Game.getCell(rowDest, colDest) instanceof Vegetarian)	//se c'è un altro erbivoro non posso spostarmi
		{
			return false;
		}
		if(Game.checkReachCell(posRow, posCol, rowDest, colDest, distMax))	//se è raggiungiblie allora posso spostarmi
		{
			
			if(Game.getCell(rowDest, colDest) instanceof Carnivorous)		//se c'è un carnivoro combatto
			{
				if(fight(Game.getCell(rowDest, colDest)))
				{
					this.posRow=rowDest;
					this.posCol=colDest;
				}
				else
				{
					//muore dinosauro
					return false;
				}
			}
			if(Game.getCell(rowDest, colDest) instanceof Vegetation)
			{
				eat(Game.getCell(rowDest, colDest));
				this.posRow=rowDest;
				this.posCol=colDest;
			}
			
		}
		return false;
	}

}
