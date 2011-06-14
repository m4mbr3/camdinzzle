package Server;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Classe che rappresenta la sessione di gioco corrente. Contiene informazioni riguardanti i giocatori
 * attualmente in partita e contiene la mappa generale.
 */
public class Game
{
	/*
	 * Mappa generale del gioco.
	 */
	private static Object[][] map;
	/**
	 * Numero massimo di righe della mappa.
	 */
	public static final int maxRow=40;
	/**
	 * Numero massimo delle colonne della mappa.
	 */
	public static final int maxCol=40;
	/**
	 * Grandezza massima della mappa di raggiungibilita'.
	 */
	public static final int maxReach=7;
	/**
	 * Distanza massima percorribile da un dinosauro.
	 */
	public static final int distMax=3;
	/*
	 * Numero massimo di celle d'acqua.
	 */
	private int Water = 164;
	/*
	 * Numero di carogne.
	 */
	private int Carrion=20;
	/*
	 * Numero di vegetazione.
	 */
	private int Vegetation=512;
	
	private boolean permise=true;
	/*
	 * Numero massimo di giocatori attualmente in gioco.
	 */
	private int maxPlayers;
	/*
	 * Lista dei giocatori in gioco. 
	 * La LinkedHashMap ha come chiave il token del giocatore.
	 */
	private LinkedHashMap<String, Player> playersInGame;
	/*
	 * Mappa di raggiungibilita'.
	 */
	private static int[][][][] mapReach;

	/**
	 * Istanzia un nuovo Game.
	 * Crea una nuova mappa se non esiste, altrimenti la carica da file e inizializza la mappa 
	 * di raggiungibilita'.
	 * @param mapFromFile riferimento alla mappa
	 */
	public Game(Object[][] mapFromFile) 
	{
		playersInGame = new LinkedHashMap<String, Player>();
		maxPlayers = 8;
		
		if(mapFromFile == null)
		{
			this.createMap();
			LogHelper.writeInfo("mappa di gioco creata.");
		}
		else
		{
			LogHelper.writeInfo("mappa di gioco caricata da file");
			map = mapFromFile;
		}
		
		this.startMapReach();
	}
	
	/**
	 * Ritorna la mappa generale della partita.
	 * @return matrice contenente la mappa generale
	 */
	public Object[][] getGeneralMap()
	{
		return map;
	}
	
	/**
	 * Ritorna il numero massimo di giocatori che possono partecipare contemporaneamente alla partita.
	 * @return numero massimo dei giocatori
	 */
	public int getMaxPlayers() 
	{
		return maxPlayers;
	}

	/**
	 * Setta il numero massimo di giocatori della partita.
	 * @param maxPlayers numero massimo di giocatori
	 */
	public void setMaxPlayers(int maxPlayers) 
	{
		this.maxPlayers = maxPlayers;
	}

	/**
	 * Aggiunge un nuovo giocatore alla lista dei giocatori gia' in partita.
	 * @param token il token del giocatore da aggiungere
	 * @param newPlayer oggetto Player da aggiungere alla lista
	 * @return true se non esiste un giocatore in partita con il token, false altrimenti
	 */
	public boolean addPlayer(String token, Player newPlayer)
	{
		synchronized (playersInGame) 
		{
			if(!playersInGame.containsKey(token))
			{
				playersInGame.put(token, newPlayer);
				return true;
			}
			return false;
		}
	}
	
	/**
	 * Ritorna un oggetto Player corrispondente al Player che ha come chiave il token.
	 * @param token il token del Player
	 * @return Player corrispondente al token. Se il nessun Player corrisponde al token viene ritornato
	 * null
	 */
	public Player getPlayer(String token)
	{
		synchronized (playersInGame)
		{
			if(playersInGame.containsKey(token))
				return playersInGame.get(token);
			else
				return null;
		}
	}
	
