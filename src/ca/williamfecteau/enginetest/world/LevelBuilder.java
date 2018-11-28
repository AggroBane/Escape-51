package ca.williamfecteau.enginetest.world;


import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import ca.williamfecteau.enginetest.GameContainer;
import ca.williamfecteau.enginetest.state.MenuState;
import ca.williamfecteau.enginetest.world.blocks.Block;
import ca.williamfecteau.enginetest.world.blocks.BrickBlock;
import ca.williamfecteau.enginetest.world.blocks.CheckpointBlock;
import ca.williamfecteau.enginetest.world.blocks.EndBlock;
import ca.williamfecteau.enginetest.world.blocks.SlowBlock;
import ca.williamfecteau.enginetest.world.blocks.SpeedBlock;
import ca.williamfecteau.enginetest.world.blocks.SpikeBlock;



public class LevelBuilder 
{
	public static BufferedImage brick, end, spike, slow, speed, checkpoint, brickHover, endHover, spikeHover, slowHover, speedHover, checkpointHover
	, emptyPot, emptyPotHover, red, redPot, redPotHover, bluePot, bluePotHover, blue, greenPot, greenPotHover, green, yellowPot
	, yellowPotHover, yellow, blackPot, blackPotHover, black, whitePot, whitePotHover, white; 
	public static Stage[] stageList = new Stage[9];
	public static String resPath;
	
	
	public static final boolean isJar = false;
	public static final String jarName = "Escape51.jar";
	
	
	
