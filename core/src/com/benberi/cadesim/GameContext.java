package com.benberi.cadesim;


import com.benberi.cadesim.client.codec.util.Packet;
import com.benberi.cadesim.client.packet.ClientPacketHandler;
import com.benberi.cadesim.client.packet.out.RegisterPacket;
import com.benberi.cadesim.game.entity.EntityManager;
import com.benberi.cadesim.game.entity.projectile.ProjectileManager;
import com.benberi.cadesim.game.scene.ConnectScene;
import com.benberi.cadesim.game.scene.GameScene;
import com.benberi.cadesim.game.scene.impl.battle.GameInformation;
import com.benberi.cadesim.game.scene.impl.battle.SeaBattleScene;
import com.benberi.cadesim.game.scene.impl.control.ControlAreaScene;
import com.benberi.cadesim.input.GameInputProcessor;
import com.benberi.cadesim.util.GameToolsContainer;
import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.List;

public class GameContext {

    private Channel serverChannel;

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
    private EntityManager entities;

    /**
     * List of scenes
     */
    private List<GameScene> scenes = new ArrayList<GameScene>();

    /**
     * If connected to server
     */
    private boolean connected = false;

    /**
     * If the game is ready to display
     */
    private boolean isReady = false;

    /**
     * Public GSON object
     */
    private GameToolsContainer tools;

    private ClientPacketHandler packets;

    private ConnectScene connectScene;

    public GameContext(BlockadeSimulator main) {
        this.simulator = main;
        this.tools = new GameToolsContainer();

        entities = new EntityManager(this);
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

        this.connectScene = new ConnectScene(this);
        connectScene.create();
        
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

    public boolean isConnected() {
        return this.connected;
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

    public SeaBattleScene getBattleScene() {
        return (SeaBattleScene) scenes.get(1);
    }

    public ControlAreaScene getControlScene() {
        return (ControlAreaScene) scenes.get(0);
    }

    public boolean isReady() {
        return isReady;
    }

    public void setConnect(boolean flag) {
        this.connected = flag;
    }

    public void setServerChannel(Channel serverChannel) {
        this.serverChannel = serverChannel;
    }

    public Channel getServerChannel() {
        return this.serverChannel;
    }

    /**
     * Sends the registration packet
     */
    public void sendRegistration() {
        Packet packet = new RegisterPacket(this);
        sendPacket(packet);
    }

    /**
     * Sends a packet
     * @param p The packet to send
     */
    public void sendPacket(Packet p) {
        serverChannel.write(p);
    }

    public ConnectScene getConnectScene() {
        return connectScene;
    }
}
