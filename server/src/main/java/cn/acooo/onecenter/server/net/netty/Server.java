package cn.acooo.onecenter.server.net.netty;

import android.util.Log;

import cn.acooo.onecenter.server.App;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server implements Runnable {
	private final int port;
	public Server(int port){
		this.port = port;
	}
	private  String TAG = App.TAG;
	
	@Override
	public void run() {
        //启动netty服务器，用做长连接的通信
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
            .option(ChannelOption.TCP_NODELAY, true)
            .option(ChannelOption.SO_KEEPALIVE, true)
             .channel(NioServerSocketChannel.class)
             .childHandler(new ServerInitializer());
            Log.i(TAG,"server is start");
            App.serverServiceIsRun = true;
            ChannelFuture cf = b.bind(port).sync().channel().closeFuture().sync();
            
            Log.i(TAG,"server is shutdown");
            App.serverServiceIsRun = false;
            cf.channel().closeFuture().sync();  
        }catch(InterruptedException e){
        	App.serverServiceIsRun = false;
        	e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
	}
}
