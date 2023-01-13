package com.itndev.FactionCore.SocketConnection.Server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.util.concurrent.GlobalEventExecutor;


public class StreamInitializer extends ChannelInitializer<SocketChannel> {


    private final SslContext sslCtx;

    public StreamInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        // Add SSL handler first to encrypt and decrypt everything.
        // In this example, we use a bogus certificate in the server side
        // and accept any invalid certificates in the client side.
        // You will need something more complicated to identify both
        // and server in the real world.
        pipeline.addLast(sslCtx.newHandler(ch.alloc()));

        // On top of the SSL handler, add the text line codec.
        pipeline.addLast(new ObjectEncoder());
        pipeline.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
        pipeline.addLast(new PacketHandler());
    }
    /*private final AtomicLong Messages = new AtomicLong();

    public static ChannelGroup getChannels() {
        return channels;
    }
    private static final ChannelGroup channels = new DefaultChannelGroup();

    public Long getMessages() {
        return Messages.get();
    }

    @Override
    public void channelConnected(ChannelHandlerContext context, ChannelStateEvent e) {
        final SslHandler sslHandler = context.getPipeline().get(SslHandler.class);
        ChannelFuture future = sslHandler.handshake();
        future.addListener(new FutureListener(sslHandler));
    }

    @Override
    public void handleUpstream(ChannelHandlerContext context, ChannelEvent e) throws Exception {
        if(e instanceof ChannelStateEvent && ((ChannelStateEvent) e).getState() != ChannelState.INTEREST_OPS) {
            SystemUtils.error_logger(e.toString());
        }
        super.handleUpstream(context, e);
    }

    @Override
    public void messageReceived(ChannelHandlerContext context, MessageEvent e) {
        Messages.incrementAndGet();
        //e.getMessage();
        //read this
        //e.getChannel().write(e.getMessage());
        HashMap<Integer, Object> message = (HashMap<Integer, Object>) e.getMessage();
        PacketProcessor.run((HashMap<Integer, Object>) e.getMessage());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, ExceptionEvent e) {
        e.getCause().printStackTrace();
        e.getChannel().close();
    }

    private static final class FutureListener implements ChannelFutureListener {
        private final SslHandler sslHandler;

        FutureListener(SslHandler sslHandler) {
            this.sslHandler = sslHandler;
        }

        public void operationComplete(ChannelFuture future) throws Exception {
            if(future.isSuccess()) {
                //future.getChannel(). send handshake packet
                channels.add(future.getChannel());
            } else {
                future.getChannel().close();
            }
        }
    }*/
}
