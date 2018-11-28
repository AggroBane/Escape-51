package ca.williamfecteau.enginetest.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInput implements KeyListener
{
	
	public boolean[] keyDown;
	public boolean[] keyReleased;
	public boolean[] hasKeyReleased;
	
	
	
	@SuppressWarnings("unused")
	public KeyboardInput() 
	{
		keyDown = new boolean[500];
		for(boolean b : keyDown)
		{
			b = false;
		}
		keyReleased = new boolean[500];
		for(boolean b : keyReleased)
		{
			b = false;
			
		}
		hasKeyReleased = new boolean[500];
		for(boolean b : hasKeyReleased)
		{
			b = false;
			
		}
	}
	
	
	@Override
	public void keyReleased(KeyEvent e) 
	{
		keyDown[e.getKeyCode()] = false;
		keyReleased[e.getKeyCode()] = true;

	}
	
	
	
	@Override
	public void keyPressed(KeyEvent e) 
	{
		keyDown[e.getKeyCode()] = true;
		keyReleased[e.getKeyCode()] = false;
	}
	
	@Override
	public void keyTyped(KeyEvent e) 
	{

	}
	
	public void resetReleased()
	{
		for(boolean b : keyReleased)
		{
			b = false;
		}
	}
	
	
	public boolean isKeyDown(int key)
	{
		return keyDown[key];
	}


	public boolean isKeyReleased(int key) 
	{
		
		return keyReleased[key];
	}
	
	public boolean hasKeyReleased(int key) 
	{
		
		return keyReleased[key];
	}



}
