package cn.acooo.onecenter.phone;

import io.netty.channel.ChannelHandlerContext;
import android.app.Application;
import android.os.Handler;

import cn.acooo.onecenter.auto.OneCenterProtos.MessageCode;
import cn.acooo.onecenter.auto.OneCenterProtos.MessageType;
import cn.acooo.onecenter.core.netty.KMessage;

import com.google.protobuf.AbstractMessage.Builder;

public class App extends Application {
	private static App app;
	private static ChannelHandlerContext session;
	public static volatile boolean socketIsRun = false;
	public static final String TAG = "ONE";
	public static Handler handler;
	
	
	@SuppressWarnings("rawtypes")
	public static void send(MessageType messageType,Builder builder){
		KMessage m = new KMessage(messageType.getNumber(), builder.build().toByteArray());
		session.writeAndFlush(m);
	}
	
	@SuppressWarnings("rawtypes")
	public static void send(MessageType messageType,MessageCode messageCode,Builder builder){
		KMessage m = new KMessage(messageType.getNumber(), messageCode.getNumber(),builder.build().toByteArray());
		session.writeAndFlush(m);
	}
	
	public static void setChannelHandlerContext(ChannelHandlerContext c){
		session = c;
		socketIsRun = true;
	}
	
	public static void disconnect(){
		if(session != null){
			session.disconnect();
			socketIsRun = false;
		}
	}
	
	public static App getInstance(){
		return app;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		app = this;
	}
}
