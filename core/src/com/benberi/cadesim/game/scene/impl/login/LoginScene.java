package com.benberi.cadesim.game.scene.impl.login;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.benberi.cadesim.GameContext;
import com.benberi.cadesim.game.scene.GameScene;

public class LoginScene implements GameScene {

    private GameContext context;

    private Stage stage;

    public LoginScene(GameContext gameContext) {
        this.context = gameContext;
    }

    @Override
    public void create() {
        stage = new Stage();


        Gdx.input.setInputProcessor(stage);
        TextButton button = new TextButton("hello", new TextButton.TextButtonStyle());
        stage.addActor(button);
    }

    @Override
    public void update() {

    }

    @Override
    public void render() {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
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
