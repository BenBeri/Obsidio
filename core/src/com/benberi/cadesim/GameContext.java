package com.benberi.cadesim;


import com.benberi.cadesim.client.codec.util.Packet;
import com.benberi.cadesim.client.packet.ClientPacketHandler;
import com.benberi.cadesim.game.entity.EntityManager;
import com.benberi.cadesim.game.entity.projectile.ProjectileManager;
import com.benberi.cadesim.game.scene.GameScene;
import com.benberi.cadesim.game.scene.impl.battle.SeaBattleScene;
import com.benberi.cadesim.game.scene.impl.control.ControlAreaScene;
import com.benberi.cadesim.input.GameInputProcessor;
import com.benberi.cadesim.util.GameToolsContainer;

import java.util.ArrayList;
import java.util.List;

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
     * The sea battle scene
     */
    private SeaBattleScene seaBattleScene;

    /**
     * The control area scene
     */
    private ControlAreaScene controlArea;

    /**
     * The projectile manager
     */
    private ProjectileManager projectileManager;

    /**
     * The entity manager
     */
    private EntityManager entities = new EntityManager();

    /**
     * List of scenes
     */
    private List<GameScene> scenes = new ArrayList<GameScene>();

    /**
     * Public GSON object
     */
    private GameToolsContainer tools;

    private ClientPacketHandler packets;

    public GameContext(BlockadeSimulator main) {
        this.simulator = main;
        this.tools = new GameToolsContainer();


        // init client
        this.packets = new ClientPacketHandler(this);
    }

    /**
     * Create!
     */
    public void create() {
        this.input = new GameInputProcessor(this);
        this.projectileManager = new ProjectileManager();
        this.seaBattleScene = new SeaBattleScene(this);
        seaBattleScene.create();
        this.controlArea = new ControlAreaScene(this);
        controlArea.create();

        scenes.add(controlArea);
        scenes.add(seaBattleScene);

    }

    public List<GameScene> getScenes() {
        return this.scenes;
    }

    public ProjectileManager getProjectileManager() {
        return this.projectileManager;
    }

    public EntityManager getEntities() {
        return this.entities;
    }

    /**
     * Gets the tools container
     * @return {@link #tools}
     */
    public GameToolsContainer getTools() {
        return this.tools;
    }

    /**
     * Handles the incoming packet from the server
     * @param o The incoming packet
     */
    public void handlePacket(Packet o) {
    }

    /**
     * Gets the packet handler
     * @return {@link #packets}
     */
    public ClientPacketHandler getPacketHandler() {
        return packets;
    }
}
