package com.benberi.cadesim;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
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
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		GameScene scene = context.getCurrentActiveScene();
		scene.update();
		scene.render();
	}
	
	@Override
	public void dispose () {
	}
}
