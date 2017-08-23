package com.benberi.cadesim;

import com.badlogic.gdx.ApplicationAdapter;
import com.benberi.cadesim.game.scene.GameScene;

public class BlockadeSimulator extends ApplicationAdapter {

	private GameContext context;

	@Override
	public void create () {
		context = new GameContext(this);
		context.create();
	}

	@Override
	public void render () {
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
