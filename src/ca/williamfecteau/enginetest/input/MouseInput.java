package ca.williamfecteau.enginetest.input;



import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;




public class MouseInput implements MouseListener
{
	boolean leftClick = false;
	boolean rightClick = false;
	
	@Override
	public void mouseClicked(MouseEvent e) 
	{

	}

	@Override
	public void mouseEntered(MouseEvent e) 
	{

		
	}

	@Override
	public void mouseExited(MouseEvent e) 
	{
		
	}

	@Override
	public void mousePressed(MouseEvent e) 
	{
		if(e.getButton() == MouseEvent.BUTTON1)
		{
			leftClick = true;
		}
		if(e.getButton() == MouseEvent.BUTTON3)
		{
			rightClick = true;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) 
	{
		if(e.getButton() == MouseEvent.BUTTON1)
		{
			leftClick = false;
		}
		if(e.getButton() == MouseEvent.BUTTON3)
		{
			rightClick = false;
		}
	}
	
	public boolean isLeftClick()
	{
		return leftClick;
	}

	public void setMouseLeftClick(boolean state) 
	{
		leftClick = state;
		
	}
	
	public void setMouseRightClick(boolean state) 
	{
		rightClick = state;
		
	}

	public boolean isRightClick()
	{
		return rightClick;
	}
	

	

	

}
