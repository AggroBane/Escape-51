package ca.williamfecteau.enginetest.state;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;


import javax.imageio.ImageIO;

import ca.williamfecteau.enginetest.GameContainer;
import ca.williamfecteau.enginetest.entities.Player;
import ca.williamfecteau.enginetest.menus.Button;
import ca.williamfecteau.enginetest.world.LevelBuilder;
import ca.williamfecteau.enginetest.world.Stage;



public class PlayState implements State
{


	private Stage stage;
	private Player p;
	private int currentStage, pauseCooldown;
	boolean paused;
	private BufferedImage pause, loading;
	private Button backToMenu, backToSelectStage, backToGame;
	private static GameContainer gc;
	

	@SuppressWarnings("static-access")
	@Override
	public void init(GameContainer gc) 
	{
		this.gc = gc;
		backToMenu = new Button(0, 0, 200, 50, "/buttons/retourAuMenu.png", "/buttons/hover/retourAuMenu.png");
		backToSelectStage = new Button(0, 0, 200, 50, "/buttons/changerDeNiveau.png", "/buttons/hover/changerDeNiveau.png");
		backToGame = new Button(0, 0, 200, 50, "/buttons/retourAuJeu.png", "/buttons/hover/retourAuJeu.png");
		paused = false;
		pauseCooldown = 0;
		currentStage = 0;
		try 
		{
			pause = ImageIO.read(getClass().getResource("/background/pause.png"));
			loading = ImageIO.read(getClass().getResource("/background/loading.png"));
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void render(GameContainer gc, Graphics g) 
	{
		stage.render(g);
	
		if(paused)
		{
			int xButton = getXScreen()+((GameContainer.WIDTH/2)-100);
			int yButton = getYScreen();
			backToMenu.updateCoordinate(xButton, yButton+275);
			backToSelectStage.updateCoordinate(xButton, yButton+200);
			backToGame.updateCoordinate(xButton, yButton+125);
			
			g.drawImage(pause, getXScreen(), getYScreen(), GameContainer.WIDTH, GameContainer.HEIGHT, null);
			backToMenu.render(g);
			backToSelectStage.render(g);
			backToGame.render(g);
		}
	}
	

	@Override
	public void update(GameContainer gc) 
	{

		if(!paused)
		{
			
			if(gc.isKeyDown(KeyEvent.VK_UP))
			{
				if(p.dy >= Player.MAX_DY && !p.isJumping())
				{
					if(p.dy < 1)
					{
						p.dy += Player.START_DY;
					}
					else
					{
						p.dy += Player.DY_PER_TICK;
					}
					
					
				}
				else
				{
					p.setJumping(true);
				}

			}
			if(gc.isKeyReleased(KeyEvent.VK_UP))
			{
				p.setJumping(true); 
			}
			
			
			if(gc.isKeyDown(KeyEvent.VK_RIGHT)&& p.getDirection() == 0)
			{
				p.setDirection(Player.RIGHT);
			}
			if(gc.isKeyReleased(KeyEvent.VK_RIGHT) && p.getDirection() == Player.RIGHT)
			{
				p.setDirection(0);
			}
			
			
			if(gc.isKeyDown(KeyEvent.VK_LEFT) && p.getDirection() == 0)
			{
				p.setDirection(Player.LEFT);
			}
			if(gc.isKeyReleased(KeyEvent.VK_LEFT) && p.getDirection() == Player.LEFT)
			{
				p.setDirection(0);
			}
			p.update(stage.getBlockInCameraList());
		}
		//PAUSED
		else
		{
			if(backToMenu.isButtonTriggeredRelativeTo(gc, getXScreen(), getYScreen()))
			{
				GameContainer.menuState.switchState(false);
				gc.switchState(GameContainer.MENU_STATE);
			}
			if(backToSelectStage.isButtonTriggeredRelativeTo(gc, getXScreen(), getYScreen()))
			{
				GameContainer.menuState.switchState(true);
				gc.switchState(GameContainer.MENU_STATE);
			}
			if(backToGame.isButtonTriggeredRelativeTo(gc, getXScreen(), getYScreen()))
			{
				paused = false;
				pauseCooldown = 10;
			}
		}
		
		if(pauseCooldown > 0)
		{
			pauseCooldown--;
		}
		
		if(gc.isKeyDown(KeyEvent.VK_ESCAPE))
		{
			if(!paused && pauseCooldown == 0)
			{
				paused = true;
				pauseCooldown = 10;
			}
			else if(paused && pauseCooldown == 0)
			{
				paused = false;
				pauseCooldown = 10;
				
			}
	
		}

		
		

		
	}
	
	public int getYScreen()
	{
		if(p.getY() < -((Stage.BACKGROUND_HEIGHT-GameContainer.HEIGHT)+(Stage.BACKGROUND_HEIGHT_REPEAT*(Stage.MAX_BACKGROUND_LOAD-2))))
		{
			return -((Stage.BACKGROUND_HEIGHT-GameContainer.HEIGHT)+(Stage.BACKGROUND_HEIGHT_REPEAT*(Stage.MAX_BACKGROUND_LOAD-2)))-GameContainer.HEIGHT/4;
		}
		else if(p.getY() < GameContainer.HEIGHT/4)
		{
			return (int)(p.getY()-(GameContainer.HEIGHT/4));
		}
		else
		{
			return 0;
		}
	}
	
	public int getXScreen()
	{
		if(p.getX() > Stage.BACKGROUND_WIDTH*(Stage.MAX_BACKGROUND_LOAD-2)+GameContainer.WIDTH/2)
		{
			return Stage.BACKGROUND_WIDTH*(Stage.MAX_BACKGROUND_LOAD-2);
		}
		else if(p.getX() > GameContainer.WIDTH/2)
		{
			return (int)p.getX()-(GameContainer.WIDTH/2);
		}
		else
		{
			return 0;
		}
	}
	
	
	public void switchState(int level)
	{
		paused = false;
		GameContainer.instance.getGraphics().drawImage(loading, 0, 0, GameContainer.WIDTH, GameContainer.HEIGHT, null);
		loadStage(level);
		
		
	}
	
	
	private void loadStage(int level)
	{
		if(level != LevelBuilder.getStageList().length)
		{
			try {
				LevelBuilder.loadStageBlockList(level);
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
			currentStage = level;
			this.stage = LevelBuilder.getStage(level);
			p = this.stage.getPlayer();
			this.stage.respawnPlayer();

			stage.setLocked(false);
			LevelBuilder.unlockLevel(level);
			
			if(level == LevelBuilder.getStageList().length-1 && !MenuState.gameEnded)
			{
				MenuState.triggerEnd();
				GameContainer.menuState.switchState(true);
				gc.switchState(GameContainer.MENU_STATE);
			}
		}


	}
	
	public static void kaizoBeated()
	{
		GameContainer.menuState.switchState(false);
		gc.switchState(GameContainer.MENU_STATE);
	}
	



	public int getCurrentStage()
	{
		return currentStage;
	}



}
