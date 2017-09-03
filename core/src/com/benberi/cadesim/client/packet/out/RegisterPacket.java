package com.benberi.cadesim.client.packet.out;

import com.benberi.cadesim.GameContext;
import com.benberi.cadesim.client.codec.util.Packet;
import com.benberi.cadesim.client.codec.util.PacketLength;

public class RegisterPacket extends Packet {

    public RegisterPacket(GameContext context) {
        super(0);
        setPacketLengthType(PacketLength.BYTE);
        writeByteString("jony");
        writeByte(0);
        writeByte(1);
        setLength(getBuffer().readableBytes());
    }
}
