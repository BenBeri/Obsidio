package com.benberi.rawr.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<Packet> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf buffer) throws Exception {

        // Action code of the packet
        int opcode = packet.getOpcode();

        // The length type of the packet length (if the length is a byte, a short or a medium(int) )
        int lengthType = packet.getPacketLengthType();

        // The length of the packet
        int length = packet.getLength();

        PacketLength type = PacketLength.get(lengthType);

        buffer.writeByte(opcode);
        buffer.writeByte(lengthType);

        assert type != null;
        switch(type) {
            case BYTE:
                buffer.writeByte(length);
                break;
            case SHORT:
                buffer.writeShort(length);
                break;
            case MEDIUM:
                buffer.writeMedium(length);
                break;
        }

        buffer.writeBytes(packet.getBuffer());

        // Flush it
        ctx.writeAndFlush(buffer);
    }
}
