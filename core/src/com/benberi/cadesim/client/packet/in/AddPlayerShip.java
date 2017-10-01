package com.benberi.cadesim.client.packet.in;

import com.benberi.cadesim.GameContext;
import com.benberi.cadesim.client.codec.util.Packet;
import com.benberi.cadesim.client.packet.ClientPacketExecutor;
import com.benberi.cadesim.game.entity.vessel.impl.WarFrigate;

public class AddPlayerShip extends ClientPacketExecutor {

    public AddPlayerShip(GameContext ctx) {
        super(ctx);
    }

    @Override
    public void execute(Packet p) {

        String name = p.readByteString();
        int x = p.readByte();
        int y = p.readByte();
        int face = p.readByte();
        int ship = p.readByte();
        getContext().getEntities().addEntity(name, x, y, face, ship);
    }

    @Override
    public int getSize() {
        return -1;
    }
}
