package Server;

import java.io.Serializable;

/**
 * Classe che rappresenta un dinosauro all'interno del gioco.
 */

public abstract class Dinosaur implements Serializable
{
	//OVERVIEW: Rappresenta il Dinosauro e ingloba le caratteristiche comuni tra Erbivori e Carnivori
	//mentre devolve la definizione delle differenze alle classi che la implementano
	
	private static final long serialVersionUID = 1L;
	//@public invariant dinoId != null;
	/*
	 * Identificatore del dinosauro.
	 */
	private /*@spec_public@*/ String dinoId;
	//@public invariant age >0;
	/*
	 * Eta' massima del dinosauro.
	 */
	private /*@spec_public@*/ int age;
	//@public invariant nameSpecie != null;
	/*
	 * Specie del dinosauro.
	 */
	protected /*@spec_public@*/ Species nameSpecie;
	//@public invariant energy>0;
	/*
	 * Energia del dinosauro.
	 */
	protected /*@spec_public@*/  int energy;
	
	//@public invariant dimension >=1 && dimension <=5;
	/*
	 * Dimensione del dinosauro.
	 */
	private /*@spec_public@*/int dimension;
	//@public invariant energyMax > 0;
	/*
	 * Energia massima che il dinosauro puo' accumulare.
	 */
	protected /*@spec_public @*/ int energyMax;
	//@public invariant eEgg == 1500
	/*
	 * Energia per creare un nuovo dinosauro.
	 */
	private /*@ spec_public @*/ final int eEgg = 1500;
	
	//@public inviariant posRow>=0 && posRow<=40;
	/*
	 * Riga della posizione del dinosauro.
	 */
	protected /*@spec_public@*/int posRow;
	//@public invariant posCol>=0 && posCol<=40;
	/*
	 * Colonna della posizione del dinosauro.
	 */
	protected /*@ spec_public @*/ int posCol;
	
	//@public inviariant localMap != null;
	/*
	 * Vista locale del dinosauro.
	 */
	private /*@spec_public @*/ Object[][] localMap;
	
	//@public invariant distMax >0;
	/*
	 * Distanza massima che puo' percorrere il dinosauro.
	 */
	private /*@ spec_public @*/int distMax;
	
	//@public invariant moveTake != null;
	/*
	 * Check per movimento gia' effettuato.
	 */
	private /*@spec_public@*/ boolean moveTake=false;
	
	//@public invariant actionTake != null; 
	/*
	 * Check per azione nel turno gia' effettuata.
	 */
	private /*@spec_public@*/ boolean actionTake=false;
	
	//@public invariant timeOfLiva >=0; 
	/*
	 * Numero di turni gia' vissuti dal dinosauro.
	 */
	private /*@spec_public@*/ int timeOfLive;
	
	//@requires cell != null;
	//@ensures (* aggiunge l'energia al dinosauro cn un tetto  che è dato dall'energia max*)
	/**
	 * Controlla cosa c'e' nella cella e aggiunge l'energia al dinosauro senza superare la massima 
	 * energia possibile.
	 * @param cell cella di destinazione della mappa
	 */
	public abstract void eat(Object cell);
	
	//@requires dino != null;
	//@ensures (* calcola l'energia dei due dino coinvolti nel combattimento e torna true se ha vinto *) 
	/**
	 * Calcola l'energia dei due dinosauri coninvolti nel combattimento.
	 * @param dino riferimento al dinosauro dell'avversario
	 * @return true se il dinosauro ha vinto il combattimento, false altrimenti
	 */
	public abstract boolean fight(Object dino);
	//@ requires  rowDest > 0 && rowCol > 0;
	//@ ensures (energy = \old(energy)-(EnergyMax/2))> 0 ==> (this.posRow == rowDest && this.posCol == colDest && this.setLocalMap() && this.updateMap() 
	//@ && setMoveTake(true)&& \return == true)
	/**
	 * Risetta la cella di partenza con la carogna o vegetazione se c'erano, calcola l'energia per spostarsi e in caso positivo setta
	 * la posizione del dinosauro, aggiorna la mappa locale, aggiorna la mappa della specie e setta il moveTake a true
	 * @param rowDest riga di destinazione
	 * @param colDest colonna di destinazione
	 * @return true se il dinosauro ha abbastanza energia per muoversi, false altrimenti
	 */
	public abstract boolean move(int rowDest, int colDest);
	
