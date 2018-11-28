package ca.williamfecteau.enginetest.menus;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import ca.williamfecteau.enginetest.GameContainer;



public class Button 
{
	private int x,y, xScale, yScale;
	private String text;
	private Rectangle bounds;
	private boolean mouseOver, basicButton;
	private Color basicColor, hoverColor;
	private BufferedImage basicImage, hoverImage;
	
	public Button(String text, int x, int y, int xScale, int yScale, Color basicColor, Color hoverColor)
	{
		this.text = text;
		this.x = x;
		this.y = y;
		this.xScale = xScale;
		this.yScale = yScale;
		this.basicColor = basicColor;
		this.hoverColor = hoverColor;
		basicButton = true;
		bounds = new Rectangle(x, y, xScale, yScale);
		
	}


	public Button(int x, int y, int xScale, int yScale, String basicImagePath, String hoverImagePath)
	{
		this.x = x;
		this.y = y;
		this.xScale = xScale;
		this.yScale = yScale;
		try 
		{
			basicImage = ImageIO.read(getClass().getResource(basicImagePath));
			hoverImage = ImageIO.read(getClass().getResource(hoverImagePath));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		basicButton = false; 
		bounds = new Rectangle(x, y, xScale, yScale);
		
	}
	
	public Button(int x, int y, int xScale, int yScale, BufferedImage image1, BufferedImage image2)
	{
		this.x = x;
		this.y = y;
		this.xScale = xScale;
		this.yScale = yScale;
		
		basicImage = image1;
		hoverImage = image2;

		basicButton = false; 
		bounds = new Rectangle(x, y, xScale, yScale);
		
	}
	
	
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	
	
	
	private boolean isMouseOver(double mouseX, double mouseY)
	{
		if(bounds.intersects(new Rectangle((int)mouseX, (int)mouseY, 1, 1)))
		{
			mouseOver = true;
		}
		else
		{
			mouseOver = false;
		}
		return mouseOver;
				
	}
	
	
	public boolean isButtonTriggered(GameContainer gc)
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
		
		isMouseOver(mouseX, mouseY);
		if(gc.isMouseLeftClick() && mouseOver)
		{
			gc.setMouseLeftClick(false);
			return true;
		}
		else return false;
	}
	
	public boolean isButtonTriggeredRelativeTo(GameContainer gc, int xScreen, int yScreen)
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
		mouseX += xScreen;
		mouseY += yScreen;
		
		isMouseOver(mouseX, mouseY);
		if(gc.isMouseLeftClick() && mouseOver)
		{
			gc.setMouseLeftClick(false);
			return true;
		}
		else return false;
	}
	
	
	
	public void render(Graphics g)
	{
		if(basicButton)
		{
			if(mouseOver)
			{
				g.setColor(hoverColor);
				g.fillRect(x, y, xScale, yScale);
			}
			else
			{
				g.setColor(basicColor);
				g.fillRect(x, y, xScale, yScale);
			}
			
			if(text != null)
			{
				g.setColor(Color.WHITE);
				g.drawString(text, x+xScale/2-25, y+yScale/2+5);
			}
		}
		else
		{
			if(mouseOver)
			{
				g.drawImage(hoverImage,x, y, xScale, yScale, null);
			}
			else
			{
				g.drawImage(basicImage,x, y, xScale, yScale, null);
			}
		}



	}

	
	public void updateCoordinate(int x, int y)
	{
		this.x = x;
		this.y = y;
		bounds = new Rectangle(this.x, this.y, xScale, yScale);
	}
	
	public void setHoverImage(String path)
	{
		try 
		{
			hoverImage = ImageIO.read(getClass().getResource(path));
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void setHoverImage(BufferedImage img)
	{
		hoverImage = img;
	}
	
	public void setImage(BufferedImage img)
	{
		basicImage = img;
	}
	
	
	public void setHoverColor(Color c)
	{
		hoverColor = c;
	}


}
