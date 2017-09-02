package com.benberi.rawr.net.packets;

import com.benberi.rawr.net.Packet;
import com.benberi.rawr.net.PacketLength;

public class RegisterPlayerPacket extends Packet {

    public RegisterPlayerPacket(int playerId) {
        super(1, 1);
        setPacketLengthType(PacketLength.BYTE);
        setLength(1);
        writeByte(playerId);
    }
}