	//@ requires dinoId != null && posRow >=0 && posRow<=39 && posCol >=0 && posCol<=39 && nameSpecie != null;
	//@ ensures (* Istanzia ed inizializza tutte le variabili legate la Dinosaur*);
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
	
	
	//@ensures (\exists int energy; energy > 0; energy > this.eEgg)  ==> ( (*Creo l'uovo e lo posiziono sulla mappa*) && \result = (* All'idDino *));
	//		|| !(\exists int energy; energy > 0; energy > this.eEgg) ==> \result = null;
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
	//@ensures \result == this.nameSpecie.getType();
	/**
	 * Ritorna il tipo della specie del dinosauro.
	 * @return speciesType tipo della specie
	 */
	public /*@pure@*/ Species.type getType()
	{
		return nameSpecie.getType();
	}
	//@ensures \result == this.dinoId;
	/**
	 * Ritorna l'id del dinosauro.
	 * @return dinoId id del dinosauro
	 */
	public /*@pure@*/ String getDinoId()
	{
		return dinoId;
	}
	//@ensures this.timeOfLive == \old(this.timeOfLive)+1 && this.age == \old(this.age)-1;
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
	//@ensures \result == this.dimension;
	/**
	 * Ritorna la dimensione dinosauro.
	 * @return int dimensione del dinosauro
	 */
	public/*@ pure @*/ int getDinoDimension()
	{
		return dimension;
	}
	//@ensures \result == this.posRow;
	/**
	 * Ritorna la riga della posizione del dinosauro.
	 * @return int riga della posizione del dinosauro
	 */
	public /*@ pure @ */ int getPosRow()
	{
		return posRow;
	}
	//@ensures \result == this.posCol;
	/**
	 * Ritorna la colonna della posizione del dinosauro.
	 * @return int colonna della posizione del dinosauro
	 */
	public /*@ pure @*/ int getPosCol()
	{
		return posCol;
	}
	//@ensures \result = this.energy;
	/**
	 * Ritorna l'energia dinosauro.
	 * @return int energia del dinosauro
	 */
	public /*@ pure @*/ int getEnergy()
	{
		return energy;
	}
	//@ensures \result = this.age;
	/**
	 * Ritorna turni di vita mancanti al dinosauro prima di morire.
	 * @return int turni di vita mancanti ad un dinosauro
	 */
	public /*@ pure @*/ int getAge()
	{
		return age;
	}
	//@ensures \result = this.timeOfLive;
	/**
	 * Ritorna i turni vissuti dal dinosauro.
	 * @return int turni vissuti dal dinosauro
	 */
	public /*@ pure @*/ int getTimeOfLive()
	{
		return timeOfLive;
	}
	//@ensures \result == localMap.length;
	/**
	 * Ritorna il numero di righe della vista locale.
	 * @return  int numero di righe della vista locale
	 */
	public /*@ pure @*/int getSizeRowLocalMap()
	{		
		return localMap.length;
	}
	//@ensures \result == this.localMap[0].length;
	/**
	 * Ritorna il numero di colonne della vista locale.
	 * @return int numero di colonne della vista locale
	 */
	public /*@ pure @*/ int getSizeColLocalMap()
	{
		return localMap[0].length;
	}
	//@ensures this.localMap = Game.getLocalMap(this.posRow, this.posCol, dimension);
	/**
	 * Setta la vista locale del dinosauro.
	 */
	public void setLocalMap()
	{
		localMap = Game.getLocalMap(posRow, posCol, dimension);		
	}
	//@ensures \return =localMap;
	/**
	 * Ritorna la mappa locale del dinosauro.
	 * @return matrice contenente la vista locale del dinosauro
	 */
	public Object[][] getLocalMap()
	{
		setLocalMap();
		 return localMap;
	}
	//@ensures \result = "d";
	public /*@ pure @*/ String toString()
	{
		return "d";
	}
	//@requires distMax>0;
	//@ensures this.distMax = distMax;
	/**
	 * Setta la distanza massima percorribile dal dinosauro.
	 * @param distMax distanza massima percorribile
	 */
	public void setDistMax(int distMax)
	{
		this.distMax = distMax;
	}
	
	//@ensures \return == this.distMax;
	/**
	 * Ritorna la distanza massima percorribile dal dinosauro.
	 * @return int distanza massima percorribile da un dinosauro
	 */
	public /*@ pure @*/ int getDistMax()
	{
		return distMax;
	}
	//@requires moveTake != null;
	//@ensures this.moveTake == moveTake;
	/**
	 * Setta la variabile moveTake.
	 * @param moveTake valore da assegnare a moveTake
	 */
	public void setMoveTake(boolean moveTake) 
	{
		this.moveTake = moveTake;
	}
	
	//@ensures \return = this.moveTake;
	/**
	 * Ritorna la variabile moveTake.
	 * @return variabile moveTake
	 */
	public /*@ pure @*/ boolean getMoveTake() 
	{
		return moveTake;
	}
	//@ requires actionTake != null;
	//@ ensures this.actionTake == actionTake;
	/**
	 * Setta la variabile actionTake.
	 * @param actionTake valore da assegnare a actionTake
	 */
	public void setActionTake(boolean actionTake) 
	{
		this.actionTake = actionTake;
	}
	//@ ensures \return == this.actionTake;
	/**
	 * Ritorna la variabile actionTake
	 * @return valore di actionTake
	 */
	public /*@ pure @*/ boolean getActionTake()	 
	{
		return actionTake;
	}
	
	//@ ensures \return == this.nameSpecie ;
	/**
	 * Ritorna il riferimento alla specie del dinosauro.
	 * @return specie del dinosauro
	 */
	public /*@ pure @*/ Species getSpecie()
	{
		return nameSpecie;
	}
	
	//@requires energy > 0;
	//@ensures this.energy == energy;
	/**
	 * Setta l'energia del dinosauro
	 * @param energy energia del dinosauro
	 */
	public void setEnergy(int energy)
	{
		this.energy = energy;
	}
}


