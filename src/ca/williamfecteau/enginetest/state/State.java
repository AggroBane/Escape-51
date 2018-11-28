package ca.williamfecteau.enginetest.state;

import java.awt.Graphics;

import ca.williamfecteau.enginetest.GameContainer;

public interface State 
{
	
	public void init(GameContainer gc);
	public void render(GameContainer gc, Graphics g);
	public void update(GameContainer gc);

}