	/**
	 * Rimuove un giocatore dalla lista dei giocatori in partita attualmente.
	 * @param token il token del giocatore
	 * @return true se il giocatore era presente ed è stato eliminato, false altrimenti
	 */
	public boolean removePlayer(String token)
	{
		synchronized (playersInGame) 
		{
			if(playersInGame.containsKey(token))
			{
				playersInGame.remove(token);
				
				return true;
			}
			return false;
		}
	}
	
	/**
	 * Ritorna il umero di giocatori in partita.
	 * @return numero di giocatori in partita
	 */
	public int numberPlayersInGame()
	{
		synchronized (playersInGame)
		{
			return playersInGame.size();
		}
	}
	
	/**
	 * Ritorna la lista dei giocatori in partita.
	 * @return Iterator con la lista dei giocatori in partita
	 */
	public Iterator<Map.Entry<String, Player>> getPlayersList()
	{
		synchronized (playersInGame) 
		{
			Set<Map.Entry<String, Player>> set = playersInGame.entrySet();
			
			return set.iterator();
		}
	}
	
	/**
	 * Ritorna il token del giocatore in testa alla lista dei giocatori attualmente in partita.
	 * @return il token del giocatore
	 */
	public String getFirstPlayer()
	{
		synchronized (playersInGame) 
		{
			if(!playersInGame.isEmpty())
			{
				Set<Map.Entry<String, Player>> set = playersInGame.entrySet();
				Iterator<Map.Entry<String, Player>>  iter = set.iterator();
				Map.Entry<String, Player> me = (Map.Entry<String, Player>) iter.next();
				
				return me.getKey().toString();
			}
			return null;
		}
	}
	
	/*
	 * Controlla che ci sia spazio nella mappa e crea una pozza di acqua rettangolare.
	 * @param size dimensione della pozza di acqua
	 * @param row altezza della pozza
	 * @param col base della pozza
	 */
	private void rectangle(int size, int row, int col, Object[][] map)
	{
		//controllo contorno
		permise=true;
		for(int i=row-1; i<= row+3; i++)
		{
			for(int j=col-1; j<=col+size/3; j++)
			{
				
				if((map[i][j] instanceof java.lang.String)&&(map[i][j].equals("a")))
				{
					permise=false;
					break;
				}
			}
			if(!permise) break;
		}
		if(permise==true)
		{
			for(int i=row; i<row+3; i++)
			{
				for(int j=col; j<col+size/3; j++)
				{
					map[i][j] ="a";
					Water -= 1;
				}
			
			}
		}
		
		
	}
	
