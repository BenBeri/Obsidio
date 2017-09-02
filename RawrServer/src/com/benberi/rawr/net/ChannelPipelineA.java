package com.benberi.rawr.net;

import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;

/**
 * @author Jony | Ben
 */
public class ChannelPipelineA extends ChannelInitializer<SocketChannel> {

    private ChannelInboundHandler handler;
    private ChannelInboundHandler decoder;
    private ChannelOutboundHandler encoder;

    public ChannelPipelineA(ChannelInboundHandler handler, ChannelInboundHandler decoder, ChannelOutboundHandler encoder) {
        this.handler = handler;
        this.encoder = encoder;
        this.decoder = decoder;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast("decoder", decoder);
        ch.pipeline().addLast("encoder", encoder);
        if (handler != null)
            ch.pipeline().addLast("handler", handler);
        System.out.println("channel initialized");
    }
}