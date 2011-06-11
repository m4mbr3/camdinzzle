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
	/**
	 * parametro che contiene la caogna presente nella cella quando il dinosauro si trova sopra di essa
	 */
	private Carrion carrion=null;
	/**
	 * parametro che contiene la vegetazione presente nella cella quando il dinosauro si trova sopra di essa
	 */
	private Vegetation vegetation=null;

	/**
	 * richiama il costruttore di Dinosaur e setta la distanza massima per il movimento a 2
	 * 
	 * @param dinoId
	 * @param posRow
	 * @param posCol
	 * @param specie
	 */
	public Vegetarian(String dinoId, int posRow, int posCol, Species specie)
	{
		super(dinoId, posRow, posCol, specie);
		setDistMax(2);
	}
	
	@Override
	public void eat(Object cell)
	{
		if(cell instanceof Vegetation)
		{
			energy += ((Vegetation)cell).getPower();
			if(energy > energyMax)
			{
				energy = energyMax;
			}
		}
	}

	@Override
	public boolean fight(Object dino) 
	{
		int powerAttack;
		int powerDefense;
		
		powerAttack = energy * this.getDinoDimension();		//calcola la forza del dinosauro che attacca
		
		powerDefense = 2 * ((Carnivorous) dino).energy * ((Dinosaur) dino).getDinoDimension();		//calcola la forza del dinosauro in difesa
		

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
	
	/**
	 * ritorna la carogna che sta nella stessa cella del dinosauro
	 * @return Carrion
	 */
	public Carrion getCarrion()
	{
		return carrion;
	}
	
	/**
	 * setta la carogna che c'era sulla mappa dove ora si e' posizionato il dinosauro
	 * @param carrion
	 */
	public void setCarrion(Carrion carrion)
	{
		this.carrion=carrion;
	}

	/**
	 * setta la vegetazione che c'era sulla mappa dove ora si e' posizionato il dinosauro
	 * @param vegetation
	 */
	public void setVegetation(Vegetation vegetation) 
	{
		this.vegetation = vegetation;
	}

	/**
	 * ritorna la vegetazione che sta nella stessa cella del dinosauro
	 * @return Vegetation
	 */
	public Vegetation getVegetation() 
	{
		return vegetation;
	}

}
