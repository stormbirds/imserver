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
  * Copyright (c) 小宝 @ 2019
  *
  * @ Package Name:    cn.stormbirds.stimlib.im.handler
  * @ Author：         stormbirds
  * @ Email：          xbaojun@gmail.com
  * @ Created At：     2019/5/13 11:24
  * @ Description：    单聊消息处理实现
  *
  */
@Service
public class SingleChatMessageHandler extends AbstractMessageHandler {

    private final Log logger = LogFactory.getLog(this.getClass());

    @Override
    protected void action(ChannelHandlerContext ctx, MessageProtobuf.Msg message) {
        logger.info("收到来自单聊的消息：" + message);
        // 收到2001消息，返回给客户端消息发送状态报告
        String fromIdSingle = message.getHead().getFromId();
        MessageProtobuf.Msg.Builder sentReportMsgBuilder = MessageProtobuf.Msg.newBuilder();
        MessageProtobuf.Head.Builder sentReportHeadBuilder = MessageProtobuf.Head.newBuilder();
        sentReportHeadBuilder.setMsgId(message.getHead().getMsgId());
        sentReportHeadBuilder.setMsgType(MessageType.SERVER_MSG_SENT_STATUS_REPORT.getMsgType());
        sentReportHeadBuilder.setTimestamp(System.currentTimeMillis());
        sentReportHeadBuilder.setStatusReport(IMServerConfig.DEFAULT_REPORT_SERVER_SEND_MSG_SUCCESSFUL);
        sentReportMsgBuilder.setHead(sentReportHeadBuilder.build());
        ChannelContainer.getInstance().getActiveChannelByUserId(fromIdSingle).getChannel().writeAndFlush(sentReportMsgBuilder.build());

        // 同时转发消息到接收方
        String toId = message.getHead().getToId();
        ChannelContainer.getInstance().getActiveChannelByUserId(toId).getChannel().writeAndFlush(message);
    }
}
