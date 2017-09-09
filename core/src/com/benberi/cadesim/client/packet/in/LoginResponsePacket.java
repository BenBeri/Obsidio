package com.benberi.cadesim.client.packet.in;

import com.benberi.cadesim.GameContext;
import com.benberi.cadesim.client.codec.util.Packet;
import com.benberi.cadesim.client.packet.ClientPacketExecutor;

public class LoginResponsePacket extends ClientPacketExecutor {

    public static final int SUCCESS = 0;
    public static final int NAME_IN_USE = 1;
    public static final int SERVER_FULL = 2;

    public LoginResponsePacket(GameContext ctx) {
        super(ctx);
    }

    @Override
    public void execute(Packet p) {
        int response = p.readByte();
        getContext().handleLoginResponse(response);
    }

    @Override
    public int getSize() {
        return -1;
    }
}
