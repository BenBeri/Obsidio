package com.benberi.cadesim.client.packet.in;

import com.benberi.cadesim.GameContext;
import com.benberi.cadesim.client.codec.util.Packet;
import com.benberi.cadesim.client.packet.ClientPacketExecutor;

public class SendMapPacket extends ClientPacketExecutor {

    public SendMapPacket(GameContext ctx) {
        super(ctx);
    }

    @Override
    public void execute(Packet p) {
        while(p.getBuffer().readableBytes() >= 3) {
            int tile = p.readByte();
            int x = p.readByte();
            int y = p.readByte();

            System.out.println(tile + " " + x + " " + y);
        }
    }

    @Override
    public int getSize() {
        return -1;
    }
}
