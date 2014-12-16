package cn.acooo.onecenter.core.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class KMessageEncoder extends MessageToByteEncoder<KMessage>{
	
	@Override
	protected void encode(ChannelHandlerContext ctx, KMessage msg, ByteBuf out)
			throws Exception {
		out.writeShort(msg.getMessageType());  // message type
		out.writeShort(msg.getMessageCode());
        out.writeInt(msg.getMagicNumber()); // magic number
        out.writeShort(msg.getLength());// data length
        out.writeBytes(msg.getData());      // data
	}

}
