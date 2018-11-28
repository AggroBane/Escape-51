package ca.williamfecteau.enginetest.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import ca.williamfecteau.enginetest.GameContainer;
import ca.williamfecteau.enginetest.world.Stage;
import ca.williamfecteau.enginetest.world.blocks.Block;

public class Player implements Entity
{


	public double dx;
	public double dy;

	private int lastDirection, currentFrameBetween, currentFrame, direction = 0;
	private double x, y, frameBetweenAnimation;
	private final int xScale = 48, yScale = 96;
	private Rectangle bounds;
	private boolean isJumping;
	public boolean oneJump;
	private Stage stage;
	private double gravityStack = 0;
	private Block collisionBlock;

	private BufferedImage spriteSheet;
	private BufferedImage[][] playerSprite;


	//SPRITE SHEET TEXTURE
	public static final int STATIC = 0;
	public static final int RIGHT_MOVE = 1;
	public static final int LEFT_MOVE = 2;
	public static final int JUMP_RIGHT = 3;
	public static final int JUMP_LEFT = 4;

	public static final int FRAME_BETWEEN = 70;
	public static final int MIN_FRAME_BETWEEN = 30;

	//MOUVEMENTS
	public static final float GRAVITY = 0.2F;
	public static final float AIR_RESISTANCE = 0.9F;

	//X
	public static final float START_DX = 4;
	public static final float DX_PER_TICK = 0.2F;
	public static final float MAX_DX = 10;

	//Y
	public static final float START_DY = -4;
	public static final float DY_PER_TICK = -1;
	public static final float MAX_DY = -12;

	//DIRECTIONS
	public static final  int RIGHT = 1;
	public static final  int LEFT = -1;



