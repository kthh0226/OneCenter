package cn.acooo.onecenter.core.netty;

import java.util.Arrays;
/**
 * 包协议：2byte(messageType) + 4byte(magic number) + 2byte(总长度) + byte[](protobuf object，len=总长度-8)
 * 包数据最大32k
 * @author bear
 *
 */
public class KMessage {
	public final static int HEAD_SIZE = 8;

	public KMessage(int messageType,byte[] data){
		this.magicNumber = messageType * 2;//用户数据加密，或者携带其他信息
		this.messageType = messageType;
		this.length = data.length + KMessage.HEAD_SIZE;
		this.data = data;
	}
	
	private int magicNumber;
	private int messageType;
	private int length;
	private byte[] data;
	
	public int getMagicNumber() {
		return magicNumber;
	}
	public void setMagicNumber(int magicNumber) {
		this.magicNumber = magicNumber;
	}
	public int getMessageType() {
		return messageType;
	}
	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		return "KMessage [magicNumber=" + magicNumber + ", messageType="
				+ messageType + ", length=" + length + ", data="
				+ Arrays.toString(data) + "]";
	}
	public final static boolean validMagicNumber(int messageType,int magicNumber){
		return KMessage.generateMagicNumber(messageType) == magicNumber;
	}
	public final static int generateMagicNumber(int messageType){
		return messageType * 2;
	}
}
