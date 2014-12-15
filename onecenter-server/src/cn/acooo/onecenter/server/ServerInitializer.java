package cn.acooo.onecenter.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import cn.acooo.onecenter.core.netty.KMessageDecoder;
import cn.acooo.onecenter.core.netty.KMessageEncoder;

public class ServerInitializer extends ChannelInitializer<SocketChannel>{

	private static final ServerHandler HANDLER = new ServerHandler();
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		
		ChannelPipeline p = ch.pipeline();
		p.addLast("decoder",new KMessageDecoder());
		p.addLast("encoder",new KMessageEncoder());
        p.addLast("handler",HANDLER);
		
	}

}
