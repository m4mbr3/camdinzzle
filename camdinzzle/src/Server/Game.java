package Server;


/**
 * 
 */

/**
 * @author Andrea
 *
 */
public class Game {

	/**
	 * 
	 */
	private String gameid;
	private static Object[][] map;
	public static final int maxRow=40;
	public static final int maxCol=40;
	private int Water = 164;
	private int Carrion=20;
	private int Vegetation=512;
	private boolean permise=true;
	
	
	public Game() {
		// TODO Auto-generated constructor stub
	}
	public void addPlayer()
	{
		
	}
	
	/**
	 * controlla che ci sia spazio e crea una pozza rettangolare
	 * @param size
	 * @param row
	 * @param col
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
	
	/**
	 * 
	 * @param size
	 * @param row
	 * @param col
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
	/**
	 * 
	 * @param size
	 * @param row
	 * @param col
	 * @param map
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
				if((ctrl<ctrlOffset)&&espansione)//entra se  sopra al punto centrale
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
	
	/**
	 * 
	 * @param size
	 * @param row
	 * @param col
	 * @param map
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
	 * crea una nuova maooa random
	 */
	public void createMap()
	{
		/**
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
		/**
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
		
		/**
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

		
	
	public void stampa()
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
	
	
	public static Object getCell(int row, int col)
	{
		return map[row][col];
	}

}
