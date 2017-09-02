package com.benberi.cadesim.client.packet.in;

import com.benberi.cadesim.GameContext;
import com.benberi.cadesim.client.codec.util.Packet;
import com.benberi.cadesim.client.packet.ClientPacketExecutor;

public class SetTimePacket extends ClientPacketExecutor {

    public SetTimePacket(GameContext ctx) {
        super(ctx);
    }

    @Override
    public void execute(Packet p) {
        int gameTime = p.readInt();
        int turnTime = p.readInt();

        getContext().getControlScene().getBnavComponent().setTime(turnTime);
        getContext().getBattleScene().getInformation().setTime(gameTime);

    }

    @Override
    public int getSize() {
        return 8;
    }
}
