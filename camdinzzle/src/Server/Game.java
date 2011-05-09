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
	private Object[][] map;
	private static final int maxRow=40;
	private static final int maxCol=40;
	private int Water=164;
	private int Carrion=20;
	private int Vegetation=512;
	
	
	public Game() {
		// TODO Auto-generated constructor stub
	}
	public void addPlayer()
	{
		
	}
	private void square(int number, int row, int col)
	{
		for(int i=row; i<)
		map[row][col] =  new String("t");
		
	}
	private void horseshoe(int number)
	{
		
	}
	private void aaaaaa (int number)
	{
	
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
						 */
						case 0:
						{
							int number = (int) (Math.random() * 16 + 5);
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
						 */
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
						 */
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
						 */
						case 3:
						{
							map[row][col] = new String("t");
							break;
						}
					}
				}
			}
		}
		
	}

}
