package ca.williamfecteau.enginetest.state;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import ca.williamfecteau.enginetest.GameContainer;
import ca.williamfecteau.enginetest.menus.Button;
import ca.williamfecteau.enginetest.world.LevelBuilder;

public class MenuState implements State
{
	private Button playButton, levelEditorButton, creditButton, backToMainMenu, backFromCredits, KAIZObutton;
	private Button[] levelButtonArray;
	boolean selectStage;
	static boolean gameEnded;
	private boolean levelEditor;
	boolean credits;
	private BufferedImage menu, escape, creditsBack, KAIZO, KAIZOhover;

	@Override
	public void init(GameContainer gc) 
	{
		playButton = new Button(GameContainer.WIDTH/2+20, 140, 300, 75, "/buttons/jouer.png", "/buttons/hover/jouer.png");
		creditButton = new Button(GameContainer.WIDTH/2+20, 240, 300, 75, "/buttons/credits.png", "/buttons/hover/credits.png");
		levelEditorButton = new Button(GameContainer.WIDTH/2+20, 340, 300, 75, "/buttons/levelEditor.png", "/buttons/hover/levelEditor.png");
		backToMainMenu = new Button(204, GameContainer.HEIGHT/2+30+90, 300, 75, "/buttons/retourAuMenu.png", "/buttons/hover/retourAuMenu.png");
		backFromCredits = new Button(GameContainer.WIDTH/2, 415, 300, 65, "/buttons/retourAuMenu.png", "/buttons/hover/retourAuMenu.png");
		
		try 
		{
			menu = ImageIO.read(getClass().getResource("/background/menu.png"));
			escape = ImageIO.read(getClass().getResource("/background/escape51.png"));
			creditsBack = ImageIO.read(getClass().getResource("/background/credits.png"));
			KAIZO = ImageIO.read(getClass().getResource("/buttons/KAIZO.png"));
			KAIZOhover = ImageIO.read(getClass().getResource("/buttons/hover/KAIZOhover.png"));
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		//LEVELS
		levelButtonArray = new Button[8];
		levelButtonArray[0] = new Button("Niveau 1", 102, GameContainer.HEIGHT/2-50-20, 92, 50, Color.GRAY, Color.RED);
		levelButtonArray[1] = new Button("Niveau 2", 204, GameContainer.HEIGHT/2-50-20, 92, 50, Color.GRAY, Color.RED);
		levelButtonArray[2] = new Button("Niveau 3", 306, GameContainer.HEIGHT/2-50-20, 92, 50, Color.GRAY, Color.RED);
		levelButtonArray[3] = new Button("Niveau 4", 408, GameContainer.HEIGHT/2-50-20, 92, 50, Color.GRAY, Color.RED);
		levelButtonArray[4] = new Button("Niveau 5", 510, GameContainer.HEIGHT/2-50-20, 92, 50, Color.GRAY, Color.RED);
		levelButtonArray[5] = new Button("Niveau 6", 204, GameContainer.HEIGHT/2+50-20, 92, 50, Color.GRAY, Color.RED);
		levelButtonArray[6] = new Button("Niveau 7", 306, GameContainer.HEIGHT/2+50-20, 92, 50, Color.GRAY, Color.RED);
		levelButtonArray[7] = new Button("Niveau 8", 408, GameContainer.HEIGHT/2+50-20, 92, 50, Color.GRAY, Color.RED);
		KAIZObutton = new Button(510, GameContainer.HEIGHT/2+50-20, 92, 50, KAIZO, KAIZOhover);
		
		
		selectStage = false;
		setLevelEditor(false);

		
	}

	@Override
	public void render(GameContainer gc, Graphics g) 
	{
		
		if(credits)
		{
			g.drawImage(creditsBack, 0, 0, creditsBack.getWidth(), creditsBack.getHeight(), null);
			backFromCredits.render(g);
		}
		else
		{
			g.drawImage(menu, 0, 0, GameContainer.WIDTH, GameContainer.HEIGHT, null);
			g.drawImage(escape, GameContainer.WIDTH/2-(escape.getWidth()/2), 20, escape.getWidth(), escape.getHeight(), null);
			if(!selectStage)
			{
				
				playButton.render(g);
				creditButton.render(g);
				if(gameEnded)
				{
					levelEditorButton.render(g);
				}
				
			}
			//LEVEL SELECT
			else
			{
				for(int i = 0; i < levelButtonArray.length; i++)
				{
					levelButtonArray[i].render(g);
				}
				backToMainMenu.render(g);
				if(gameEnded)
				{
					KAIZObutton.render(g);
				}

			}
		}
		

		
		

	}

	@Override
	public void update(GameContainer gc) 
	{
		if(!isLevelEditor())
		{
			for(int i = 0; i < levelButtonArray.length; i++)
			{
				if(!LevelBuilder.isStageLocked(i))
				{
					levelButtonArray[i].setHoverColor(Color.GREEN);
				}
				else
				{
					levelButtonArray[i].setHoverColor(Color.RED);
				}
			}
		}
		else
		{
			for(int i = 0; i < levelButtonArray.length; i++)
			{
				levelButtonArray[i].setHoverColor(Color.GREEN);
			}
		}

		
		if(!selectStage && credits == false)
		{

			if(playButton.isButtonTriggered(gc))
			{
				selectStage = true;
			}
			if(creditButton.isButtonTriggered(gc))
			{
				credits = true;
				selectStage = false;
			}
			if(gameEnded)
			{
				if(levelEditorButton.isButtonTriggered(gc))
				{
					selectStage = true;
					setLevelEditor(true);
				}
			}

		}
		//SELECT STAGE
		else if(credits == false)
		{
			for(int i = 0; i < levelButtonArray.length; i++)
			{
				if(levelButtonArray[i].isButtonTriggered(gc))
				{
					if(!isLevelEditor() && !LevelBuilder.isStageLocked(i))
					{
						GameContainer.playState.switchState(i);
						gc.switchState(GameContainer.PLAY_STATE);
					}
					else if(isLevelEditor())
					{
						GameContainer.levelEditorState.setModifiedStage(i);
						gc.switchState(GameContainer.LEVEL_EDITOR_STATE);
					}

				}
			}

			
			if(backToMainMenu.isButtonTriggered(gc))
			{
				selectStage = false;
				levelEditor = false;
			}
			if(gameEnded)
			{
				if(KAIZObutton.isButtonTriggered(gc))
				{
					if(!isLevelEditor() && !LevelBuilder.isStageLocked(8))
					{
						GameContainer.playState.switchState(8);
						gc.switchState(GameContainer.PLAY_STATE);
					}
					else if(isLevelEditor())
					{
						GameContainer.levelEditorState.setModifiedStage(8);
						gc.switchState(GameContainer.LEVEL_EDITOR_STATE);
					}

				}
			}
		}
		else
		{
			if(backFromCredits.isButtonTriggered(gc))
			{
				credits = false;
			}
		}


		
	}
	
	public static void triggerEnd()
	{
		gameEnded = true;
	}
	
	public void setLevelEditor(boolean levelEditor) 
	{
		this.levelEditor = levelEditor;
	}

	public void switchState(boolean selectLevel)
	{
		selectStage = selectLevel;
	}

	public boolean isLevelEditor() {
		return levelEditor;
	}

}
