package ca.williamfecteau.enginetest.world.blocks;

import java.awt.Graphics;

import ca.williamfecteau.enginetest.world.LevelBuilder;
import ca.williamfecteau.enginetest.world.Stage;

public class SpikeBlock extends Block
{

	private int rotation = -1;
	private int stageID = 0;
	
	public void render(Graphics g)
	{
		if(rotation == -1)
		{
			if(LevelBuilder.getStage(stageID).isBlockAt((int)x, (int)y+32))
			{
				rotation = 0;
			}
			else if(LevelBuilder.getStage(stageID).isBlockAt((int)x, (int)y-32))
			{
				rotation = 1;
			}
			else
			{
				rotation = 0;
			}
		}
		
		if(rotation == 0)
		{
			g.drawImage(LevelBuilder.getGoodTexture(type), (int)x, (int)y, 32, 32, null);
		}
		else if(rotation == 1)
		{
			g.drawImage(LevelBuilder.getGoodTexture(type), (int)x, (int)y+32, 32, -32, null);
		}
		
		
		if(color != 0)
		{
			g.drawImage(LevelBuilder.getGoodColor(color), (int)x, (int)y, 32, 32, null);
		}
	}
	
	
	public SpikeBlock(int x, int y, int color, int stageID) 
	{
		super(x, y, "Spike", color);
		this.stageID = stageID;

	}
	
	public void playerCollision(Stage stage)
	{
		stage.respawnPlayer();
	}
	
}
