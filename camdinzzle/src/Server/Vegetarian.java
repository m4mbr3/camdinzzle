package Server;

import java.io.Serializable;

/**
 * 
 * @author Marco
 *
 */

public class Vegetarian extends Dinosaur implements Serializable
{
	private static final long serialVersionUID = 1L;
	private Carrion carrion=null;
	private Vegetation vegetation=null;

	public Vegetarian(String dinoId, int posRow, int posCol, Species specie)
	{
		super(dinoId, posRow, posCol, specie);
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
			nameSpecie.setCellMap(carrion.toString(), this.getPosRow(), this.getPosCol());
			carrion=null;
		}
		else if(vegetation!=null)
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
	
	public Carrion getCarrion()
	{
		return carrion;
	}
	
	public void setCarrion(Carrion carrion)
	{
		this.carrion=carrion;
	}

	public void setVegetation(Vegetation vegetation) 
	{
		this.vegetation = vegetation;
	}

	public Vegetation getVegetation() 
	{
		return vegetation;
	}

}
