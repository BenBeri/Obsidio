package com.benberi.cadesim.client.packet.in;

import com.benberi.cadesim.GameContext;
import com.benberi.cadesim.client.codec.util.Packet;
import com.benberi.cadesim.client.packet.ClientPacketExecutor;

public class SendMoveTokensPacket extends ClientPacketExecutor {

    public SendMoveTokensPacket(GameContext ctx) {
        super(ctx);
    }

    @Override
    public void execute(Packet p) {
        int left = p.readByte();
        int forward = p.readByte();
        int right = p.readByte();
        int cannons = p.readByte();

        getContext().getControlScene().getBnavComponent().setMoves(left, forward, right);
        getContext().getControlScene().getBnavComponent().setLoadedCannonballs(cannons);
    }

    @Override
    public int getSize() {
        return -1;
    }
}