	public LevelBuilder() throws IOException
	{
		//BLOCKS
		brick = ImageIO.read(getClass().getResource("/blocks/brick.png"));
		spike = ImageIO.read(getClass().getResource("/blocks/spike.png"));
		slow = ImageIO.read(getClass().getResource("/blocks/slow.png"));
		speed = ImageIO.read(getClass().getResource("/blocks/speed.png"));
		end = ImageIO.read(getClass().getResource("/blocks/end.png"));
		checkpoint = ImageIO.read(getClass().getResource("/blocks/checkpoint.png"));
		
		//HOVER
		brickHover = ImageIO.read(getClass().getResource("/blocks/hover/brick.png"));
		spikeHover = ImageIO.read(getClass().getResource("/blocks/hover/spike.png"));
		slowHover = ImageIO.read(getClass().getResource("/blocks/hover/slow.png"));
		speedHover = ImageIO.read(getClass().getResource("/blocks/hover/speed.png"));
		endHover = ImageIO.read(getClass().getResource("/blocks/hover/end.png"));
		checkpointHover = ImageIO.read(getClass().getResource("/blocks/hover/checkpoint.png"));
		
		//COLOR
		red = ImageIO.read(getClass().getResource("/blocks/color/red.png"));
		blue = ImageIO.read(getClass().getResource("/blocks/color/blue.png"));
		green = ImageIO.read(getClass().getResource("/blocks/color/green.png"));
		yellow = ImageIO.read(getClass().getResource("/blocks/color/yellow.png"));
		white = ImageIO.read(getClass().getResource("/blocks/color/white.png"));
		black = ImageIO.read(getClass().getResource("/blocks/color/black.png"));
		
		
		//COLOR POT
		
		emptyPot = ImageIO.read(getClass().getResource("/blocks/color/emptyPot.png"));
		redPot = ImageIO.read(getClass().getResource("/blocks/color/redPot.png"));
		bluePot = ImageIO.read(getClass().getResource("/blocks/color/bluePot.png"));
		greenPot = ImageIO.read(getClass().getResource("/blocks/color/greenPot.png"));
		yellowPot = ImageIO.read(getClass().getResource("/blocks/color/yellowPot.png"));
		whitePot = ImageIO.read(getClass().getResource("/blocks/color/whitePot.png"));
		blackPot = ImageIO.read(getClass().getResource("/blocks/color/blackPot.png"));
		
		//COLOR POT HOVER
		emptyPotHover = ImageIO.read(getClass().getResource("/blocks/color/emptyPotHover.png"));
		redPotHover = ImageIO.read(getClass().getResource("/blocks/color/redPotHover.png"));
		bluePotHover = ImageIO.read(getClass().getResource("/blocks/color/bluePotHover.png"));
		greenPotHover = ImageIO.read(getClass().getResource("/blocks/color/greenPotHover.png"));
		yellowPotHover = ImageIO.read(getClass().getResource("/blocks/color/yellowPotHover.png"));
		blackPotHover = ImageIO.read(getClass().getResource("/blocks/color/blackPotHover.png"));
		whitePotHover = ImageIO.read(getClass().getResource("/blocks/color/whitePotHover.png"));
		
		String path = null;
		try {
			path = GameContainer.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		String[] parts = path.split("/"+jarName);
		resPath = parts[0]+"/res";
		
		BufferedReader buffR = getBufferedReader(resPath+"/levels/StageInitializer.txt", "res/levels/StageInitializer.txt");
		
		
		String line = buffR.readLine();
		int stageCount = 0;
		while(line != null)
		{
			String[] stageInfo = line.split(",");
			int x = Integer.parseInt(stageInfo[0]);
			int y = Integer.parseInt(stageInfo[1]);
			int bool = Integer.parseInt(stageInfo[2]);
			boolean locked = true;

			if(bool == 1)
			{
				locked = true;
			}
			if(bool == 0)
			{
				locked = false;
			}
			
			stageList[stageCount] = new Stage(x, y, locked, stageCount);
			stageCount++;
			line = buffR.readLine();
		}
		
		buffR.close();
	
		if(!stageList[stageList.length-1].isLocked())
		{
			MenuState.triggerEnd();
		}
	}
	
	
	
	public static BufferedImage getGoodTexture(String type)
	{
		if(type == "Brick") return brick;
		else if(type == "Speed") return speed;
		else if(type == "Spike") return spike;
		else if(type == "Slow") return slow;
		else if(type == "End") return end;
		else if(type == "Checkpoint") return checkpoint;
		else return null;

	}
	
	public static BufferedImage getGoodHoverTexture(String type)
	{
		if(type == "Brick") return brickHover;
		else if(type == "Speed") return speedHover;
		else if(type == "Spike") return spikeHover;
		else if(type == "Slow") return slowHover;
		else if(type == "End") return endHover;
		else if(type == "Checkpoint") return checkpointHover;
		else return null;

	}
	
	
	
	public boolean isBlockExisting(ArrayList<Block> blockList, Block b)
	{
		for(Block lb : blockList)
		{
			if(lb.getX() == b.getX() && lb.getY() == b.getY()) return true;
		}
		return false;
	}
	
	
	public static void loadStageBlockList(int stageID) throws IOException 
	{

		for(Stage s : stageList)
		{
			s.unloadBlockList();
		}
		BufferedReader buffR = getBufferedReader(resPath+"/levels/level"+stageID+".txt", "res/levels/level"+stageID+".txt");


		
		String line = buffR.readLine();
		ArrayList<Block> stageBlockList = new ArrayList<Block>();
		
		while(line != null)
		{
			String[] blockInfo = line.split(",");
			int x = Integer.parseInt(blockInfo[1]);
			int y = Integer.parseInt(blockInfo[2]);
			int color = Integer.parseInt(blockInfo[3]);
			
			Block blockToAdd = null;
			
	    	if(blockInfo[0].equals("Brick"))
	    	{
	    		blockToAdd = new BrickBlock(x, y, color);
	    	}
	    	if(blockInfo[0].equals("Spike"))
	    	{
	    		blockToAdd = new SpikeBlock(x, y, color, stageID);
	    	}
	    	if(blockInfo[0].equals("End"))
	    	{
	    		blockToAdd = new EndBlock(x, y, color);
	    	}
	    	if(blockInfo[0].equals("Slow"))
	    	{
	    		blockToAdd = new SlowBlock(x, y, color);
	    	}
	    	if(blockInfo[0].equals("Speed"))
	    	{
	    		blockToAdd = new SpeedBlock(x, y, color);
	    	}
	    	if(blockInfo[0].equals("Checkpoint"))
	    	{
	    		blockToAdd = new CheckpointBlock(x, y, color);
	    	}
	    	stageBlockList.add(blockToAdd);
	    	line = buffR.readLine();
		}

		
		
		
		buffR.close();
		
		stageList[stageID].loadBlockList(stageBlockList);
	}
	
	
	private static BufferedReader getBufferedReader(String outPath, String eclipsePath)
	{
		//OUVERTURE
		FileReader fileR = null;
		BufferedReader buffR = null;
		try 
		{

			File f = null;
			//VERSION .JAR
			if(isJar)
			{
				
	
	
					f = new File(outPath);
	
			}
			//VERSION ECLIPSE
			else
			{
				f = new File(eclipsePath);
			}

		
			fileR = new FileReader(f);
			buffR = new BufferedReader(fileR);
		} catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		return buffR;
	}
	
	public static boolean isStageLocked(int level)
	{
		return stageList[level].isLocked();
	}
	
	
	public static Stage getStage(int level)
	{
		return stageList[level];
	}
	
	
	public static Stage[] getStageList()
	{
		return stageList;
	}



	public static void unlockLevel(int level) 
	{
    	File temp = null;
    	File actual = null;

    	if(isJar)
    	{
			temp = new File(resPath+"/levels/temp.txt");
			actual = new File(resPath+"/levels/StageInitializer.txt");
			try {
				temp.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	else
    	{
        	temp = new File("res/levels/temp.txt");
        	actual = new File("res/levels/StageInitializer.txt");
    	}

		FileWriter fwTemp = null;
		BufferedWriter bwTemp = null;
		try 
		{
			fwTemp = new FileWriter(temp);
			bwTemp = new BufferedWriter(fwTemp);
		} catch (IOException e1) 
		{
			e1.printStackTrace();
		}
		
		
		
		
		for(int i = 0; i < stageList.length; i++)
		{
			if(level == i)
			{
				write(stageList[i].getxSpawn()+","+stageList[i].getySpawn()+","+"0", bwTemp);
			}
			else
			{
				if(stageList[i].isLocked() == true)
				{
					write(stageList[i].getxSpawn()+","+stageList[i].getySpawn()+","+"1", bwTemp);
				}
				else
				{
					write(stageList[i].getxSpawn()+","+stageList[i].getySpawn()+","+"0", bwTemp);
				}
				
			}
			
		}
		
		try 
		{
			bwTemp.close();
		} catch (IOException e)
		{

			e.printStackTrace();
		}

		actual.delete();
		temp.renameTo(actual);

		
		
	}
	
	private static void write(String message, BufferedWriter fw)
	{
		try 
		{
			fw.write(message);
			fw.newLine();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}



	public static BufferedImage getGoodColor(int color) 
	{
		if(color == 1) return red;
		else if(color == 2) return blue;
		else if(color == 3) return green;
		else if(color == 4) return yellow;
		else if(color == 5) return white;
		else if(color == 6) return black;
		else return null;

	}



	public static BufferedImage getGoodColorPot(int color) 
	{
		if(color == 0) return emptyPot;
		else if(color == 1) return redPot;
		else if(color == 2) return bluePot;
		else if(color == 3) return greenPot;
		else if(color == 4) return yellowPot;
		else if(color == 5) return whitePot;
		else if(color == 6) return blackPot;
		else return null;
	}



	public static BufferedImage getGoodColorPotHover(int color) 
	{
		if(color == 0) return emptyPotHover;
		else if(color == 1) return redPotHover;
		else if(color == 2) return bluePotHover;
		else if(color == 3) return greenPotHover;
		else if(color == 4) return yellowPotHover;
		else if(color == 5) return whitePotHover;
		else if(color == 6) return blackPotHover;
		else return null;
	}
	

	
	
	
	
	

}
