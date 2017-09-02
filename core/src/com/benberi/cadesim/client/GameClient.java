package com.benberi.cadesim.client;

import com.benberi.cadesim.Constants;
import com.benberi.cadesim.GameContext;
import com.benberi.cadesim.client.codec.ClientChannelHandler;
import com.benberi.cadesim.client.codec.util.PacketDecoder;
import com.benberi.cadesim.client.codec.util.PacketEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class GameClient extends Bootstrap implements Runnable {

    /**
     * The game context
     */
    private GameContext context;

    /**
     * The worker
     */
    private EventLoopGroup worker = new NioEventLoopGroup(1);

    public GameClient(GameContext ctx) {
        this.context = ctx;
    }

    @Override
    public void run() {
        group(worker);
        channel(NioSocketChannel.class);
        //option(ChannelOption.TCP_NODELAY, true);
        handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline p = socketChannel.pipeline();
                p.addLast("encoder", new PacketEncoder());
                p.addLast("decoder", new PacketDecoder());
                p.addLast("handler", new ClientChannelHandler(context));
            }
        });

        ChannelFuture f = null;

        try {
            f = connect("127.0.0.1", Constants.PROTOCOL_PORT).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            worker.shutdownGracefully();
        }
    }
}
