package cn.acooo.onecenter.phone.service;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import cn.acooo.onecenter.core.netty.KMessageDecoder;
import cn.acooo.onecenter.core.netty.KMessageEncoder;

public class ClientInitializer extends ChannelInitializer<SocketChannel>{
	
	private static final ClientHandler HANDLER = new ClientHandler();
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline p = ch.pipeline();
		p.addLast("decoder",new KMessageDecoder());
		p.addLast("encoder",new KMessageEncoder());
        p.addLast("handler",HANDLER);
	}
}
