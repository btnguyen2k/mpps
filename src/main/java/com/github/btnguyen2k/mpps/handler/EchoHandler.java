package com.github.btnguyen2k.mpps.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Dummy handler that echoes back input.
 * 
 * @author Thanh Nguyen <btnguyen2k@gmail.com>
 * @since 0.1.0
 */
public class EchoHandler extends ChannelInboundHandlerAdapter {
    /**
     * {@inheritDoc}
     */
    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) {
        System.out.println(msg.getClass());

        ctx.write(msg);
        ctx.flush();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
