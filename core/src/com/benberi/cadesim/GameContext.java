package com.benberi.cadesim;


import com.benberi.cadesim.input.GameInputProcessor;
import com.benberi.cadesim.game.scene.GameScene;
import com.benberi.cadesim.game.scene.impl.battle.SeaBattleScene;
import com.benberi.cadesim.util.GameToolsContainer;

public class GameContext {

    /**
     * The main class of the game
     */
    private BlockadeSimulator simulator;

    /**
     * The main input processor of the game
     */
    private GameInputProcessor input;

    /**
     * The current active scene
     */
    private GameScene currentScene;

    /**
     * Public GSON object
     */
    private GameToolsContainer tools;

    public GameContext(BlockadeSimulator main) {
        this.simulator = main;
        this.tools = new GameToolsContainer();
    }

    /**
     * Create!
     */
    public void create() {
        this.input = new GameInputProcessor(this);

        SeaBattleScene sea = new SeaBattleScene(this);
        sea.create();

        this.currentScene = sea;
    }

    /**
     * Gets the current active game scene
     *
     * @return The active game scene {@link #currentScene}
     */
    public GameScene getCurrentActiveScene() {
        return this.currentScene;
    }

    /**
     * Sets active current scene {@link #currentScene}
     *
     * @param scene The new scene to be active
     */
    public void setCurrentActiveScene(GameScene scene) {
        this.currentScene = scene;
    }

    /**
     * Gets the tools container
     * @return {@link #tools}
     */
    public GameToolsContainer getTools() {
        return this.tools;
    }
}
