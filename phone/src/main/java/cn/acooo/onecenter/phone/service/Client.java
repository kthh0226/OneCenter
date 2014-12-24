package cn.acooo.onecenter.phone.service;

import android.util.Log;

import cn.acooo.onecenter.phone.App;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client implements Runnable{
	private final String host;
	private final int port;
	
	public Client(String host, int port){
		this.host = host;
		this.port = port;
	}
	//todo:这里要实现自动扫描机制初始话ip
	private void init(){
		
	}
	
	@Override
	public void run() {
		this.init();
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class)
					.handler(new ClientInitializer());

			Log.i(App.TAG,"over1=======================");
			// Make the connection attempt.
            ChannelFuture f = b.connect(host, port).sync();

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
			Log.i(App.TAG,"over2=======================");
		}catch(Exception e){
			App.disconnect();
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
	}

	public String getHost() {
		return host;
	}
	public int getPort() {
		return port;
	}
	@Override
	public String toString() {
		return "Client [host=" + host + ", port=" + port + "]";
	}
	
}
