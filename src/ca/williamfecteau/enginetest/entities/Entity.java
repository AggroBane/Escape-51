package ca.williamfecteau.enginetest.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import ca.williamfecteau.enginetest.world.blocks.Block;



public interface Entity 
{
	public void render(Graphics g);
	public void update(ArrayList<Block> blockList);
	
	public double getX();
	public double getY();
	public Rectangle getBounds();

}
