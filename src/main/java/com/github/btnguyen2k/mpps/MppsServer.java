package com.github.btnguyen2k.mpps;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

import com.github.btnguyen2k.mpps.handler.EchoHandler;
import com.github.btnguyen2k.mpps.handler.MemcachedAsciiProtocolDecoder;
import com.github.btnguyen2k.mpps.utils.MppsConstants;

/**
 * Application bootstrapper
 * 
 * @author Thanh Nguyen <btnguyen2k@gmail.com>
 * @since 0.1.0
 */
public class MppsServer {

    public static void main(String[] args) throws Exception {
        // setup command line options
        Options options = new Options();
        options.addOption("h", "help", false, "print this help screen");

        // read command line options
        CommandLineParser parser = new DefaultParser();
        CommandLine cmdline = parser.parse(options, args);

        MppsServer server = new MppsServer();
        server.start("0.0.0.0", 11211);
    }

    // private boolean running = false;
    // private ServerSocketChannelFactory channelFactory;
    // private DefaultChannelGroup allChannels;

    private void start(String listenAddr, int port) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(
                                    new MemcachedAsciiProtocolDecoder(270,
                                            MppsConstants.DEFAULT_MAX_VALUE_SIZE, false),
                                    new EchoHandler());
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // Bind and start to accept incoming connections.
            final ChannelFuture f = b.bind(listenAddr, port);
            // Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            // public void run() {
            // if (f.channel().isActive()) {
            // f.channel().close();
            // }
            // }
            // }));
            f.sync();

            // Wait until the server socket is closed.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

}
