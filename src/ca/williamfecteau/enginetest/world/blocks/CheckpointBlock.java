package ca.williamfecteau.enginetest.world.blocks;


import ca.williamfecteau.enginetest.world.Stage;

public class CheckpointBlock extends Block

{
	public CheckpointBlock(int x, int y, int color) 
	{
		super(x, y, "Checkpoint", color);
	}

	public void endStage(Stage stage)
	{

		stage.setxSpawn((int)this.x);
		stage.setySpawn((int)this.y-96);
	}
}