	/*
	 * Controlla che ci sia spazio e crea una pozza di acqua a ferro di cavallo.
	 * @param size dimensione della pozza
	 * @param row numero di righe occupate dalla pozza
	 * @param col numero di colonne occupate dalla pozza
	 */
	private void horseshoe(int size, int row, int col, Object[][] map)
	{
		//controllo contorno
		permise=true;
		int ctrlOffsetRow, ctrlOffsetCol;
		if(size==8)
		{
			ctrlOffsetRow = 2;
			ctrlOffsetCol = 3;
		}
		else
		{
			if(size==14)
				{
					ctrlOffsetRow = 4;
					ctrlOffsetCol = 5;
				}
			else
			{
				ctrlOffsetRow = 4;
				ctrlOffsetCol = 6;
			}
		}
		for(int i=row-1; i<=row+ctrlOffsetRow+1; i++)
		{
			for(int j=col-1; j<=col+ctrlOffsetCol+1; j++)
			{
				
				if((map[i][j] instanceof java.lang.String)&&(map[i][j].equals("a")))
				{
					permise=false;
					break;
				}
			}
			if(!permise) break;
		}
		if(permise==true)
		{

			for(int j=col; j<=col+ctrlOffsetCol; j++)
			{
				map[row][j] ="a";
				Water -= 1;
			}
			for(int i=row+1; i<=row+ctrlOffsetRow; i++)
			{
				map[i][col] ="a";
				map[i][col+ctrlOffsetCol] ="a";
				Water -= 2;
			}
		}
	}
	/*
	 * Controlla che ci sia spazio e crea una pozza d'acqua a stella.
	 * @param size dimensione della pozza
	 * @param row numero di righe occupate dalla pozza
	 * @param col numero di colonne occupate dalla pozza
	 * @param map matrice contenente la mappa generale
	 */
	private void star(int size, int row, int col, Object[][] map)
	{
		//controllo contorno
		int ctrlOffset;	//differenzia la stella da 5 da quella da 8
		if(size==5)
		{
			ctrlOffset=2;
		}
		else
		{
			ctrlOffset=3;
		}
		permise=true;
		for(int i=row-ctrlOffset; i<= row+ctrlOffset; i++)
		{
			for(int j=col-ctrlOffset; j<=col + ctrlOffset; j++)
			{
				
				if((map[i][j] instanceof java.lang.String)&&(map[i][j].equals("a")))
				{
					permise=false;
					break;
				}
			}
			if(!permise) break;
		}
		//fine controllo contorno
		if(permise)
		{
			int ctrl=0;	//serve x aumentare e diminuire il numero di celle di acqua nella riga
			boolean espansione=true, riduzione=true; //attiva e disattiva l'espansione e la riduzione delle celle stampate
			for(int i=row-ctrlOffset+1; i<=row+ctrlOffset-1; i++)
			{
				if((ctrl<ctrlOffset)&&espansione)//entra se � sopra al punto centrale
				{
					for(int j=col-ctrl; j<=col+ctrl; j++)	//stampala parte sopra
					{
						map[i][j] ="a";
						Water -= 1;
					}
					ctrl++;
				}
				else
				{
					if(riduzione)//entra solo la prima volta
					{
						ctrl--;
						riduzione=false;
					}
					ctrl--;
					espansione=false;
					for(int j=col-ctrl; j<=col+ctrl; j++)	//stampa la parte sotto
					{
						map[i][j] ="a";
						Water -= 1;
					}
				}
				
			}

		}

	}
	
	/*
	 * Controlla che ci sia spazio e crea una pozza di acqua a fiume.
	 * @param size dimensione della pozza
	 * @param row numero di righe occupate dalla pozza
	 * @param col numero di colonne occupate dalla pozza
	 * @param map matrice contenente la mappa generale
	 */
	private void river(int size, int row, int col, Object[][] map)
	{
		//controllo contorno
		permise=true;
		int ctrlOffsetRow, ctrlOffsetCol;
		if(size==7)
		{
			ctrlOffsetRow = 4;
			ctrlOffsetCol = 0;
		}
		else
		{
			if(size==10)
				{
					ctrlOffsetRow = 4;
					ctrlOffsetCol = 3;
				}
			else
			{
				ctrlOffsetRow = 5;
				ctrlOffsetCol = 3;
			}
		}
		for(int i=row-1; i<=row+ctrlOffsetRow+1; i++)
		{
			for(int j=col-ctrlOffsetCol-1; j<=col+2; j++)
			{
				
				if((map[i][j] instanceof java.lang.String)&&(map[i][j].equals("a")))
				{
					permise=false;
					break;
				}
			}
			if(!permise) break;
		}
		if(permise==true)
		{
			for(int i=row; i<=row+ctrlOffsetRow; i++)
			{
				if((i==row)||(i==row+1)||(i==row+3)||(i==row+4))
				{
				map[i][col] ="a";
				Water -= 1;
				}
				if((i==row+1)||(i==row+2)||(i==row+3))
				{
					map[i][col+1] ="a";
					Water -= 1;
				}
				if((i==row+4)&&(size!=7))
				{
					int count = 0;
					do
						{
						map[i][col-count] ="a";
						Water -= 1;
						count++;
						}
					while(count<4);	
				}
				else
				{
					if(i==row+4)
					{	
					map[i][col] ="a";
					Water -= 1;
					}
				}
				if(i==row+5)
				{
					map[i][col-3] ="a";
					Water -= 1;
				}
			}
		}
	}
	
