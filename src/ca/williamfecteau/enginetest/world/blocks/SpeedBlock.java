package ca.williamfecteau.enginetest.world.blocks;


import ca.williamfecteau.enginetest.entities.Player;
import ca.williamfecteau.enginetest.world.Stage;

public class SpeedBlock extends Block
{

	public static final float SPEED_RATE = 0.5F;
	public static final float SPEED_MAX = 50;

	
	public SpeedBlock(int x, int y, int color) 
	{
		super(x, y, "Speed", color);
	}

	public void playerCollision(Stage stage)
	{
		Player p = stage.getPlayer();

		if(p.getDirection() == Player.RIGHT)
		{
			if(p.dx+SPEED_RATE <= SPEED_MAX)
			{
				p.dx = p.dx+SPEED_RATE;
			}
		}
		if(p.getDirection() == Player.LEFT)
		{
			if(p.dx-SPEED_RATE >= -SPEED_MAX)
			{
				p.dx = p.dx-SPEED_RATE;
			}



		}

	}
}




