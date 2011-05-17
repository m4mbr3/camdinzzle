package Server;

/**
 * 
 * @author Marco
 *
 */

public class Carnivorous extends Dinosaur
{
	private static final int distMax = 3;

	public Carnivorous(String dinoId, int posRow, int posCol) 
	{
		super(dinoId, posRow, posCol);
	}
	
	@Override
	/**
	 * @param map
	 */
	public boolean eat(Object cell)
	{
		if(cell instanceof Carrion)
		{
			energy += ((Carrion)cell).getPower();
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
	public boolean move(int rowDest, int colDest)
	{
		int posRow = this.posRow;
		int posCol = this.posCol;
		
		if(Game.checkReachCell(posRow, posCol, rowDest, colDest, distMax))	//se è raggiungiblie allora posso spostarmi
		{
			
			if((Game.getCell(rowDest, colDest) instanceof Carnivorous)||(Game.getCell(rowDest, colDest) instanceof Vegetarian))		//se c'è un carnivoro o vegetariano combatto
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
			if(Game.getCell(rowDest, colDest) instanceof Carrion)
			{
				eat(Game.getCell(rowDest, colDest));
				this.posRow=rowDest;
				this.posCol=colDest;
			}
			
		}
		return false;
	}

}
