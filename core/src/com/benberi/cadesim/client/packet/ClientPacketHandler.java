package com.benberi.cadesim.client.packet;

import com.benberi.cadesim.GameContext;
import com.benberi.cadesim.client.codec.util.Packet;
import com.benberi.cadesim.client.packet.in.AddPlayerShip;
import com.benberi.cadesim.client.packet.in.SetTimePacket;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * The packet handler
 */
public class ClientPacketHandler {

    private Logger logger = Logger.getLogger("Packet Handler");

    /**
     * The game context
     */
    private GameContext context;

    /**
     * The packets map
     */
    private Map<Integer, ClientPacketExecutor> packets = new HashMap<Integer, ClientPacketExecutor>();

    public ClientPacketHandler(GameContext context) {
        this.context = context;
        registerPackets();
    }

    /**
     * Handles a packet
     * @param packet    The packet to handle
     */
    public void handle(Packet packet) {
        for (Map.Entry<Integer, ClientPacketExecutor> entry : packets.entrySet()) {
            int opcode = entry.getKey();
            ClientPacketExecutor p = entry.getValue();

            if (packet.getOpcode() == opcode) {
                if (p.getSize() == -1 || p.getSize() == packet.getSize()) {
                    p.execute(packet);
                    return;
                }
                else {
                    logger.info("Packet with opcode: " + packet.getOpcode() + " got dropped because of size: " + packet.getSize() + " instead of " + p.getSize());
                }
            }
        }

        logger.info("Packet with unknown opcode: " + packet.getOpcode() + " got dropped.");
    }

    private void registerPackets() {
        packets.put(0, new SetTimePacket(context));
        packets.put(2, new AddPlayerShip(context));
    }
}
