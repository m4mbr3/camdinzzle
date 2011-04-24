package Server;

public class Vegetarian extends Dinosaur
{

	public Vegetarian(String dinoId, int posX, int posY)
	{
		super(dinoId, posX, posY);
	}
	
	@Override
	public int eat() 
	{

		return 0;
	}

	@Override
	public void fight() 
	{

		
	}

	@Override
	public boolean move() 
	{

		return false;
	}

}
