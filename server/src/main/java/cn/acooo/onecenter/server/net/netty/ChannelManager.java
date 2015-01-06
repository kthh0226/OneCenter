package cn.acooo.onecenter.server.net.netty;

import io.netty.channel.ChannelHandlerContext;

import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import cn.acooo.onecenter.core.netty.Sender;
import cn.acooo.onecenter.server.model.PhoneClient;
/**
 * 所有与服务器建立连接的管理器
 * @author bear
 *
 */
public class ChannelManager {
	private final static ConcurrentHashMap<ChannelHandlerContext, PhoneClient> channels =
			new ConcurrentHashMap<ChannelHandlerContext, PhoneClient>();
	
	public final static void putChannel(ChannelHandlerContext ctx, PhoneClient client){
		channels.put(ctx, client);
	}
	
	public final static void removeChannel(ChannelHandlerContext ctx){
		channels.remove(ctx);
	}
	public final static Sender getSender(ChannelHandlerContext ctx){
		return channels.get(ctx);
	}
	
	public final static void debugChannelsString(){
		System.out.println(channels);
	} 
	
	public final static Enumeration<PhoneClient> getSenders(){
		return channels.elements();
	}
	
}
