package cn.stormbirds.stormim.imserver.handler;


import cn.stormbirds.stormim.imserver.protobuf.MessageProtobuf;
import io.netty.channel.ChannelHandlerContext;

public interface IMessageHandler {

    void execute(ChannelHandlerContext ctx, MessageProtobuf.Msg message);
}