	/**
	 * Crea una nuova mappa di gioco.
	 */
	public void createMap()
	{
		/*
		 * creazione bordo d'acqua
		 */
		map = new Object[maxRow][maxCol];
		for(int row=0; row<maxRow; row++)
		{
			if(row==0||row==(maxRow-1))
			{
				for(int col=0; col<maxCol; col++)
				{
					map[row][col] = new String("a");
				}
			}
			else
			{
				map[row][0] = new String("a");
				map[row][maxRow-1] = new String("a");
			}
		}
		/*
		 * inizializzazione mappa casuale
		 */

		
		/*
		 * inizializza la mappa con tutta terra
		 */
		for(int row=1; row<maxRow-1; row++)
		{
			for(int col=1; col<maxCol-1; col++)
			{
				map[row][col] = new String("t");
			}
		}
		
		/*
		 * creaione pozze d'acqua
		 */
		do
		{
			int row = (int)(Math.random() * (maxRow - 10) + 4); // random da 4 a 33
			int col = (int)(Math.random() * (maxCol - 14) + 5); // random da 5 a 31
			int size = (int)(Math.random() * 11 + 5); //random da 5 a 15
			if(Water >= size)
			{
				switch (size)
				{
					case 5:
					{
						star(size,row,col, map);
						break;
					}
					case 6:
					{
						rectangle(size,row,col, map);
						break;
					}
					case 7:
					{
						river(size,row,col,map);
						break;
					}
					case 8:
					{
						horseshoe(size, row, col, map);
						break;
					}
					case 9:
					{
						rectangle(size,row,col, map);
						break;
					}
					case 10:
					{
						river(size,row,col,map);
						break;
					}
					case 11:
					{
						river(size,row,col,map);
						break;
					}
					case 12:
					{	
						rectangle(size,row,col, map);
						break;
					}
					case 13:
					{
						star(size,row,col, map);
						break;
					}
					case 14:
					{
						horseshoe(size, row, col, map);
						break;
					}
					case 15:
					{
						horseshoe(size, row, col, map);
						break;
					}
				}
			}
		}while(Water>=5);
		/*
		 * crea vegetazione e carogne
		 */
			do
			{
				int row = (int)(Math.random() * (maxRow-1) + 1); // random da 1 a 38
				int col = (int)(Math.random() * (maxCol-1) + 1); // random da 1 a 38
				if((map[row][col] instanceof java.lang.String)&&(map[row][col].equals("t")))
				{
					switch ((int) (Math.random() * 2))
					{
						/**
						 * casella di vegetazione se si pu˜ inerirne ancora
						 */
						case 0:
						{
							if(Vegetation>0)
							{
								map[row][col] = new Vegetation((int) (Math.random() * 201 + 150));
								Vegetation -= 1;
								break;
							}
						}
						/**
						 * casella di carogana se si pu˜ inerirne ancora
						 */
						case 1:
						{
							if(Carrion>0)
							{
								map[row][col] = new Carrion((int) (Math.random() * 301 + 350));
								Carrion -= 1;
								break;
							}
						}
					}
				}
			}while(Vegetation>0 || Carrion>0);
			
	}

	/**
	 * Stampa la mappa generale di gioco.
	 */
	public static void stampa()
	{

		for(int i=0; i<maxRow; i++)
		{
			for(int j=0; j<maxCol; j++)
			{
				System.out.print(map[i][j] + " ");
			}
			System.out.print("\n");
		}
	}
	
	/**
	 * Ritorna il contenuto di una cella della matrice della mappa generale.
	 * @param row riga della cella desiderata
	 * @param col colonna della cella desiderata
	 * @return oggetto contenuto nella cella
	 */
	public static Object getCell(int row, int col)
	{
		return map[row][col];
	}
	
