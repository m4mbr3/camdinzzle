package Server;

/**
 * 
 * @author Marco
 *
 */

public class Vegetarian extends Dinosaur
{
	private Carrion carrion=null;

	public Vegetarian(String dinoId, int posRow, int posCol)
	{
		super(dinoId, posRow, posCol);
		setDistMax(2);
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
			this.setActionTake(true);
			return true;
		}
		return false;
	}

	@Override
	/**
	 * @param 
	 */
	public boolean fight(Object dino) 
	{
		int powerAttack;
		int powerDefense;
		
		powerAttack = energy * this.getDinoDimension();		//calcola la forza del dinosauro che attacca
		
		powerDefense = 2 * ((Carnivorous) dino).energy * ((Dinosaur) dino).getDinoDimension();		//calcola la forza del dinosauro in difesa
		

		if(powerAttack >= powerDefense)
		{
			if(((Carnivorous) dino).getVegetation()!=null)
			{
				Game.setCellMap(((Carnivorous) dino).getVegetation(), ((Dinosaur)dino).getPosRow(), ((Dinosaur)dino).getPosCol());
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

		if(carrion!=null)
		{
			Game.setCellMap(carrion, this.getPosRow(), this.getPosCol());
			carrion=null;
		}
		else
		{
			Game.setCellMap("t", this.getPosRow(), this.getPosCol());
		}
		energy -= 10 * Math.pow(2, Game.getDistCell(this.getPosRow(), this.getPosCol(), rowDest, colDest));
		if(energy<=0)
		{
			return false;
		}

		this.posRow=rowDest;
		this.posCol=colDest;
		this.setMoveTake(true);
		return true;
/*		else
		{
			
			if(Game.getCell(rowDest, colDest) instanceof Carnivorous)		//se c'� un carnivoro  combatto
			{
				if(!fight(Game.getCell(rowDest, colDest)))
				{
					return false;
				}

			}
			if(Game.getCell(rowDest, colDest) instanceof Carrion)
			{
				carrion = (Carrion)Game.getCell(rowDest, colDest);
			}
			if(Game.getCell(rowDest, colDest) instanceof Vegetation)
			{
				eat(Game.getCell(rowDest, colDest));
			}
			this.posRow=rowDest;
			this.posCol=colDest;
			this.setMoveTake(true);
			return true;
		
		}
*/
	}
	
	public Carrion getCarrion()
	{
		return carrion;
	}
	
	public void setCarrion(Carrion carrion)
	{
		this.carrion=carrion;
	}

}
