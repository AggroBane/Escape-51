package ca.williamfecteau.enginetest;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import ca.williamfecteau.enginetest.input.KeyboardInput;
import ca.williamfecteau.enginetest.input.MouseInput;
import ca.williamfecteau.enginetest.state.LevelEditorState;
import ca.williamfecteau.enginetest.state.MenuState;
import ca.williamfecteau.enginetest.state.PlayState;
import ca.williamfecteau.enginetest.state.State;
import ca.williamfecteau.enginetest.world.LevelBuilder;

public class GameContainer extends Canvas implements Runnable
{

	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 720, HEIGHT = 480;
	public static final String NAME = "Escape 51 - 1.0";
	
	private Thread thread;
	private boolean running = false;
	private KeyboardInput keyListener;
	private MouseInput mouseListener;
	private State[] state;
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	BufferedImage loading;
	private static int currentState;
	public static GameContainer instance;
	
	


	public static PlayState playState; 
	public static MenuState menuState; 
	public static LevelEditorState levelEditorState; 
	public static final int MENU_STATE = 0;
	public static final int PLAY_STATE = 1;
	public static final int LEVEL_EDITOR_STATE = 2;
	
	final int TICKS_PER_SECOND = 25;
	final int SKIP_TICKS = 1000 / TICKS_PER_SECOND;
	final int MAX_FRAMESKIP = 5;


	private synchronized void start()
	{
		init();


		if(!running)
		{
			running = true;
			thread = new Thread(this);
			thread.start();
		}
	}

	private synchronized void stop()
	{
		if(running)
		{
			try 
			{
				thread.join();
			} catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
			System.exit(1);
		}
	}

	public void init()
	{
		instance = this;
		try 
		{
			loading = ImageIO.read(getClass().getResource("/background/loading.png"));
		} catch (IOException e1) 
		{
			e1.printStackTrace();
		}
		keyListener = new KeyboardInput();
		mouseListener = new MouseInput();
		try {
			this.getGraphics().drawImage(loading, 0, 0, WIDTH, HEIGHT, null);
			new LevelBuilder();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		this.addKeyListener(keyListener);
		this.addMouseListener(mouseListener);



		state = new State[3];

		state[MENU_STATE] = new MenuState();
		state[MENU_STATE].init(this);
		menuState = (MenuState) state[MENU_STATE];

		state[PLAY_STATE] = new PlayState();
		state[PLAY_STATE].init(this);
		playState = (PlayState) state[PLAY_STATE];
		
		state[LEVEL_EDITOR_STATE] = new LevelEditorState();
		state[LEVEL_EDITOR_STATE].init(this);
		levelEditorState = (LevelEditorState) state[LEVEL_EDITOR_STATE];




		currentState = 0;
	}


	@Override
	public void run() 
	{
		double next_game_tick = System.currentTimeMillis();
		int loops;

		while(running) 
		{
			loops = 0;
			while (System.currentTimeMillis() > next_game_tick && loops < MAX_FRAMESKIP) 
			{

				update();

				next_game_tick += SKIP_TICKS;
				loops++;
			}


			render();
		}
		stop();
		
	}



	private void update()
	{
		keyListener.resetReleased();
		state[currentState].update(this);
		
	}


	private void render()
	{
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null)
		{
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		//-------------------------------------
		//render ici



		g.drawImage(image, 0, 0, null);
		state[currentState].render(this, g);



		//-------------------------------------
		g.dispose();
		bs.show();

	}

	public void switchState(int newState)
	{
		currentState = newState;
	}


	public boolean isKeyDown(int key)
	{
		return keyListener.isKeyDown(key);
	}



	public boolean isKeyReleased(int key)
	{
		return keyListener.isKeyReleased(key);
	}
	
	public boolean hasKeyReleased(int key)
	{
		return keyListener.hasKeyReleased(key);
	}
	

	public boolean isMouseLeftClick()
	{
		return mouseListener.isLeftClick();
	}
	
	public void setMouseLeftClick(boolean state)
	{
		mouseListener.setMouseLeftClick(state);
	}
	
	public void setMouseRightClick(boolean state)
	{
		mouseListener.setMouseRightClick(state);
	}
	
	public boolean isMouseRightClick() 
	{
		return mouseListener.isRightClick();
	}
	
	public static int getCurrentState() 
	{
		return currentState;
	}



	//Main
	public static void main(String[] args)
	{
		GameContainer gc = new GameContainer();
		
		
		gc.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		JFrame frame = new JFrame(NAME);
		frame.setResizable(false);
		frame.add(gc);
		

		
		BufferedImage icon = null;
		try 
		{
			icon = ImageIO.read(GameContainer.class.getResource("/icon.png"));
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		frame.setIconImage(icon);
		frame.pack();
		
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		gc.start();

	}






}
