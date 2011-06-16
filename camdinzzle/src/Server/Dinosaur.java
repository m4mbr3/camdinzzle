package Server;

import java.io.Serializable;

/**
 * Classe che rappresenta un dinosauro all'interno del gioco.
 */

public abstract class Dinosaur implements Serializable
{
	private static final long serialVersionUID = 1L;
	/*
	 * Identificatore del dinosauro.
	 */
	private String dinoId;
	/*
	 * Eta' massima del dinosauro.
	 */
	private int age;
	/*
	 * Specie del dinosauro.
	 */
	protected Species nameSpecie;
	/*
	 * Energia del dinosauro.
	 */
	protected int energy;
	/*
	 * Dimensione del dinosauro.
	 */
	private int dimension;
	/*
	 * Energia massima che il dinosauro puo' accumulare.
	 */
	protected int energyMax;
	/*
	 * Energia per creare un nuovo dinosauro.
	 */
	private final int eEgg = 1500;
	/*
	 * Riga della posizione del dinosauro.
	 */
	protected int posRow;
	/*
	 * Colonna della posizione del dinosauro.
	 */
	protected int posCol;
	/*
	 * Vista locale del dinosauro.
	 */
	private Object[][] localMap;
	/*
	 * Distanza massima che puo' percorrere il dinosauro.
	 */
	private int distMax;
	/*
	 * Check per movimento gia' effettuato.
	 */
	private boolean moveTake=false;
	/*
	 * Check per azione nel turno gia' effettuata.
	 */
	private boolean actionTake=false;
	/*
	 * Numero di turni gia' vissuti dal dinosauro.
	 */
	private int timeOfLive;
	
	/**
	 * Controlla cosa c'e' nella cella e aggiunge l'energia al dinosauro senza superare la massima 
	 * energia possibile.
	 * @param cell cella di destinazione della mappa
	 */
	public abstract void eat(Object cell);
	/**
	 * Calcola l'energia dei due dinosauri coninvolti nel combattimento.
	 * @param dino riferimento al dinosauro dell'avversario
	 * @return true se il dinosauro ha vinto il combattimento, false altrimenti
	 */
	public abstract boolean fight(Object dino);
	/**
	 * Risetta la cella di partenza con la carogna o vegetazione se c'erano, calcola l'energia per spostarsi e in caso positivo setta
	 * la posizione del dinosauro, aggiorna la mappa locale, aggiorna la mappa della specie e setta il moveTake a true
	 * @param rowDest riga di destinazione
	 * @param colDest colonna di destinazione
	 * @return true se il dinosauro ha abbastanza energia per muoversi, false altrimenti
	 */
	public abstract boolean move(int rowDest, int colDest);
	/**
	 * Crea un nuovo dinosauro con eta' = (30±20%), dimensione = 1 ed energia = 1000.
	 * @param dinoId id del dinosauro da creare
	 * @param posRow riga nella mappa dove posizionare il dinosauro
	 * @param posCol colonna nella mappa dove posizionare il dinosauro
	 */
	public Dinosaur(String dinoId, int posRow, int posCol, Species nameSpecie)
	{
		timeOfLive=0;
		age = (int) (Math.random() * 13 + 24);
		dimension = 1;
		energy = 1000;
		this.dinoId = dinoId;
		energyMax = 1000 * dimension;
		this.posRow = posRow;
		this.posCol = posCol;
		distMax=0;
		this.nameSpecie = nameSpecie;
		actionTake=false;
		moveTake=false;
		setLocalMap();
	}

	/*@ensures (* if (requires dimension < 5  &&  energy > energyMax/2)*) ==> (dimension == dimension + 1 && energyMax == dimension*1000 && \result == true);
  	  @ 		|| !(*Se invece e' no*) ==> \result == false
	  @*/
	/**
	 * Controlla che la dimensione non sia gia' massima (=5).
	 * Controlla che abbia abbanstanza energia per crescere, cresce , toglie l'energia necessaria, aumenta l'energia massima
	 * e setta actionTake a true, altrimenti muore
	 * @return true se il dinosauro cresce, false altrimenti
	 */
	public boolean growUp()
	{
		if(energy > (energyMax/2) && (dimension < 5))
		{
			energy = energy - energyMax/2;
			dimension = dimension + 1;
			energyMax = dimension * 1000;
			setActionTake(true);
			return true;
		}
		else 
			return false;
	}
	
	
	//@ensures (\exists int energy; energy != 0; energy > eEgg)  ==> ( (*Creo l'uovo e lo posiziono sulla mappa*) && \result = (* All'idDino *));
	//		|| !(*ritorno null*) ==> \result = null;
	/*@ assignable energy; @*/
	/**
	 * Crea un nuovo dinosauro nella prima cella libera vicino al dinosauro.
	 * @return dinoId id del nuovo dinosauro
	 */
	public String newEgg()
	{
		String idDino=null;
		Dinosaur newDino;
		if(energy > eEgg)
		{
			energy = energy - eEgg;
			int offSet = 1;
			boolean positioned = false;
			do
			{
				for(int i=posRow-offSet; i<posRow+offSet+1; i++ )
				{
					for(int j=posCol-offSet; j<posCol+offSet+1; j++)
					{
						if((i>=0)&&(i<Game.maxRow)&&(j>=0)&&(j<Game.maxCol))
						{					
							if((Game.getCell(i, j) instanceof String)&&(((String)Game.getCell(i, j)).compareTo("t")==0))
							{
								newDino=nameSpecie.addDinosaur(i, j);
								idDino=newDino.getDinoId();
								positioned = true;
								newDino.setActionTake(true);
								newDino.setMoveTake(true);
								break;
							}
							
						}
					}
					if(positioned) break;
				}
				offSet++;
				
			}while(!positioned);
			setActionTake(true);
			return idDino;
		}
		else 
		{
			return idDino;
		}
	}
	