	public Player(Stage stage) 
	{
		x = 0;
		y = 0;
		oneJump = true;
		this.stage = stage;
		playerSprite = new BufferedImage[6][5];
		lastDirection = (int)RIGHT;
		isJumping = false;
		frameBetweenAnimation = FRAME_BETWEEN;
		currentFrame = 0;
		try 
		{
			spriteSheet = ImageIO.read(getClass().getResource("/entities/playerSpriteSheet.png"));
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		for(int y = 0; y < 5; y++)
		{
			for(int x = 0; x < 6; x++)
			{
				playerSprite[x][y] = spriteSheet.getSubimage(x*196, y*389, 196, 389);
			}
		}
		bounds = new Rectangle((int)x, (int)y, xScale, yScale);

	}




	@Override
	public void render(Graphics g) 
	{
		if(direction == RIGHT)
		{
			if(dy > 0)
			{
				g.drawImage(playerSprite[1][JUMP_RIGHT], (int)x, (int)y, xScale+18, yScale, null);
			}
			else if(dy < 0)
			{

				g.drawImage(playerSprite[0][JUMP_RIGHT], (int)x, (int)y, xScale, yScale, null);
			}
			else
			{
				g.drawImage(runAnimation(RIGHT_MOVE), (int)x, (int)y, xScale, yScale, null);
			}

		}
		else if(direction == LEFT)
		{
			if(dy > 0)
			{
				g.drawImage(playerSprite[1][JUMP_LEFT], (int)x, (int)y, xScale+18, yScale, null);
			}
			else if(dy < 0)
			{
				g.drawImage(playerSprite[0][JUMP_LEFT], (int)x, (int)y, xScale+18, yScale, null);
			}
			else
			{
				g.drawImage(runAnimation(LEFT_MOVE), (int)x, (int)y, xScale, yScale, null);
			}
		}
		else
		{
			if(lastDirection == RIGHT)
			{
				if(dy > 0)
				{
					g.drawImage(playerSprite[1][JUMP_RIGHT], (int)x, (int)y, xScale+18, yScale, null);
				}
				else if(dy < START_DY)
				{
					g.drawImage(playerSprite[0][JUMP_RIGHT], (int)x, (int)y, xScale, yScale, null);
				}
				else
				{
					g.drawImage(playerSprite[0][STATIC], (int)x, (int)y, xScale, yScale, null);
				}
			}
			else if(lastDirection == LEFT)
			{
				if(dy > 0)
				{
					g.drawImage(playerSprite[1][JUMP_LEFT], (int)x, (int)y, xScale+18, yScale, null);
				}
				else if(dy < 0)
				{
					g.drawImage(playerSprite[0][JUMP_LEFT], (int)x, (int)y, xScale+18, yScale, null);
				}
				else
				{
					g.drawImage(playerSprite[1][STATIC], (int)x, (int)y, xScale, yScale, null);
				}
			}

		}

	}


	public BufferedImage runAnimation(int movement)
	{
		if(frameBetweenAnimation-0.1 >= MIN_FRAME_BETWEEN)
		{
			frameBetweenAnimation -= 0.1;
		}

		if(currentFrameBetween == 0)
		{
			currentFrameBetween = (int)frameBetweenAnimation;
			if(currentFrame != 5)
			{
				currentFrame++;
			}
			else
			{
				currentFrame = 0;
			}


		}
		else
		{
			currentFrameBetween--;
		}


		return playerSprite[currentFrame][movement];

	}



	public int getxScale() {
		return xScale;
	}




	public int getyScale() {
		return yScale;
	}




	@Override
	public void update(ArrayList<Block> blockList) 
	{
		move(blockList);

	}





	public void move(ArrayList<Block> blockList)
	{

		fall(blockList);








		if(dy < 0)
		{
			if(collision(bounds, blockList))
			{
				y += 10;
			}
			boolean triggerJump = true;
			for(int i = 0; i >= dy; i--)
			{
				if(collision(new Rectangle((int)x, (int)(y-i+START_DY-2), xScale, yScale), blockList))
				{
					triggerJump = false;
					dy = -START_DY;
					gravityStack = 0;
					isJumping = true;
				}
			}
			if(triggerJump)
			{
				y += dy;
				gravityStack += GRAVITY;
				gravityStack *= AIR_RESISTANCE;
				dy += gravityStack;
			}


		}


		if(direction != 0)
		{
			if(direction == RIGHT)
			{


				if(collision(new Rectangle((int)(x+dx), (int)y, xScale, yScale), blockList))
				{
					direction = 0;
					dx = 0;
				}
				if(x+dx+xScale > Stage.BACKGROUND_WIDTH*(Stage.MAX_BACKGROUND_LOAD-2)+GameContainer.WIDTH)
				{
					dx = 0;
					direction = 0;
				}
	
	

				x += dx;
				dx = approach(dx);

			}
			if(direction == LEFT)
			{
				if(collision(new Rectangle((int)(x+dx), (int)y, xScale, yScale), blockList))
				{
					dx = 0;
					direction = 0;
				}
				if(x + dx < 0)
				{
					x = 0;
					dx = 0;
				}
				else
				{
					x += dx;
					dx = -approach(-dx);
				}

			}
		}

		bounds = new Rectangle((int)x, (int)y, xScale, yScale);
	}





	public int getDirection() {
		return direction;
	}




	public void fall(ArrayList<Block> blockList)
	{
		boolean triggerFall = false;
		if(y > 480)
		{
			stage.respawnPlayer();
			dx = 0;
			dy = 0;
		}
		if(dy >= 0)
		{
			for(int i = 0; i <= dy; i++)
			{
				if(!collision(new Rectangle((int)x, (int)y+i+1, xScale, yScale), blockList))
				{
					triggerFall = true;
					isJumping = true;
				}
				else
				{
					dy = 0;
					if(this.y <= collisionBlock.getY() && collisionBlock.getType() != "Spike")
					{
						y = collisionBlock.getY()-this.yScale;
					}
					isJumping = false;
					gravityStack = 0;

					break;

				}
			}
			if(triggerFall)
			{
				y -= dy;
				gravityStack += GRAVITY;
				gravityStack *= 0.9;
				dy += gravityStack;
			}
		}



	}




	public boolean collision(Rectangle a, ArrayList<Block> blockList)
	{

		for(Block block : blockList)
		{

			if(a.intersects(block.getBounds()))
			{
				Block nextBlock = stage.getBlockAt((int)block.getX()+32, (int)block.getY());
				if(nextBlock != null && isJumping)
				{
					nextBlock.playerCollision(stage);
					nextBlock.endStage(this.stage);	
				}
				block.endStage(this.stage);	
				block.playerCollision(stage);
				collisionBlock = block;
				return true;
			}
			else if((direction == Player.STATIC && dy==0 && new Rectangle(a.x, a.y+1, a.width, a.height).intersects(block.getBounds())) || (lastDirection == Player.RIGHT && direction == Player.LEFT && new Rectangle(a.x+20, a.y+1, a.width, a.height).intersects(block.getBounds())) || (lastDirection == Player.LEFT && direction == Player.RIGHT && new Rectangle(a.x-20, a.y+1, a.width, a.height).intersects(block.getBounds())))
			{
				block.endStage(this.stage);	
				block.playerCollision(stage);

				collisionBlock = block;
			}




		}
		collisionBlock = null;
		return false;
	}





	public double approach(double actualddx)
	{
		if(!isJumping)
		{
			if(actualddx < MAX_DX)
			{
				return actualddx + DX_PER_TICK;
			}

			else return actualddx;
		}
		else return actualddx;
	}


	public void setJumping(boolean isJumping) 
	{
		this.isJumping = isJumping;
	}

	public void respawn(int x, int y)
	{
		this.dx = 0;
		this.dy = 0;
		this.x = x;
		this.y = y;

	}


	public boolean isJumping() 
	{
		return isJumping;
	}


	public void setDirection(int direction)
	{
		if(direction == Player.RIGHT)
		{

			lastDirection = RIGHT;
			frameBetweenAnimation = FRAME_BETWEEN;
			currentFrameBetween = 0;
			dx = START_DX;
		}
		if(direction == Player.LEFT)
		{

			lastDirection = LEFT;
			frameBetweenAnimation = FRAME_BETWEEN;
			currentFrameBetween = 0;
			dx = -START_DX;
		}
		this.direction = direction;
	}


	@Override
	public double getX() 
	{
		return x;
	}

	@Override
	public double getY() 
	{

		return y;
	}




	@Override
	public Rectangle getBounds() 
	{
		return bounds;
	}




	public void setX(double x) {
		this.x = x;
	}




	public void setY(double y) {
		this.y = y;
	}

}
