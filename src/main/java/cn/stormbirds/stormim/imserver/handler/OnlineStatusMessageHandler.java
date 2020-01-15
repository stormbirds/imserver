package cn.stormbirds.stormim.imserver.handler;


import cn.stormbirds.stormim.imserver.protobuf.MessageProtobuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * @ Description com.snhanyue.banbancommon.im.handler
 * @ Author StormBirds
 * @ Email xbaojun@gmail.com
 * @ Date 2019/5/14 17:59
 */


public class OnlineStatusMessageHandler extends AbstractMessageHandler {
    @Override
    protected void action(ChannelHandlerContext ctx, MessageProtobuf.Msg message) {
        System.out.println("收到来自查询的消息：" + message);
        String fromId = message.getHead().getFromId();
    }
}
