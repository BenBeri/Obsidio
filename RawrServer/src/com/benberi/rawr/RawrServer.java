package com.benberi.rawr;

import com.benberi.rawr.net.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Rawr Server
 *
 * RawrServer is a small server ran by one thread to run small games
 * such as the blockade simulator for low-players games
 */
public class RawrServer extends ServerBootstrap {


    /**
     * The executor
     */
    private EventLoopGroup worker = new NioEventLoopGroup(1);

    /**
     * The server context
     */
    private ServerContext context;

    public RawrServer() {
        super();
        this.context = new ServerContext(this);

        group(worker)
        .channel(NioServerSocketChannel.class)
        .option(ChannelOption.SO_BACKLOG, 100)
       // .childOption(ChannelOption.TCP_NODELAY, true)

        .childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline p = socketChannel.pipeline();
                p.addLast("encoder", new PacketEncoder());
                p.addLast("decoder", new PacketDecoder());
                p.addLast("handler", new PacketHandler(context));
            }
        });

        try {
            ChannelFuture f = bind(NetworkConstants.PORT).sync();
            System.out.println("Server started.");
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            worker.shutdownGracefully();
        }
    }


    public static void main(String... args) {
        new RawrServer();
    }
}
