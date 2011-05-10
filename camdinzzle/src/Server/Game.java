package Server;

import org.omg.CosNaming.IstringHelper;

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
	private Object[][] map;
	private static final int maxRow=40;
	private static final int maxCol=40;
	private int Water=164;
	private int Carrion=20;
	private int Vegetation=512;
	private boolean permise=false;
	
	
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
	private void rectangle(int size, int row, int col)
	{
		//controllo contorno ???
		int controllo=0;
		for(int i=row-1; i<= row+3; i++)
		{
			for(int j=col-1; j<=size/3; j++)
			{
				controllo++;
				if((map[i][j] instanceof java.lang.String)&&(map[i][j].equals("t")))
				{
					controllo--;
				}
			}
		}
		if(controllo==0) permise=true;
		if(permise==true)
		{
			for(int i=row; i<row+3; i++)
			{
				for(int j=col; j<size/3; j++)
				{
					map[i][j] =  new String("a");
					Water -= 1;
				}
			
			}
		}
		
		
	}
	private void horseshoe(int number)
	{
		
	}
	private void star(int size, int row, int col)
	{
		//controllo contorno ???
		int controllo=0;
		for(int i=row-1; i<= row+3; i++)
		{
			for(int j=col-1; j<=size/3; j++)
			{
				controllo++;
				if((map[i][j] instanceof java.lang.String)&&(map[i][j].equals("t")))
				{
					controllo--;
				}
			}
		}
		if(controllo==0) permise=true;
	}
	public void createMap()
	{
		/**
		 * creazione bordo d'acqua
		 */
		map = new Object[40][40];
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
		for(int row=1; row<maxRow-1; row++)
		{
			for(int col=1; col<maxCol-1; col++)
			{
				if(map[row][col]==null)
				{
					switch ((int) (Math.random() * 4))
					{
					
						/**
						 * creazione pozza d'acqua con dim comprese tra 5 e 20
						 *//*
						case 0:
						{
							int number = (int) (Math.random() * 11 + 5);
							if(Water>=number)
							{
								//map[row][col] = new String("a");
								Water -= number;
								//chiamare metodo che crea pozza
								break;
							}
						}
						/**
						 * casella di vegetazione se si pu˜ inerirne ancora
						 *//*
						case 1:
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
						 *//*
						case 2:
						{
							if(Carrion>0)
							{
								map[row][col] = new Carrion((int) (Math.random() * 301 + 350));
								Carrion -= 1;
								break;
							}
						}
						/**
						 * casella di terra vuota
						 *//*
						case 3:
						{
							map[row][col] = new String("t");
							break;
						}
					}
				}
			}
		}
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
			int row = (int)(Math.random() * 37 + 2); // random da 2 a 38
			int col = (int)(Math.random() * 37 + 2);
			int size = (int)(Math.random() * 11 + 5);
			if(Water >= size)
			{
				switch (size) // random da 5 a 15
				{
					case 5:
					{
						//figura fiume
					}
					case 6:
					{
						rectangle(size,row,col);
					}
					case 7:
					{
						//figuara fiume
					}
					case 8:
					{
						//figura ferro cavallo
					}
					case 9:
					{
						rectangle(size,row,col);
					}
					case 10:
					{
						//figura stella
					}
					case 11:
					{
						//figura fiume
					}
					case 12:
					{	
						rectangle(size,row,col);
					}
					case 13:
					{
						//figura stella
					}
					case 14:
					{
						//figura ferro cavallo
					}
					case 15:
					{
						//figuara ferro cavallo
					}
				}
			}
		}while(Water>=5);
		
		
	}

}
