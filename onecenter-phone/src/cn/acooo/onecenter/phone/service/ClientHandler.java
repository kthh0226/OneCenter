package cn.acooo.onecenter.phone.service;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import android.os.Message;
import android.util.Log;
import cn.acooo.onecenter.core.netty.KMessage;
import cn.acooo.onecenter.phone.App;
import cn.acooo.onecenter.phone.BaseActivity;

@Sharable
public class ClientHandler extends SimpleChannelInboundHandler<KMessage> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, KMessage msg)
			throws Exception {
		Log.i(App.TAG, "channel channelRead0==================");
		Log.i(App.TAG, "recive==="+msg);
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		super.channelRegistered(ctx);
		Log.i(App.TAG, "channel channelRegistered==================");
		
	}
	
	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		super.channelUnregistered(ctx);
		Log.i(App.TAG, "channel channelUnregistered==================");
		App.disconnect();
		App.handler.sendEmptyMessage(BaseActivity.UI_MSG_ID_DISCONNECTED);
		
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Log.i(App.TAG, "channel channelActive==================");
		super.channelActive(ctx);
		App.setChannel(ctx.channel());
		App.handler.sendEmptyMessage(BaseActivity.UI_MSG_ID_CONNECTED);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		Log.i(App.TAG, "channel channelReadComplete==================");
		super.channelReadComplete(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		Log.i(App.TAG, "channel exceptionCaught==================");
		super.exceptionCaught(ctx, cause);
	}
	
}
