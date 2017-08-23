package com.benberi.cadesim.game.scene.impl.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.benberi.cadesim.GameContext;
import com.benberi.cadesim.game.scene.GameScene;

public class ControlAreaScene implements GameScene {

    private GameContext context;

    Texture t;
    public ControlAreaScene(GameContext context) {
        this.context = context;
    }

    @Override
    public void create() {
        t = new Texture("core/assets/ui/move_empty.png");
    }

    @Override
    public void update() {
    }

    @Override
    public void render() {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        ShapeRenderer shapeRenderer = new ShapeRenderer();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(65 / 255f, 101 / 255f, 139 / 255f, 1));
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), 200);

        shapeRenderer.setColor(new Color(72 / 255f, 72 / 255f, 72 / 255f, 1));
        shapeRenderer.rect(0, 199, Gdx.graphics.getWidth(), 1);

        shapeRenderer.setColor(new Color(135 / 255f, 161 / 255f, 188 / 255f, 1));
        shapeRenderer.rect(0, 198, Gdx.graphics.getWidth(), 1);

        shapeRenderer.setColor(new Color(68 / 255f, 101 / 255f, 136 / 255f, 1));
        shapeRenderer.rect(0, 197, Gdx.graphics.getWidth(), 1);
        
        shapeRenderer.end();

    }

    @Override
    public void dispose() {

    }

    @Override
    public void handleDrag(float x, float y) {

    }

    @Override
    public void handleClick(float x, float y) {

    }
}
