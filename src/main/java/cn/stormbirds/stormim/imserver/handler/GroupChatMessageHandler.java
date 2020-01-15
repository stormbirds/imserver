package cn.stormbirds.stormim.imserver.handler;

import cn.stormbirds.stormim.imserver.bean.MessageType;
import cn.stormbirds.stormim.imserver.channel.ChannelContainer;
import cn.stormbirds.stormim.imserver.config.IMServerConfig;
import cn.stormbirds.stormim.imserver.protobuf.MessageProtobuf;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

/**
 * @ Description 群聊消息处理器
 * @ Author StormBirds
 * @ Email xbaojun@gmail.com
 * @ Date 2019/5/14 09:40
 */

@Service
public class GroupChatMessageHandler extends AbstractMessageHandler {
    private final Log logger = LogFactory.getLog(this.getClass());

    @Override
    protected void action(ChannelHandlerContext ctx, MessageProtobuf.Msg message) {
        logger.info("收到来自客户端的群聊消息：" + message);
        String fromId = message.getHead().getFromId();
        MessageProtobuf.Msg.Builder sentReportMsgBuilder = MessageProtobuf.Msg.newBuilder();
        MessageProtobuf.Head.Builder sentReportHeadBuilder = MessageProtobuf.Head.newBuilder();
        sentReportHeadBuilder.setMsgId(message.getHead().getMsgId());
        sentReportHeadBuilder.setMsgType(MessageType.SERVER_MSG_SENT_STATUS_REPORT.getMsgType());
        sentReportHeadBuilder.setTimestamp(System.currentTimeMillis());
        sentReportHeadBuilder.setStatusReport(IMServerConfig.DEFAULT_REPORT_SERVER_SEND_MSG_SUCCESSFUL);
        sentReportMsgBuilder.setHead(sentReportHeadBuilder.build());
        ChannelContainer.getInstance().getActiveChannelByUserId(fromId).getChannel().writeAndFlush(sentReportMsgBuilder.build());

        // 同时转发消息到群成员


        MessageProtobuf.Msg finalMessage = message;
        ChannelContainer.getInstance().CHANNELS
                .values()
                .stream()
                .filter(nettyChannel -> ( !nettyChannel.getUserId().equals(fromId)))
                .forEach(nettyChannel -> nettyChannel.getChannel().writeAndFlush(finalMessage));
    }
}
