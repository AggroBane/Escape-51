package ca.williamfecteau.enginetest.world;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import ca.williamfecteau.enginetest.GameContainer;
import ca.williamfecteau.enginetest.entities.Player;
import ca.williamfecteau.enginetest.world.blocks.Block;

public class Stage 
{
	public static final int MAX_BLOCK = 10000;
	public static final int MAX_BACKGROUND_LOAD = 8;
	public static final int MAX_TOP_LOAD = 20;
	
	public static int BACKGROUND_WIDTH = 0;
	public static int BACKGROUND_HEIGHT = 0;
	public static int BACKGROUND_HEIGHT_REPEAT = 0;

	private BufferedImage background, topRepeat;
	private ArrayList<Block> blockList, blockInCamera;
	private Player player;
	private boolean locked;

	public int ID;
	private int xSpawn, xCheck, ySpawn, yCheck;
	

	public Stage(int xSpawn, int ySpawn, boolean locked, int ID)
	{
		this.locked = locked;

		this.ID = ID;
		blockList = new ArrayList<Block>();
		blockInCamera = new ArrayList<Block>();
		try 
		{
			background = ImageIO.read(getClass().getResource("/background/game.jpg"));
			topRepeat = ImageIO.read(getClass().getResource("/background/gameTopRepeat.png"));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		BACKGROUND_WIDTH = background.getWidth();
		
		BACKGROUND_HEIGHT = background.getHeight();
		BACKGROUND_HEIGHT_REPEAT = topRepeat.getHeight();
		
		this.xSpawn = xSpawn;
		this.ySpawn = ySpawn;
		
		xCheck = xSpawn;
		yCheck = ySpawn;
				
				
		player = new Player(this);
		respawnPlayer();
	}



	public int getID()
	{
		return ID;
	}

	public void render(Graphics g)
	{
		renderWithoutPlayer(g);
	}



	public int getxSpawn() {
		return xSpawn;
	}





	public int getySpawn() {
		return ySpawn;
	}





	//WITHOUT TIMER
	public void renderWithoutPlayer(Graphics g)
	{
		g.translate(-getXScreen(), -getYScreen());
		

		//background qui suit
		int x1 = 0, x2= 0;
		int yNormal = -(background.getHeight()-GameContainer.HEIGHT);
		
		for(int i = 0; i < MAX_BACKGROUND_LOAD; i++)
		{
			if(getXScreen()+background.getWidth() > i*background.getWidth() && getXScreen() <= i*background.getWidth())
			{
				x1 = i*background.getWidth();
				x2 = i*background.getWidth()-background.getWidth();
				g.drawImage(background, x1, yNormal, background.getWidth(), background.getHeight(), null);
				g.drawImage(background, x2, yNormal, background.getWidth(), background.getHeight(), null);
				break;
			}
		}
		
		for(int i = 0; i < MAX_TOP_LOAD; i++)
		{
			if(getYScreen() < (i*topRepeat.getHeight())+yNormal)
			{
				g.drawImage(topRepeat, x1, ((-i*topRepeat.getHeight())+yNormal)-topRepeat.getHeight(), topRepeat.getWidth(), topRepeat.getHeight(), null);
				g.drawImage(topRepeat, x2, ((-i*topRepeat.getHeight())+yNormal)-topRepeat.getHeight(), topRepeat.getWidth(), topRepeat.getHeight(), null);
			}
		}




		
		Rectangle cameraBounds = new Rectangle(getXScreen(), getYScreen(), GameContainer.WIDTH, GameContainer.HEIGHT);
		blockInCamera.clear();
		for(Block b : blockList)
		{
			if(cameraBounds.intersects(b.getBounds()))
			{
				blockInCamera.add(b);
				b.render(g);
			}
		}
		player.render(g);
		g.setColor(Color.WHITE);
	}

	
	
	public void unloadBlockList()
	{
		xSpawn = xCheck;
		ySpawn = yCheck;
		blockList.clear();
	}

	
	public void loadBlockList(ArrayList<Block> newBlockList)
	{
		blockList = newBlockList;
		blockList.trimToSize();
	}




	private int getYScreen()
	{

		if(player.getY() < -((background.getHeight()-GameContainer.HEIGHT)+(topRepeat.getHeight()*(MAX_BACKGROUND_LOAD-2))))
		{
			return -((background.getHeight()-GameContainer.HEIGHT)+(topRepeat.getHeight()*(MAX_BACKGROUND_LOAD-2)))-GameContainer.HEIGHT/4;
		}
		else if(player.getY() < GameContainer.HEIGHT/4)
		{
			return (int)(player.getY()-(GameContainer.HEIGHT/4));
		}
		else if(player.getY() > 335 && GameContainer.menuState.isLevelEditor())
		{
			return 48;
		}
		else
		{
			return 0;
		}
	}

	private int getXScreen()
	{
		if(player.getX() > background.getWidth()*(MAX_BACKGROUND_LOAD-2)+GameContainer.WIDTH/2)
		{
			return background.getWidth()*(MAX_BACKGROUND_LOAD-2);
		}
		if(player.getX() > GameContainer.WIDTH/2)
		{
			return (int)player.getX()-(GameContainer.WIDTH/2);
		}
		else
		{
			return 0;
		}
	}




	public void setLocked(boolean locked) {
		this.locked = locked;
	}


	public boolean isLocked() {
		return locked;
	}


	public void respawnPlayer()
	{
		player.respawn(xSpawn, ySpawn);
	}



	public Player getPlayer() {
		return player;
	}


	public ArrayList<Block> getBlockList() 
	{
		return blockList;
	}

	public ArrayList<Block> getBlockInCameraList() 
	{
		return blockInCamera;
	}

	public void addBlock(Block b)
	{
		if(blockList.size() < MAX_BLOCK)
		{
			blockList.add(b);
		}
	}

	public Block getBlockAt(int x, int y)
	{
		for(Block b : blockList)
		{
			if((int)b.getX() == x && (int)b.getY() == y)
			{
				return b;
			}
		}
		return null;
	}
	
	public boolean isBlockAt(int x, int y)
	{
		for(Block b : blockList)
		{
			if((int)b.getX() == x && (int)b.getY() == y)
			{
				return true;
			}
		}
		return false;
	}



	public void removeBlockAt(int x, int y)
	{
		for(Block b : blockList)
		{
			if((int)b.getX() == x*32 && (int)b.getY() == y*32)
			{
				blockList.remove(b);
				break;
			}
		}
	}





	public void setxSpawn(int xSpawn) {
		this.xSpawn = xSpawn;
	}





	public void setySpawn(int ySpawn) {
		this.ySpawn = ySpawn;
	}





}
