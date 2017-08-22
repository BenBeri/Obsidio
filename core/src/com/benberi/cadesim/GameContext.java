package com.benberi.cadesim;


import com.benberi.cadesim.game.entity.EntityManager;
import com.benberi.cadesim.game.entity.projectile.ProjectileManager;
import com.benberi.cadesim.game.scene.GameScene;
import com.benberi.cadesim.game.scene.impl.battle.SeaBattleScene;
import com.benberi.cadesim.input.GameInputProcessor;
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
     * The projectile manager
     */
    private ProjectileManager projectileManager;

    /**
     * The entity manager
     */
    private EntityManager entities = new EntityManager();

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
        this.projectileManager = new ProjectileManager();
        this.currentScene = new SeaBattleScene(this);
        currentScene.create();
    }

    public ProjectileManager getProjectileManager() {
        return this.projectileManager;
    }

    public EntityManager getEntities() {
        return this.entities;
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
