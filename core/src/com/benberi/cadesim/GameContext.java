package com.benberi.cadesim;


import com.badlogic.gdx.Gdx;
import com.benberi.cadesim.client.ClientConnectionCallback;
import com.benberi.cadesim.client.ClientConnectionTask;
import com.benberi.cadesim.client.codec.util.Packet;
import com.benberi.cadesim.client.packet.ClientPacketHandler;
import com.benberi.cadesim.client.packet.OutgoingPacket;
import com.benberi.cadesim.client.packet.in.LoginResponsePacket;
import com.benberi.cadesim.client.packet.out.LoginPacket;
import com.benberi.cadesim.client.packet.out.ManuaverSlotChanged;
import com.benberi.cadesim.client.packet.out.PlaceCannonPacket;
import com.benberi.cadesim.client.packet.out.PlaceMovePacket;
import com.benberi.cadesim.game.entity.EntityManager;
import com.benberi.cadesim.game.entity.vessel.move.MoveType;
import com.benberi.cadesim.game.scene.ConnectScene;
import com.benberi.cadesim.game.scene.ConnectionSceneState;
import com.benberi.cadesim.game.scene.GameScene;
import com.benberi.cadesim.game.scene.TextureCollection;
import com.benberi.cadesim.game.scene.impl.battle.SeaBattleScene;
import com.benberi.cadesim.game.scene.impl.control.ControlAreaScene;
import com.benberi.cadesim.input.GameInputProcessor;
import com.benberi.cadesim.util.GameToolsContainer;
import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
     * The texture collection
     */
    private TextureCollection textures;

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
     * Executors service
     */
    private ExecutorService service = Executors.newSingleThreadExecutor();

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
        textures = new TextureCollection(this);
        textures.create();

        this.input = new GameInputProcessor(this);
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
     * Gets the texture collection
     * @return {@link #textures}
     */
    public TextureCollection getTextures() {
         return this.textures;
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
     * Sends a packet
     * @param p The packet to send
     */
    public void sendPacket(OutgoingPacket p) {
        p.encode();
        serverChannel.write(p);
        serverChannel.flush();
    }

    /**
     * Gets the connection scene
     * @return  {@link #connectScene}
     */
    public ConnectScene getConnectScene() {
        return connectScene;
    }

    /**
     * Sends a login packet to the server with the given display name
     * @param display   The display name
     */
    public void sendLoginPacket(String display) {
        LoginPacket packet = new LoginPacket();
        packet.setName(display);
        sendPacket(packet);
    }

    /**
     * Sends a move placement packet
     * @param slot  The slot to place
     * @param move  The move to place
     */
    public void sendSelectMoveSlot(int slot, MoveType move) {
        PlaceMovePacket packet = new PlaceMovePacket();
        packet.setSlot(slot);
        packet.setMove(move.getId());
        sendPacket(packet);
    }


    /**
     * Attempts to connect to server
     *
     * @param displayName   The display name
     * @param ip            The IP Address to connect
     */
    public void connect(final String displayName, String ip) {
        service.execute(new ClientConnectionTask(this, ip, new ClientConnectionCallback() {
            @Override
            public void onSuccess(Channel channel) {
                serverChannel = channel; // initialize the server channel
                connectScene.setState(ConnectionSceneState.CREATING_PROFILE);
                sendLoginPacket(displayName); // send login packet
            }

            @Override
            public void onFailure() {
                connectScene.setState(ConnectionSceneState.DEFAULT);
                connectScene.loginFailed();
            }
        }));
    }

    /**
     * Handles a login response form the server
     *
     * @param response  The response code
     */
    public void handleLoginResponse(int response) {
        if (response != LoginResponsePacket.SUCCESS) {
            serverChannel.disconnect();

            switch (response) {
                case LoginResponsePacket.NAME_IN_USE:
                    connectScene.setPopup("Display name already in use");
                    break;
                case LoginResponsePacket.SERVER_FULL:
                    connectScene.setPopup("The server is full.");
                    break;
                default:
                    connectScene.setPopup("Unknown login failure.");
                    break;
            }

            connectScene.setState(ConnectionSceneState.DEFAULT);
        }
        else {
            connectScene.setState(ConnectionSceneState.CREATING_MAP);
        }

    }

    public void setReady(boolean ready) {
        this.isReady = ready;
        Gdx.input.setInputProcessor(input);
    }

    public void dispose() {
        isReady = false;
        connected = false;
        connectScene.setup();
        connectScene.setPopup("You have disconnected from the server.");
    }

    public void sendManuaverSlotChanged(int manuaverSlot) {
        ManuaverSlotChanged packet = new ManuaverSlotChanged();
        packet.setSlot(manuaverSlot);
        sendPacket(packet);
    }

    public void sendAddCannon(int side, int slot) {
        PlaceCannonPacket packet = new PlaceCannonPacket();
        packet.setSide(side);
        packet.setSlot(slot);
        sendPacket(packet);
    }
}
