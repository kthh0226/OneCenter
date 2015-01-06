package cn.acooo.onecenter.server.net.netty;

import android.os.Message;
import android.util.Log;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Date;

import cn.acooo.onecenter.core.BaseActivity;
import cn.acooo.onecenter.core.netty.KMessage;
import cn.acooo.onecenter.core.netty.Sender;
import cn.acooo.onecenter.server.App;
import cn.acooo.onecenter.server.model.PhoneClient;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Sharable
public class ServerHandler extends SimpleChannelInboundHandler<KMessage> {
	
	
	@Override
    public void exceptionCaught(ChannelHandlerContext ctx,
            Throwable cause) throws Exception {
		Log.e(App.TAG, "into exceptionCaught",cause);
        Sender sender = ChannelManager.getSender(ctx);
        if(sender.closeOnException()){
        	ChannelManager.removeChannel(ctx);
        	ctx.close();
        	App.handler.sendEmptyMessage(BaseActivity.UI_MSG_ID_NEW_PHONE);
        }
    }
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, KMessage msg)
			throws Exception {
		Log.i(App.TAG, "into channelRead0");
		Message m = Message.obtain();
		m.obj = msg.getData();
		m.what = msg.getMessageType();
		m.arg1 = msg.getMessageCode();
		App.handler.sendMessage(m);
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
        super.channelReadComplete(ctx);
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
