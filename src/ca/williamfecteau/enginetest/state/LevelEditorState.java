package ca.williamfecteau.enginetest.state;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;

import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;


import javax.imageio.ImageIO;


import ca.williamfecteau.enginetest.GameContainer;
import ca.williamfecteau.enginetest.entities.Player;
import ca.williamfecteau.enginetest.menus.Button;
import ca.williamfecteau.enginetest.world.LevelBuilder;
import ca.williamfecteau.enginetest.world.Stage;
import ca.williamfecteau.enginetest.world.blocks.Block;
import ca.williamfecteau.enginetest.world.blocks.BrickBlock;
import ca.williamfecteau.enginetest.world.blocks.CheckpointBlock;
import ca.williamfecteau.enginetest.world.blocks.EndBlock;
import ca.williamfecteau.enginetest.world.blocks.SlowBlock;
import ca.williamfecteau.enginetest.world.blocks.SpeedBlock;
import ca.williamfecteau.enginetest.world.blocks.SpikeBlock;


public class LevelEditorState implements State 
{
	int modifiedStage, cooldownTimer, godCooldown, activeColor = 0;
	Stage stage;
	Player player;
	boolean godMode, saved, optionOpen;
	Button optionButton, backToMenu, backToSelectStage, backToGame, saveButton;
	BufferedImage optionBackground, loading;
	Rectangle blockSelection;
	String currentBlockType;
	Block b;

	//ARRAY
	Button[] buttonArray;
	Button[] colorArray;
	BufferedImage[] hoverArray;
	BufferedImage[] colorHoverArray;
	String[] blockTypeArray;
	Block[] blockArray;

