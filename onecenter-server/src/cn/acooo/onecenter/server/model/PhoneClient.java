package cn.acooo.onecenter.server.model;

import io.netty.channel.ChannelHandlerContext;
import cn.acooo.onecenter.auto.OneCenterProtos.MessageCode;
import cn.acooo.onecenter.auto.OneCenterProtos.MessageType;
import cn.acooo.onecenter.core.netty.KMessage;
import cn.acooo.onecenter.core.netty.Sender;

import com.google.protobuf.AbstractMessage.Builder;

public class PhoneClient implements Sender {
	private String model;//手机型号
	private String sdkVersion;//版本号
	private String ip;
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public PhoneClient(){
		
	}
	public PhoneClient(ChannelHandlerContext session){
		this.session = session;
	}
	public PhoneClient(ChannelHandlerContext session,String ip){
		this.session = session;
		this.ip = ip;
	}
	@SuppressWarnings("rawtypes")
	public void send(MessageType messageType,Builder builder){
		KMessage m = new KMessage(messageType.getNumber(), builder.build().toByteArray());
		this.session.writeAndFlush(m);
	}
	
	@SuppressWarnings("rawtypes")
	public void send(MessageType messageType,MessageCode messageCode,Builder builder){
		KMessage m = new KMessage(messageType.getNumber(), messageCode.getNumber(),builder.build().toByteArray());
		this.session.writeAndFlush(m);
	}
	
	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getSdkVersion() {
		return sdkVersion;
	}

	public void setSdkVersion(String sdkVersion) {
		this.sdkVersion = sdkVersion;
	}

	private int state;
	private volatile ChannelHandlerContext session;
	
	@Override
	public boolean closeOnException() {
		return true;
	}

	@Override
	public void disconnect() {
		this.session.disconnect();
		
	}

	@Override
	public int getState() {
		return state;
	}

	@Override
	public void setSession(ChannelHandlerContext session) {
		this.session = session;
	}

	@Override
	public void setState(int state) {
		this.state = state;
	}
	@Override
	public String toString() {
		return "PhoneClient [model=" + model + ", sdkVersion=" + sdkVersion
				+ ", ip=" + ip + ", state=" + state + ", session=" + session
				+ "]";
	}
}
