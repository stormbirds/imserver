package cn.stormbirds.stormim.imserver.handler;

import cn.stormbirds.stormim.imserver.bean.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class MessageHandlerFactory {

    private final SingleChatMessageHandler singleChatMessageHandler;
    private final GroupChatMessageHandler groupChatMessageHandler;
    private final ServerReportMessageHandler serverReportMessageHandler;

    private static final Map<Integer,IMessageHandler> HANDLERS = new HashMap<>();

    public MessageHandlerFactory(SingleChatMessageHandler singleChatMessageHandler, GroupChatMessageHandler groupChatMessageHandler, ServerReportMessageHandler serverReportMessageHandler) {
        this.singleChatMessageHandler = singleChatMessageHandler;
        this.groupChatMessageHandler = groupChatMessageHandler;
        this.serverReportMessageHandler = serverReportMessageHandler;
    }

    @PostConstruct
    public void init(){
        /** 单聊消息处理handler */
        HANDLERS.put(MessageType.SINGLE_CHAT.getMsgType(), singleChatMessageHandler);
        /** 群聊消息处理handler */
        HANDLERS.put(MessageType.GROUP_CHAT.getMsgType(), groupChatMessageHandler);

        /** 客户端发送的握手消息与心跳消息handler及客户端消息报告 统一交由系统消息报告handler处理*/
        HANDLERS.put(MessageType.HANDSHAKE.getMsgType(), serverReportMessageHandler);
        HANDLERS.put(MessageType.HEARTBEAT.getMsgType(), serverReportMessageHandler);
        HANDLERS.put(MessageType.CLIENT_MSG_RECEIVED_STATUS_REPORT.getMsgType(), serverReportMessageHandler);
    }
//
//    static {
//        /** 单聊消息处理handler */
//        HANDLERS.put(MessageType.SINGLE_CHAT.getMsgType(), singleChatMessageHandler);
//        /** 群聊消息处理handler */
//        HANDLERS.put(MessageType.GROUP_CHAT.getMsgType(), new GroupChatMessageHandler());
//
//        /** 客户端发送的握手消息与心跳消息handler 统一交由系统消息报告handler处理*/
//        HANDLERS.put(MessageType.HANDSHAKE.getMsgType(), new ServerReportMessageHandler());
//        HANDLERS.put(MessageType.HEARTBEAT.getMsgType(), new ServerReportMessageHandler());
//    }

    /**
     * 根据消息类型获取对应的处理handler
     *
     * @param msgType
     * @return
     */
    public static IMessageHandler getHandlerByMsgType(int msgType) {
        return HANDLERS.get(msgType);
    }
}