	/**
	 * Ritaglia dalla mappa la vista locale di un dinosauro.
	 * @param row riga del dinosauro
	 * @param col colonna del dinosauro
	 * @param dimension dimensione del dinosauro
	 * @return mappa locale del dinosauro
	 */
	public static Object[][] getLocalMap(int row, int col, int dimension)
	{
		int sizeRow;
		int sizeCol;
		if(dimension==1)
		{
			sizeRow = 5;
			sizeCol = 5;
		}
		else
		{
			if((dimension==2)||(dimension==3))
			{
				sizeRow = 7;
				sizeCol = 7;
			}
			else
			{
				sizeRow = 9;
				sizeCol = 9;
			}
		}
		int k = row - sizeRow/2;
		int h = col - sizeCol/2;
		int startCol = h;
		if(k<0)
		{
			sizeRow += k;
			k=0;
		}
		if(h<0)
		{
			sizeCol += h;
			startCol=0;
		}
		if((k+sizeRow-1)>=maxRow)
		{
			sizeRow += maxRow - (k+sizeRow);
		}
		if((h+sizeCol-1)>=maxCol)
		{
			sizeCol += maxCol - (h+sizeCol);
		}
		
		Object[][] localMap = new Object[sizeRow][sizeCol];
		
		for(int i=0; i<sizeRow; i++)
		{
			h=startCol;
			for(int j=0; j<sizeCol; j++)
			{
				if((k>=0)&&(k<maxRow)&&(h>=0)&&(h<maxCol)&&(i>=0)&&(i<maxRow)&&(j>=0)&&(j<maxCol))
				{
					localMap[i][j] = map [k][h];
				}
				h++;
			}
			k++;
		}
		return localMap;
	}
	
	/**
	 * Inizializza la mappa di raggiungibilità.
	 */
	public void startMapReach()
	{
		mapReach = new int [maxRow][maxCol][maxReach][maxReach];
		int rowRel = maxReach/2;
		int colRel = maxReach/2;
		for(int i=0; i<maxRow; i++)
		{
			for(int j=0; j<maxCol; j++)
			{
				boolean[][] view = new boolean [maxReach][maxReach];
				int[][] dist = new int [maxReach][maxReach];
				dist[maxReach/2][maxReach/2] = 0;
				reachAbleCell(i, j, rowRel, colRel, view, dist, -1);
				mapReach[i][j] = dist;
			}
		}
		
	}
	
	/**
	 * Crea la mappa di raggiungibilita'.
	 * @param rowStart riga di inizio della creazione
	 * @param colStart colonna di inizio della creazione
	 * @param rowRel riga della cella della mappa da controllare
	 * @param colRel colonna della cella della mappa da controllare
	 * @param view matrice delle cella gia' visitate
	 * @param dist matrice delle distanze dalla cella iniziale
	 * @param d distanza della cella relativa rispetto alla cella iniziale
	 */
	public void reachAbleCell(int rowStart, int colStart, int rowRel, int colRel, boolean[][] view, int[][] dist, int d)
	{
		int i=rowRel;
		int j=colRel;
		if((rowStart<=0)||(rowStart>=maxRow-1)||(colStart<=0)||(colStart>=maxCol-1))
		{
			return;
		}
		if(d+1 > distMax)
		{
			return;
		}
		if((i<0)||(i>=maxReach)||(j<0)||(i>=maxReach))
		{
			return;
		}
		if((map[rowStart][colStart] instanceof String)&&(map[rowStart][colStart] == "a"))
		{
			return;
		}
		int distRel = dist[i][j];
		if((view[i][j]==true) && /*(distRel!=0) &&*/ (distRel <= (d+1)))
		{
			return;
		}
		view[i][j] = true;
		if((distRel>(d+1)) || (distRel==0))
		{
			dist[i][j] = d+1;
		}
		
		reachAbleCell(rowStart+1, colStart-1, rowRel+1, colRel-1, view, dist, d+1);
		reachAbleCell(rowStart+1, colStart, rowRel+1, colRel, view, dist, d+1);
		reachAbleCell(rowStart+1, colStart+1, rowRel+1, colRel+1, view, dist, d+1);
		reachAbleCell(rowStart, colStart-1, rowRel, colRel-1, view, dist, d+1);
		reachAbleCell(rowStart, colStart+1, rowRel, colRel+1, view, dist, d+1);
		reachAbleCell(rowStart-1, colStart-1, rowRel-1, colRel-1, view, dist, d+1);
		reachAbleCell(rowStart-1, colStart, rowRel-1, colRel, view, dist, d+1);
		reachAbleCell(rowStart-1, colStart+1, rowRel-1, colRel+1, view, dist, d+1);
	}
	
