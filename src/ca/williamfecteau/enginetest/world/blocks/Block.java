package ca.williamfecteau.enginetest.world.blocks;

import java.awt.Graphics;
import java.awt.Rectangle;

import ca.williamfecteau.enginetest.world.LevelBuilder;
import ca.williamfecteau.enginetest.world.Stage;


public abstract class Block 
{
	protected float x,y;
	protected int color;
	protected Rectangle bounds;
	protected String type;
	

	public Block(int x, int y, String type, int color) 
	{
		this.type = type;
		this.color = color;
		
		this.x = x*32;
		this.y = y*32;
		bounds = new Rectangle((int)this.x,(int)this.y,32,32);
	}
	

	public void endStage(Stage stage)
	{
		
	}

	
	public String getType() 
	{
		return type;
	}




	public void render(Graphics g)
	{
		g.drawImage(LevelBuilder.getGoodTexture(type), (int)x, (int)y, 32, 32, null);
		if(color != 0)
		{
			g.drawImage(LevelBuilder.getGoodColor(color), (int)x, (int)y, 32, 32, null);
		}

	}
	
	
	public void playerCollision(Stage stage)
	{
		
	}
	

	
	
	public double getX() 
	{
		return x;
	}

	public double getY() 
	{
		return y;
	}
	
	
	public void setY(int y) 
	{
		this.y = y*32;
	}

	public void setX(int x) 
	{
		this.x = x*32;
	}
	
	public Rectangle getBounds()
	{
		return bounds;
	}


	public int getColor() {
		return color;
	}


	public void setColor(int color) {
		this.color = color;
	}

}