	@Override
	public void init(GameContainer gc) 
	{

		//BLOCK
		currentBlockType = "none";
		blockSelection = new Rectangle();
		

		int numberOfColor = 7;
		int numberOfBlock = 6;
		blockArray = new Block[numberOfBlock];
		blockArray[0] = new BrickBlock(0, 0, 0);
		blockArray[1] = new SpikeBlock(0, 0, 0, 0);
		blockArray[2] = new EndBlock(0, 0, 0);
		blockArray[3] = new SlowBlock(0, 0, 0);
		blockArray[4] = new SpeedBlock(0, 0, 0);
		blockArray[5] = new CheckpointBlock(0, 0, 0);
		
		
		buttonArray = new Button[numberOfBlock];
		blockTypeArray = new String[numberOfBlock];
		


		for(int i = 0; i < buttonArray.length; i++)
		{
			buttonArray[i] = new Button(0, GameContainer.HEIGHT-40, 32, 32, LevelBuilder.getGoodTexture(blockArray[i].getType()), LevelBuilder.getGoodHoverTexture(blockArray[i].getType()));
			

			blockTypeArray[i] = blockArray[i].getType();
		}
		
		colorArray = new Button[numberOfColor];
		for(int i = 0; i < colorArray.length; i++)
		{
			colorArray[i] = new Button(0, GameContainer.HEIGHT-40, 32, 32, LevelBuilder.getGoodColorPot(i), LevelBuilder.getGoodColorPotHover(i));
		}


		//OPTIONS
		optionButton = new Button("Options", 0, 0, 100, 50, Color.BLUE, Color.RED);
		backToMenu = new Button(0, 0, 200, 50, "/buttons/retourAuMenu.png", "/buttons/hover/retourAuMenu.png");
		backToSelectStage = new Button(0, 0, 200, 50, "/buttons/changerDeNiveau.png", "/buttons/hover/changerDeNiveau.png");
		backToGame = new Button(0, 0, 200, 50, "/buttons/retourAuJeu.png", "/buttons/hover/retourAuJeu.png");
		saveButton = new Button(0, 0, 200, 50, "/buttons/sauvegarder.png", "/buttons/hover/sauvegarder.png");
		optionOpen = false;
		
		try 
		{
			optionBackground = ImageIO.read(getClass().getResource("/background/options.png"));
			loading = ImageIO.read(getClass().getResource("/background/loading.png"));
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		modifiedStage = 0;
		godMode = true;
		saved = false;
		cooldownTimer = 10;
		godCooldown = 10;
	}

	@Override
	public void render(GameContainer gc, Graphics g) 
	{
		stage.renderWithoutPlayer(g);
		g.setColor(Color.GRAY);

		
		
		
		int buttonHeight = getYScreen()+(GameContainer.HEIGHT-40);
		int selectButtonX = getXScreen();
		g.fillRect(getXScreen(), getYScreen()+(GameContainer.HEIGHT-48), GameContainer.WIDTH, 48);
		optionButton.updateCoordinate(getXScreen()+(GameContainer.WIDTH-100), getYScreen()+(GameContainer.HEIGHT-48));
		for(int i = 0; i < buttonArray.length; i++)
		{
			buttonArray[i].updateCoordinate(selectButtonX+(i*48), buttonHeight);
			if(currentBlockType == blockTypeArray[i])
			{
				
				buttonArray[i].setImage(LevelBuilder.getGoodHoverTexture(blockTypeArray[i]));
			}
			else
			{
				buttonArray[i].setImage(LevelBuilder.getGoodTexture(blockTypeArray[i]));
			}
			buttonArray[i].render(g);
			
		}


		for(int i = 0; i < colorArray.length; i++)
		{
			colorArray[i].updateCoordinate(selectButtonX+(i*48)+300, buttonHeight);
			if(activeColor == i)
			{
				colorArray[i].setImage(LevelBuilder.getGoodColorPotHover(i));
			}
			else
			{
				colorArray[i].setImage(LevelBuilder.getGoodColorPot(i));
			}
			colorArray[i].render(g);
			
			
		}
		
		
		optionButton.render(g);
		


		//OPTION OPEN
		if(optionOpen)
		{
			int buttonX = getXScreen()+(GameContainer.WIDTH/2)-100;
			int buttonY = getYScreen();
			g.drawImage(optionBackground, getXScreen(), buttonY, GameContainer.WIDTH, GameContainer.HEIGHT, null);
			backToMenu.updateCoordinate(buttonX, buttonY+250);
			backToSelectStage.updateCoordinate(buttonX, buttonY+175);
			backToGame.updateCoordinate(buttonX, buttonY+100);
			saveButton.updateCoordinate(buttonX, buttonY+325);
			
			backToMenu.render(g);
			backToSelectStage.render(g);
			backToGame.render(g);
			saveButton.render(g);
		}

	}

	
	
	@Override
	public void update(GameContainer gc) 
	{
		if(!optionOpen)
		{
			//BUTTONS
			if(optionButton.isButtonTriggered(gc))
			{
				optionOpen = true;
			}
			for(int i = 0; i < buttonArray.length; i++)
			{
				if(buttonArray[i].isButtonTriggeredRelativeTo(gc, getXScreen(), getYScreen()))
				{
					currentBlockType = blockTypeArray[i];
				}

			}
			
			
			for(int i = 0; i < colorArray.length; i++)
			{
				if(colorArray[i].isButtonTriggeredRelativeTo(gc, getXScreen(), getYScreen()))
				{
					activeColor = i;
				}

			}

			
			//PLACER BLOCS
			if(gc.isMouseLeftClick())
			{
				double mouseX, mouseY;
				try
				{
					mouseX = gc.getMousePosition().getX();
					mouseY = gc.getMousePosition().getY();
				} catch(NullPointerException e)
				{
					mouseX = -1;
					mouseY = -1;
				}
				if(mouseX != -1)
				{
					int x = (int)(((getXScreen()+mouseX)/32));
					int y = (int)(((getYScreen()+mouseY)/32));
					if(getYScreen() < -200)
					{
						y = (int)(((getYScreen()+mouseY)/32)-0.9);
					}


					stage.removeBlockAt(x, y);
					
					if(currentBlockType == "Brick")
					{
						stage.addBlock(new BrickBlock(x, y, activeColor));
					}
					if(currentBlockType == "Spike")
					{
						stage.addBlock(new SpikeBlock(x, y, activeColor, stage.getID()));
					}
					if(currentBlockType == "End")
					{
						stage.addBlock(new EndBlock(x, y, activeColor));
					}
					if(currentBlockType == "Slow")
					{
						stage.addBlock(new SlowBlock(x, y, activeColor));
					}
					if(currentBlockType == "Speed")
					{
						stage.addBlock(new SpeedBlock(x, y, activeColor));
					}
					if(currentBlockType == "Checkpoint")
					{
						stage.addBlock(new CheckpointBlock(x, y, activeColor));
					}
				}
			}
			if(gc.isMouseRightClick())
			{
				double mouseX, mouseY;
				try
				{
					mouseX = gc.getMousePosition().getX();
					mouseY = gc.getMousePosition().getY();
				} catch(NullPointerException e)
				{
					mouseX = -1;
					mouseY = -1;
				}

				if(mouseX != -1)
				{
					int x = (int)(((getXScreen()+mouseX)/32));
					int y = (int)(((getYScreen()+mouseY)/32));
					if(getYScreen() < -200)
					{
						y = (int)(((getYScreen()+mouseY)/32)-0.9);
					}

	

					stage.removeBlockAt(x, y);
					

				}
			}



			if(gc.isKeyDown(KeyEvent.VK_SPACE))
			{
				if(godCooldown == 0)
				{
					godMode = !godMode;
					godCooldown = 10;
				}
				
			}
			if(godMode) //GOD PHYSICS
			{
				if(gc.isKeyDown(KeyEvent.VK_RIGHT))
				{
					player.setX(player.getX()+10);
				}
				if(gc.isKeyDown(KeyEvent.VK_LEFT))
				{
					player.setX(player.getX()-10);
				}
				if(gc.isKeyDown(KeyEvent.VK_UP))
				{
					player.setY(player.getY()-10);
				}
				if(gc.isKeyDown(KeyEvent.VK_DOWN))
				{
					player.setY(player.getY()+10);
				}
			}
			else //REAL PHYSICS
			{
				if(gc.isKeyDown(KeyEvent.VK_UP))
				{
					if(player.dy >= Player.MAX_DY && !player.isJumping())
					{
						if(player.dy < 1)
						{
							player.dy += Player.START_DY;
						}
						else
						{
							player.dy += Player.DY_PER_TICK;
						}
					}
					else
					{
						player.setJumping(true);
					}

				}
				if(gc.isKeyReleased(KeyEvent.VK_UP))
				{
					player.setJumping(true); 
				}


				if(gc.isKeyDown(KeyEvent.VK_RIGHT) && player.getDirection() == 0)
				{
					player.setDirection(Player.RIGHT);
				}
				if(gc.isKeyReleased(KeyEvent.VK_RIGHT) && player.getDirection() == Player.RIGHT)
				{
					player.setDirection(0);
				}


				if(gc.isKeyDown(KeyEvent.VK_LEFT) && player.getDirection() == 0)
				{
					player.setDirection(Player.LEFT);
				}
				if(gc.isKeyReleased(KeyEvent.VK_LEFT) && player.getDirection() == Player.LEFT)
				{
					player.setDirection(0);
				}
				player.update(stage.getBlockInCameraList());
			}

		}
		//OPTION OPEN
		else
		{
			if(backToMenu.isButtonTriggeredRelativeTo(gc, getXScreen(), getYScreen()))
			{
				optionOpen = false;
				GameContainer.menuState.setLevelEditor(false);
				godMode = true;
				stage.respawnPlayer();
				GameContainer.menuState.switchState(false);
				gc.switchState(GameContainer.MENU_STATE);
			}
			if(backToSelectStage.isButtonTriggeredRelativeTo(gc, getXScreen(), getYScreen()))
			{
				optionOpen = false;
				godMode = true;
				stage.respawnPlayer();
				GameContainer.menuState.switchState(true);
				gc.switchState(GameContainer.MENU_STATE);
				
			}
			if(backToGame.isButtonTriggeredRelativeTo(gc, getXScreen(), getYScreen()))
			{
				optionOpen = false;
			}
			if(saveButton.isButtonTriggeredRelativeTo(gc, getXScreen(), getYScreen()))
			{
				save();
			}
		}
		

		
		//OUVRIR OPTIONS
		if(optionButton.isButtonTriggeredRelativeTo(gc, getXScreen(), getYScreen()))
		{
			optionOpen = true;
		}
		
		if(cooldownTimer != 0)
		{
			cooldownTimer--;
		}
		
		if(godCooldown != 0)
		{
			godCooldown--;
		}
		
		if(gc.isKeyDown(KeyEvent.VK_ESCAPE))
		{
			if(!optionOpen && cooldownTimer == 0)
			{
				optionOpen = true;
				cooldownTimer = 10;
			}
			else if(optionOpen && cooldownTimer == 0)
			{
				optionOpen = false;
				cooldownTimer = 10;
			}
		}



	}

	
	public int getYScreen()
	{
		if(player.getY() < -((Stage.BACKGROUND_HEIGHT-GameContainer.HEIGHT)+(Stage.BACKGROUND_HEIGHT_REPEAT*(Stage.MAX_BACKGROUND_LOAD-2))))
		{
			return -((Stage.BACKGROUND_HEIGHT-GameContainer.HEIGHT)+(Stage.BACKGROUND_HEIGHT_REPEAT*(Stage.MAX_BACKGROUND_LOAD-2)))-GameContainer.HEIGHT/4;
		}
		else if(player.getY() < GameContainer.HEIGHT/4)
		{
			return (int)(player.getY()-(GameContainer.HEIGHT/4));
		}
		else if(player.getY() > 335)
		{
			return 48;
		}
		else
		{
			return 0;
		}
	}
	
	public int getXScreen()
	{
		if(player.getX() > Stage.BACKGROUND_WIDTH*(Stage.MAX_BACKGROUND_LOAD-2)+GameContainer.WIDTH/2)
		{
			return Stage.BACKGROUND_WIDTH*(Stage.MAX_BACKGROUND_LOAD-2);
		}
		else if(player.getX() > GameContainer.WIDTH/2)
		{
			return (int)player.getX()-(GameContainer.WIDTH/2);
		}
		else
		{
			return 0;
		}
	}

	private synchronized void save()
	{
    	File temp = null;
    	File actual = null;

    	if(LevelBuilder.isJar)
    	{
			temp = new File(LevelBuilder.resPath+"/levels/temp.txt");
			actual = new File(LevelBuilder.resPath+"/levels/level"+modifiedStage+".txt");
			try {
				temp.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	else
    	{
        	temp = new File("res/levels/temp.txt");
        	actual = new File("res/levels/level"+modifiedStage+".txt");
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




		ArrayList<Block> blockArray = stage.getBlockList();
		ArrayList<Block> addedBlock = new ArrayList<Block>();
		for(Block b : blockArray)
		{
			if(!addedBlock.contains(b))
			{
				addedBlock.add(b);
				write(b.getType()+","+(int)(b.getX()/32)+","+(int)(b.getY()/32)+","+b.getColor(), bwTemp);
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



	private void write(String message, BufferedWriter fw)
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


	public void setModifiedStage(int n)
	{
		GameContainer.instance.getGraphics().drawImage(loading, 0, 0, GameContainer.WIDTH, GameContainer.HEIGHT, null);
		modifiedStage = n;
		try {
			LevelBuilder.loadStageBlockList(n);
		} catch (IOException e) {
			e.printStackTrace();
		}
		stage = LevelBuilder.getStage(n);
		player = stage.getPlayer();
		stage.respawnPlayer();
	}
}
