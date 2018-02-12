package com.benberi.cadesim.game.scene.impl.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.benberi.cadesim.GameContext;
import com.benberi.cadesim.game.scene.GameScene;
import com.benberi.cadesim.game.scene.SceneComponent;

public class GameInformation extends SceneComponent {

    /**
     * The sprite batch
     */
    private SpriteBatch batch;

    private Texture panel;

    /**
     * Font for texts
     */
    private BitmapFont fontTeam;
    private BitmapFont fontPoints;
    private BitmapFont timeFont;

    private int teamOneScore;
    private int teamTwoScore;

    private int time;
    
    private String defender = "Defender";
    private String attacker = "Attacker";
    private String longestTeam = attacker;

    GameInformation(GameContext context, GameScene owner) {
        super(context, owner);
    }

    @Override
    public void create() {
        this.batch = new SpriteBatch();
        this.panel = new Texture("core/assets/ui/info.png");

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("core/assets/font/FjallaOne-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 14;
        parameter.shadowColor = new Color(0, 0, 0, 0.8f);
        parameter.shadowOffsetY = 1;
        fontTeam = generator.generateFont(parameter);

        generator = new FreeTypeFontGenerator(Gdx.files.internal("core/assets/font/Roboto-Regular.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 13;
        parameter.shadowColor = new Color(0, 0, 0, 0.6f);
        parameter.shadowOffsetY = 1;
        fontPoints = generator.generateFont(parameter);

        generator = new FreeTypeFontGenerator(Gdx.files.internal("core/assets/font/BreeSerif-Regular.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 30;

        parameter.shadowColor = new Color(0, 0, 0, 0.3f);
        parameter.shadowOffsetY = 2;

        timeFont = generator.generateFont(parameter);
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public void update() {

    }

    @Override
    public void render() {
    	int xPlacement = 40 + (longestTeam.length() * 6);
        Gdx.gl.glViewport(0,200, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.begin();
        batch.draw(panel, 5, 5);
        fontTeam.setColor(new Color(146 / 255f, 236 / 255f, 30 / 255f, 1));
        fontTeam.draw(batch, this.defender + ":", 18,120 );
        fontPoints.draw(batch, Integer.toString(teamOneScore), xPlacement,118 );

        fontTeam.setColor(new Color(162 / 255f, 7 / 255f, 7 / 255f, 1));
        fontTeam.draw(batch, this.attacker + ":", 18,97 );
        fontPoints.draw(batch, Integer.toString(teamTwoScore), xPlacement,95 );

        int minutes = time / 60;
        int seconds = time % 60;

        timeFont.setColor(new Color(1, 230 / 255f, 59 / 255f, 1));

        timeFont.draw(batch, (minutes < 10 ? "0" + minutes : minutes) + ":" + (seconds < 10 ? "0" + seconds : seconds), 62,50 );

        batch.end();
    }

    @Override
    public void dispose() {
        teamOneScore = 0;
        teamTwoScore = 0;
        time = 0;
    }

    @Override
    public boolean handleClick(float x, float y, int button) {
        return false;
    }

    @Override
    public boolean handleDrag(float screenX, float screenY, float x, float y) {
        return false;
    }

    @Override
    public boolean handleRelease(float x, float y, int button) {
        return false;
    }

    public void setPoints(int greenPoints, int redPoints) {
        this.teamOneScore = greenPoints;
        this.teamTwoScore = redPoints;
    }

	public int getTime() {
		return time;
	}
	
	public void setTeamNames(String attacker, String defender) {
		this.attacker = attacker;
		this.defender = defender;
		if(defender.length() > attacker.length()) {
			this.longestTeam = defender;
		}
		else {
			this.longestTeam = attacker;
		}
	}
}