	/**
	 * Ritorna il tipo della specie del dinosauro.
	 * @return speciesType tipo della specie
	 */
	public Species.type getType()
	{
		return nameSpecie.getType();
	}
	/**
	 * Ritorna l'id del dinosauro.
	 * @return dinoId id del dinosauro
	 */
	public String getDinoId()
	{
		return dinoId;
	}
	
	/**
	 * Aggiorna lo stato del dinosauro. Sottrae un anno di vita a quelli rimanenti al dinosauro, 
	 * aggiunge uno ai turni vissuti dal dinosauro e risetta a false moveTake e ActionTake.
	 */
	public void updateDinosaurAge()
	{
		timeOfLive += 1;
		age -= 1;
		setMoveTake(false);
		setActionTake(false);
	}
	
	/**
	 * Ritorna la dimensione dinosauro.
	 * @return int dimensione del dinosauro
	 */
	public int getDinoDimension()
	{
		return dimension;
	}
	
	/**
	 * Ritorna la riga della posizione del dinosauro.
	 * @return int riga della posizione del dinosauro
	 */
	public int getPosRow()
	{
		return posRow;
	}

	/**
	 * Ritorna la colonna della posizione del dinosauro.
	 * @return int colonna della posizione del dinosauro
	 */
	public int getPosCol()
	{
		return posCol;
	}
	
	/**
	 * Ritorna l'energia dinosauro.
	 * @return int energia del dinosauro
	 */
	public int getEnergy()
	{
		return energy;
	}
	
	/**
	 * Ritorna turni di vita mancanti al dinosauro prima di morire.
	 * @return int turni di vita mancanti ad un dinosauro
	 */
	public int getAge()
	{
		return age;
	}
	
	/**
	 * Ritorna i turni vissuti dal dinosauro.
	 * @return int turni vissuti dal dinosauro
	 */
	public int getTimeOfLive()
	{
		return timeOfLive;
	}
	
	/**
	 * Ritorna il numero di righe della vista locale.
	 * @return  int numero di righe della vista locale
	 */
	public int getSizeRowLocalMap()
	{		
		return localMap.length;
	}
	
	/**
	 * Ritorna il numero di colonne della vista locale.
	 * @return int numero di colonne della vista locale
	 */
	public int getSizeColLocalMap()
	{
		return localMap[0].length;
	}
	
	/**
	 * Setta la vista locale del dinosauro.
	 */
	public void setLocalMap()
	{
		localMap = Game.getLocalMap(posRow, posCol, dimension);		
	}
	
	/**
	 * Ritorna la mappa locale del dinosauro.
	 * @return matrice contenente la vista locale del dinosauro
	 */
	public Object[][] getLocalMap()
	{
		setLocalMap();
		 return localMap;
	}
	
	public String toString()
	{
		return "d";
	}
	
	/**
	 * Setta la distanza massima percorribile dal dinosauro.
	 * @param distMax distanza massima percorribile
	 */
	public void setDistMax(int distMax)
	{
		this.distMax = distMax;
	}
	
	/**
	 * Ritorna la distanza massima percorribile dal dinosauro.
	 * @return int distanza massima percorribile da un dinosauro
	 */
	public int getDistMax()
	{
		return distMax;
	}
	
	/**
	 * Setta la variabile moveTake.
	 * @param moveTake valore da assegnare a moveTake
	 */
	public void setMoveTake(boolean moveTake) 
	{
		this.moveTake = moveTake;
	}
	
	/**
	 * Ritorna la variabile moveTake.
	 * @return variabile moveTake
	 */
	public boolean getMoveTake() 
	{
		return moveTake;
	}
	
	/**
	 * Setta la variabile actionTake.
	 * @param actionTake valore da assegnare a actionTake
	 */
	public void setActionTake(boolean actionTake) 
	{
		this.actionTake = actionTake;
	}
	
	/**
	 * Ritorna la variabile actionTake
	 * @return valore di actionTake
	 */
	public boolean getActionTake() 
	{
		return actionTake;
	}
	
	/**
	 * Ritorna il riferimento alla specie del dinosauro.
	 * @return specie del dinosauro
	 */
	public Species getSpecie()
	{
		return nameSpecie;
	}
	
	/**
	 * Setta l'energia del dinosauro
	 * @param energy energia del dinosauro
	 */
	public void setEnergy(int energy)
	{
		this.energy = energy;
	}
}


