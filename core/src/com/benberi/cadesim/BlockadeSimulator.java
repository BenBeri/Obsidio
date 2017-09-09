package com.benberi.cadesim;

import com.badlogic.gdx.ApplicationAdapter;
import com.benberi.cadesim.game.scene.GameScene;

public class BlockadeSimulator extends ApplicationAdapter {

	/**
	 * The game context
	 */
	private GameContext context;

	@Override
	public void create () {
		context = new GameContext(this);
		context.create();
	}

	@Override
	public void render () {

		context.getPacketHandler().tickQueue();

		if (!context.isReady()) {
			context.getConnectScene().update();
			context.getConnectScene().render();
			return;
		}
		/*
		 * Render  and update all scenes
		 */
		for (GameScene scene : context.getScenes()) {
			scene.update();
			scene.render();
		}
	}
	
	@Override
	public void dispose () {
	}
}
