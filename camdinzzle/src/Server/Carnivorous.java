package Server;

import java.io.Serializable;

/**
 * 
 * @author Marco
 *
 */

public class Carnivorous extends Dinosaur implements Serializable
{
	private static final long serialVersionUID = 1L;
	private Vegetation vegetation=null;

	public Carnivorous(String dinoId, int posRow, int posCol, Species specie) 
	{
		super(dinoId, posRow, posCol, specie);
		setDistMax(3);
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
		

		if(vegetation!=null)
		{
			Game.setCellMap(vegetation, this.getPosRow(), this.getPosCol());
			nameSpecie.setCellMap(vegetation.toString(), this.getPosRow(), this.getPosCol());
			vegetation=null;
		}
		else
		{
			nameSpecie.setCellMap("t", this.getPosRow(), this.getPosCol());
			Game.setCellMap("t", this.getPosRow(), this.getPosCol());
		}
		energy -= 10 * Math.pow(2, Game.getDistCell(this.getPosRow(), this.getPosCol(), rowDest, colDest));
		if(energy<=0)
		{
			return false;
		}
		
		this.posRow=rowDest;
		this.posCol=colDest;
		setLocalMap();
		super.nameSpecie.updateMap();
		this.setMoveTake(true);
		return true;

	
	}
	
	public Vegetation getVegetation()
	{
		return vegetation;
	}
	
	public void setVegetation(Vegetation vegetation)
	{
		this.vegetation=vegetation;
	}

}
