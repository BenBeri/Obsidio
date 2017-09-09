package com.benberi.cadesim.client.packet.out;

import com.benberi.cadesim.client.codec.util.PacketLength;
import com.benberi.cadesim.client.packet.OutgoingPacket;

/**
 * The login packet requests the server to
 * connect to the game, with the given display name.
 */
public class LoginPacket extends OutgoingPacket {

    private String name;

    public LoginPacket() {
        super(0);
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void encode() {
        setPacketLengthType(PacketLength.BYTE);
        writeByteString(name);
        setLength(getBuffer().readableBytes());
    }
}
