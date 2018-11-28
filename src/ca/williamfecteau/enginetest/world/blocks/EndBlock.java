package ca.williamfecteau.enginetest.world.blocks;

import ca.williamfecteau.enginetest.GameContainer;
import ca.williamfecteau.enginetest.state.PlayState;
import ca.williamfecteau.enginetest.world.LevelBuilder;
import ca.williamfecteau.enginetest.world.Stage;

public class EndBlock extends Block
{

	public EndBlock(int x, int y, int color) 
	{
		super(x, y, "End", color);
	}

	public void endStage(Stage stage)
	{
		if(GameContainer.getCurrentState() != GameContainer.LEVEL_EDITOR_STATE && GameContainer.playState.getCurrentStage()+1 < LevelBuilder.getStageList().length)
		{		
			GameContainer.playState.switchState(GameContainer.playState.getCurrentStage()+1);
		}
		else if(GameContainer.getCurrentState() != GameContainer.LEVEL_EDITOR_STATE)
		{
			PlayState.kaizoBeated();
		}
	}
	
}
