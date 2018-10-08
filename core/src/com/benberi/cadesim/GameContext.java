package com.benberi.cadesim;


import com.badlogic.gdx.Gdx;
import com.benberi.cadesim.client.ClientConnectionCallback;
import com.benberi.cadesim.client.ClientConnectionTask;
import com.benberi.cadesim.client.codec.util.Packet;
import com.benberi.cadesim.client.packet.ClientPacketHandler;
import com.benberi.cadesim.client.packet.OutgoingPacket;
import com.benberi.cadesim.client.packet.in.LoginResponsePacket;
import com.benberi.cadesim.client.packet.out.*;
import com.benberi.cadesim.game.cade.Team;
import com.benberi.cadesim.game.entity.EntityManager;
import com.benberi.cadesim.game.entity.vessel.move.MoveType;
import com.benberi.cadesim.game.scene.impl.connect.ConnectScene;
import com.benberi.cadesim.game.scene.impl.connect.ConnectionSceneState;
import com.benberi.cadesim.game.scene.GameScene;
import com.benberi.cadesim.game.scene.TextureCollection;
import com.benberi.cadesim.game.scene.impl.battle.SeaBattleScene;
import com.benberi.cadesim.game.scene.impl.control.ControlAreaScene;
import com.benberi.cadesim.input.GameInputProcessor;
import com.benberi.cadesim.util.GameToolsContainer;
import com.benberi.cadesim.util.RandomUtils;

import io.netty.channel.Channel;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameContext {

    private Channel serverChannel;

    public boolean clear;
    
    private int shipId = 0;

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

    public String myVessel;

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
    public Team myTeam;

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

        

        this.connectScene = new ConnectScene(this);
        connectScene.create();
        
        

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
    
    public void createFurtherScenes(int shipId) {
    	ControlAreaScene.shipId = shipId;
    	this.input = new GameInputProcessor(this);
        this.seaBattleScene = new SeaBattleScene(this);
        seaBattleScene.create();
        this.controlArea = new ControlAreaScene(this);
        controlArea.create();
        scenes.add(controlArea);
        scenes.add(seaBattleScene);
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
    public void sendLoginPacket(String display, int ship, int team) {
        LoginPacket packet = new LoginPacket();
        packet.setVersion(Constants.VERSION);
        packet.setName(display);
        packet.setShip(ship);
        packet.setTeam(team);
        sendPacket(packet);
        shipId = ship;
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
     * @throws UnknownHostException 
     */
    public void connect(final String displayName, String ip, int ship, int team) throws UnknownHostException {
    	if(!RandomUtils.validIP(ip) && RandomUtils.validUrl(ip)) {
    		try {
	    		InetAddress address = InetAddress.getByName(ip); 
	    		ip = address.getHostAddress();
    		} catch(UnknownHostException e) {
    			return;
    		}
    	}
        service.execute(new ClientConnectionTask(this, ip, new ClientConnectionCallback() {
            @Override
            public void onSuccess(Channel channel) {
                serverChannel = channel; // initialize the server channel
                connectScene.setState(ConnectionSceneState.CREATING_PROFILE);
                sendLoginPacket(displayName, ship, team); // send login packet
                myVessel = displayName;
                myTeam = Team.forId(team);
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
                case LoginResponsePacket.BAD_VERSION:
                    connectScene.setPopup("Outdated client");
                    break;
                case LoginResponsePacket.NAME_IN_USE:
                    connectScene.setPopup("Display name already in use");
                    break;
                    case LoginResponsePacket.BAD_SHIP:
                        connectScene.setPopup("The selected ship either does not exist, or not allowed");
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
        	createFurtherScenes(shipId);
            connectScene.setState(ConnectionSceneState.CREATING_MAP);
        }

    }

    public void setReady(boolean ready) {
        this.isReady = ready;
        Gdx.input.setInputProcessor(input);
        clear = true;

    }

    public void dispose() {
        controlArea.dispose();
        seaBattleScene.dispose();
        entities.dispose();
        isReady = false;
        connected = false;
        scenes.clear();
        connectScene.setup();
        if (!connectScene.hasPopup())
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

    public void sendToggleAuto(boolean auto) {
        AutoSealGenerationTogglePacket packet = new AutoSealGenerationTogglePacket();
        packet.setToggle(auto);
        sendPacket(packet);
    }

    public void sendGenerationTarget(MoveType targetMove) {
        SetSealGenerationTargetPacket packet = new SetSealGenerationTargetPacket();
        packet.setTargetMove(targetMove.getId());
        sendPacket(packet);
    }

    public void notifyFinishTurn() {
        TurnFinishNotification packet = new TurnFinishNotification();
        sendPacket(packet);
    }
}
