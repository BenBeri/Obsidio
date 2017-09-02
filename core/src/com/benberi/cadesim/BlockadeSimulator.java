package com.benberi.cadesim;

import com.badlogic.gdx.ApplicationAdapter;
import com.benberi.cadesim.game.scene.GameScene;
import com.benberi.cadesim.client.GameClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BlockadeSimulator extends ApplicationAdapter {

	/**
	 * The game context
	 */
	private GameContext context;

	@Override
	public void create () {
		context = new GameContext(this);
		context.create();

		ExecutorService service = Executors.newSingleThreadExecutor();
		GameClient client = new GameClient(context);
		service.execute(client);
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
