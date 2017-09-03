package com.benberi.cadesim.game.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.benberi.cadesim.GameContext;

public class ConnectScene implements GameScene {

    private GameContext context;

    /**
     * Batch for opening screen
     */
    private SpriteBatch batch;

    /**
     * Background texture
     */
    private Texture background;

    private int connectAnimationState = 0;

    private long lastConnectionAnimatinoStateChange;

    private BitmapFont font;

    private boolean failed;

    public ConnectScene(GameContext ctx) {
        this.context = ctx;
    }

    @Override
    public void create() {
		/*
		 * OPENING SCREEN
		 */

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("core/assets/font/FjallaOne-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 18;
        parameter.shadowColor = new Color(0, 0, 0, 0.8f);
        parameter.shadowOffsetY = 1;
        font = generator.generateFont(parameter);
        font.setColor(Color.YELLOW);
        batch = new SpriteBatch();
        background = new Texture("core/assets/bg.png");
    }

    @Override
    public void update() {

    }

    @Override
    public void render() {
        batch.begin();
        batch.draw(background, 0,0);



			/*
			 * Cheap way of animation dots lol...
			 */
        String dot = "";

        if (connectAnimationState == 0) {
            dot = ".";
        }
        else if (connectAnimationState == 1) {
            dot = "..";
        }
        else if (connectAnimationState == 2) {
            dot = "...";
        }


        if (!failed) {
            if (!context.isConnected()) {
                String text = "Connecting to Cade Server, please wait";
                GlyphLayout layout = new GlyphLayout(font, text);
                font.draw(batch, text + dot, Gdx.graphics.getWidth() / 2 - (layout.width / 2), 300);
            } else {
                String text = "Connected, Getting things ready";
                GlyphLayout layout = new GlyphLayout(font, text);
                font.draw(batch, text + dot, Gdx.graphics.getWidth() / 2 - (layout.width / 2), 300);
            }
        }
        else {
            font.setColor(Color.RED);
            String text = "Could not connect to the given Cade Server";
            GlyphLayout layout = new GlyphLayout(font, text);
            font.draw(batch, text, Gdx.graphics.getWidth() / 2 - (layout.width / 2), 300);
        }

        if (System.currentTimeMillis() - lastConnectionAnimatinoStateChange >= 200) {
            connectAnimationState++;
            lastConnectionAnimatinoStateChange = System.currentTimeMillis();
        }
        if(connectAnimationState > 2) {
            connectAnimationState = 0;
        }

        batch.end();
    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean handleDrag(float screenX, float screenY, float diffX, float diffY) {
        return false;
    }

    @Override
    public boolean handleClick(float x, float y) {
        return false;
    }

    @Override
    public boolean handleClickRelease(float x, float y) {
        return false;
    }

    public void setFailed(boolean failed) {
        this.failed = failed;
    }
}