	/**
	 * Controlla che una cella sia raggiungibile dal dinosauro interessato in base alla massima distanza che puo' percorrere
	 * e alla presenza di un percorso continuo senza acqua in mezzo
	 * @param startRow riga di partenza del controllo 
	 * @param startCol colonna di partenza del controllo
	 * @param destRow riga di destinazione del movimento
	 * @param destCol colonna di destinazione del movimento
	 * @param distMax distanza massima del movimento
	 * @return true se la cella di destinazione e' raggiungibile, false altrimenti
	 */
	public static boolean checkReachCell(int startRow, int startCol, int destRow, int destCol, int distMax)
	{
		int destRelRow = maxReach/2 + (destRow - startRow);
		int destRelCol = maxReach/2 + (destCol - startCol);
		if((startRow>=0)&&(startRow<maxRow)&&(startCol>=0)&&(startCol<maxCol)&&(destRelRow>=0)&&(destRelRow<maxReach)&&(destRelCol>=0)&&(destRelCol<maxReach))
		{
			if((mapReach[startRow][startCol][destRelRow][destRelCol]<=distMax)&&(mapReach[startRow][startCol][destRelRow][destRelCol]>0))
			{
				return true;
			}
		}
		return false;
	}
	
	public static void stampaReachAble(int row, int col)
	{
		for(int i=0; i<maxReach; i++)
		{
			for(int j=0; j<maxReach; j++)
			{
				System.out.print(mapReach[row][col][i][j] + " ");
			}
			System.out.print("\n");
		}
	}
	
	/**
	 * Inserisce nella mappa generale l'oggetto passato.
	 * @param obj oggetto da inserire nella mappa
	 * @param row riga della mappa in cui inserire l'oggetto
	 * @param col colonna della mappa in cui inserire l'oggetto
	 */
	public static void setCellMap(Object obj, int row, int col)
	{
		synchronized(map)
		{
			if((row>=0)&&(row<maxRow)&&(col>=0)&&(col<maxCol)&&(obj!=null))
			{
				map[row][col] = null;
				map[row][col] = obj;
			}
		}
	}
	
