package com.itndev.FactionCore.SocketConnection.Server;

import com.itndev.FactionCore.SocketConnection.IO.PacketProcessor;
import com.itndev.FactionCore.Utils.Factions.SystemUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.net.InetAddress;
import java.util.HashMap;

public class PacketHandler extends SimpleChannelInboundHandler<Object> {

    private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public static ChannelGroup getChannels() {
        return channels;
    }

    @Override
    public void channelActive(final ChannelHandlerContext channelHandlerContext) {
        channelHandlerContext.pipeline().get(SslHandler.class).handshakeFuture().addListener(
                (GenericFutureListener<Future<Channel>>) future -> {
                    SystemUtils.logger("New Connection From Client");
                    SystemUtils.logger(channelHandlerContext.channel().toString());
                    /*ctx.writeAndFlush(
                            "Welcome to " + InetAddress.getLocalHost().getHostName() + " secure chat service!\n");
                    ctx.writeAndFlush(
                            "Your session is protected by " +
                                    ctx.pipeline().get(SslHandler.class).engine().getSession().getCipherSuite() +
                                    " cipher suite.\n");*/
                    channels.add(channelHandlerContext.channel());
                });
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        // Send the received message to all channels but the current one.
        /*for (Channel c: channels) {
            if (c != channelHandlerContext.channel()) {
                c.writeAndFlush("[" + ctx.channel().remoteAddress() + "] " + msg + '\n');
            } else {
                c.writeAndFlush("[you] " + msg + '\n');
            }
        }*/

        // Close the connection if the client has sent 'bye'.
        /*if ("bye".equals(msg.toLowerCase())) {
            ctx.close();
        }*/
        HashMap<Integer, Object> stream = (HashMap<Integer, Object>) o;

        if(!stream.isEmpty()) {
            PacketProcessor.run(stream);
        } else {
            Channel channel = channelHandlerContext.channel();
            channel.close().sync();
            channels.remove(channel);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws InterruptedException {
        cause.printStackTrace();
        ctx.close().sync();
    }
}
