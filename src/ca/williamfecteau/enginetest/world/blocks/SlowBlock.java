package ca.williamfecteau.enginetest.world.blocks;

import ca.williamfecteau.enginetest.entities.Player;
import ca.williamfecteau.enginetest.world.Stage;

public class SlowBlock extends Block
{
	public static final float SLOW_RATE = 0.5F;
	public static final float SLOW_MAX = 3;
	
	//CHECK Y
	

	public SlowBlock(int x, int y, int color) 
	{
		super(x, y, "Slow", color);
	}

	public void playerCollision(Stage stage)
	{
		Player p = stage.getPlayer();
		if(p.getDirection() == Player.RIGHT)
		{
			if(p.dx > SLOW_MAX)
			{
				stage.getPlayer().dx = p.dx-SLOW_RATE;
			}
			
		}
		if(p.getDirection() == Player.LEFT)
		{
			if(p.dx < -SLOW_MAX)
			{
				stage.getPlayer().dx = p.dx+SLOW_RATE;
			}
		}

		
	}

}




