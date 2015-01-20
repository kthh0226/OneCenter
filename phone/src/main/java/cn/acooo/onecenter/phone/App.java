package cn.acooo.onecenter.phone;

import android.app.Application;
import android.os.Handler;

import com.google.protobuf.AbstractMessage.Builder;

import org.eclipse.jetty.server.Server;

import java.util.HashMap;
import java.util.Map;

import cn.acooo.onecenter.core.auto.OneCenterProtos.MessageCode;
import cn.acooo.onecenter.core.auto.OneCenterProtos.MessageType;
import cn.acooo.onecenter.core.netty.KMessage;
import cn.acooo.onecenter.phone.service.PhoneUdpServer;
import io.netty.channel.ChannelHandlerContext;

public class App extends Application {
	private static App app;
	private static ChannelHandlerContext session;
	public static volatile boolean socketIsRun = false;
	public static final String TAG = "ONE";
	public static Handler handler;
    public static Server jettyServer;
    public static PhoneUdpServer phoneUdpServer;

	/**
	 * <packageName,publicSourceDir>
	 */
	public static Map<String,String> apkPaths = new HashMap<String,String>();
	
	@SuppressWarnings("rawtypes")
	public static void send(MessageType messageType,Builder builder){
        send(messageType,MessageCode.SYS_NORMAL,builder);
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