	/**
	 * Legge la mappa di raggiungibilità e restituisce la distanza tra la cella di partenza e quella 
	 * di arrivo
	 * @param startRow riga di inizio del controllo
	 * @param startCol colonna di inizio del controllo
	 * @param destRow riga di destinazione del controllo
	 * @param destCol colonna di destinazione del controllo
	 * @return  distanza delle celle controllate
	 */
	public static int getDistCell(int startRow, int startCol, int destRow, int destCol)
	{
		if((startRow>=0)&&(startRow<maxRow)&&(startCol>=0)&&(startCol<maxCol)&&(destRow>=0)&&(destRow<maxRow)&&(destCol>=0)&&(destCol<maxCol))
		{
			int destRelRow = maxReach/2 + (startRow - destRow);
			int destRelCol = maxReach/2 + (startCol - destCol);
			if((destRelRow>=0)&&(destRelRow<maxReach)&&(destRelCol>=0)&&(destRelCol<maxReach))
			{
				return mapReach[startRow][startCol][destRelRow][destRelCol];
			}
			else
			{
				return -1;
			}
		}
		else
		{
			return -1;
		}
	}
	
/*	public void newCreateMap()
	{
		
		 // creazione bordo d'acqua
		 
		map = new Object[maxRow][maxCol];
		for(int row=0; row<maxRow; row++)
		{
			if(row==0||row==(maxRow-1))
			{
				for(int col=0; col<maxCol; col++)
				{
					map[row][col] = new String("a");
				}
			}
			else
			{
				map[row][0] = new String("a");
				map[row][maxRow-1] = new String("a");
			}
		}
		
		 // inizializzazione mappa casuale
		 

		
		
		 // inizializza la mappa con tutta terra
		 
		for(int row=1; row<maxRow-1; row++)
		{
			for(int col=1; col<maxCol-1; col++)
			{
				map[row][col] = new String("t");
			}
		}
		
		
		 // creaione pozze d'acqua
		 
		int posRow;
		int posCol;
		do
		{
			posRow = (int) (Math.random() * 36 + 2);
			posCol = (int) (Math.random() * 36 + 2);
		}while(!((Game.getCell(posRow, posCol) instanceof String)&&(((String)Game.getCell(posRow, posCol)).compareTo("t")==0)));
		
		
	}
	
	private boolean controlWater(int startRow, int startCol, int destRow, int destCol)
	{
		if((startRow>=0)&&(startRow<maxRow)&&(startCol>=0)&&(startCol<maxRow)&&(destRow>=0)&&(destRow<maxRow)&&(destCol>=0)&&(destCol<maxRow))
		{
			if(((Game.getCell(destRow, destCol) instanceof String)&&(((String)Game.getCell(destRow,destCol)).compareTo("a")==0)))
			{
				return false;
			}
			else
			{
				if(!((destRow+1==startRow)&&(destCol==startCol)))
				{
					if((destRow+1<0)||(destRow+1>=maxRow))
					{
						return false;
					}
					if(((Game.getCell(destRow+1, destCol) instanceof String)&&(((String)Game.getCell(destRow+1, destCol)).compareTo("a")==0)))
					{
						return false;
					}
				}
				if(!((destRow-1==startRow)&&(destCol==startCol)))
				{
					if((destRow-1<0)||(destRow-1>=maxRow))
					{
						return false;
					}
					if(((Game.getCell(destRow-1, destCol) instanceof String)&&(((String)Game.getCell(destRow-1, destCol)).compareTo("a")==0)))
					{
						return false;
					}
				}
				if(!((destRow==startRow)&&(destCol+1==startCol)))
				{
					if((destCol+1<0)||(destCol+1>=maxRow))
					{
						return false;
					}
					if(((Game.getCell(destRow, destCol+1) instanceof String)&&(((String)Game.getCell(destRow, destCol+1)).compareTo("a")==0)))
					{
						return false;
					}
				}
				if(!((destRow==startRow)&&(destCol-1==startCol)))
				{
					if((destCol-1<0)||(destCol-1>=maxRow))
					{
						return false;
					}
					if(((Game.getCell(destRow, destCol-1) instanceof String)&&(((String)Game.getCell(destRow, destCol-1)).compareTo("a")==0)))
					{
						return false;
					}
				}
				return true;

			}
		}
		else
			return false;
		
	}*/
	
	/**
	 * Riposiziona nella mappa in modo casuale un oggetto di tipo Carrion.
	 * @param row riga della posizione iniziale
	 * @param col colonna della posizione iniziale
	 */
	public void repositionCarrion(int row, int col)
	{
		int posRow,posCol;
		do
		{
			posRow = (int) (Math.random() * 40);
			posCol = (int) (Math.random() * 40);
		}
		while(!((Game.getCell(posRow, posCol) instanceof String)&&(((String)Game.getCell(posRow, posCol)).compareTo("t")==0)));
		
		setCellMap(getCell(row, col), posRow, posCol);
	}
	
	/**
	 * Controlla che il giocatore sia in gioco.
	 * @param token il token del giocatore su cui fare il controllo
	 * @return true se il giocatore e' in gioco, false altrimenti
	 */
	public boolean isPlayerInGame(String token)
	{
		synchronized (playersInGame) 
		{
			if(playersInGame.containsKey(token))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
	}

}
