package cn.acooo.onecenter.server;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Date;

import android.util.Log;
import cn.acooo.onecenter.App;
import cn.acooo.onecenter.BaseActivity;
import cn.acooo.onecenter.core.netty.KMessage;
import cn.acooo.onecenter.core.netty.Sender;
import cn.acooo.onecenter.model.PhoneClient;

@Sharable
public class ServerHandler extends SimpleChannelInboundHandler<KMessage> {
	
	
	@Override
    public void exceptionCaught(ChannelHandlerContext ctx,
            Throwable cause) throws Exception {
		Log.i(App.TAG, "into exceptionCaught");
        Sender sender = ChannelManager.getSender(ctx);
        if(sender.closeOnException()){
        	ChannelManager.removeChannel(ctx);
        	ctx.close();
        }
    }
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, KMessage msg)
			throws Exception {
		Log.i(App.TAG, "into channelRead0");
//		FlyProtos.CSLogin cslogin = FlyProtos.CSLogin.parseFrom(msg.getData());
		//ChannelManager.putChannel(ctx, new Player(1,ctx.channel().toString()));
//		FlyProtos.SCLogin.Builder builder = FlyProtos.SCLogin.newBuilder();
//		builder.setState(1);
//		KMessage m = new KMessage(111, builder.build().toByteArray());
//		ctx.writeAndFlush(m);
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Log.i(App.TAG, "into channelActive");
		 ctx.write("Welcome to " + InetAddress.getLocalHost().getHostName() + "!\r\n");
	     ctx.write("It is " + new Date() + " now.\r\n");
	     ctx.flush();
	     
	     InetSocketAddress socketAddress = (InetSocketAddress)ctx.channel().remoteAddress();
		 InetAddress inetaddress = socketAddress.getAddress();
		 ChannelManager.putChannel(ctx, new PhoneClient(ctx,inetaddress.getHostAddress()));
		 App.handler.sendEmptyMessage(BaseActivity.UI_MSG_ID_NEW_PHONE);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Log.i(App.TAG, "into channelInactive");
		super.channelInactive(ctx);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		Log.i(App.TAG, "into channelReadComplete");
		 ctx.flush();
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		super.channelRegistered(ctx);
		Log.i(App.TAG, "into server channelRegistered");
		
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		Log.i(App.TAG, "into channelUnregistered");
		ChannelManager.removeChannel(ctx);
		App.handler.sendEmptyMessage(BaseActivity.UI_MSG_ID_NEW_PHONE);
	}

	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx)
			throws Exception {
		Log.i(App.TAG, "into channelWritabilityChanged");
		super.channelWritabilityChanged(ctx);
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		System.out.println("into userEventTriggered");
		super.userEventTriggered(ctx, evt);
	}

}
