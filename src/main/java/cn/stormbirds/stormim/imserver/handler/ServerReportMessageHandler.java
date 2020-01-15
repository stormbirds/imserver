package cn.stormbirds.stormim.imserver.handler;

import cn.stormbirds.stormim.imserver.channel.ChannelContainer;
import cn.stormbirds.stormim.imserver.channel.NettyChannel;
import cn.stormbirds.stormim.imserver.protobuf.MessageProtobuf;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;


/**
 * @ Description 服务端报告消息处理器
 * @ Author StormBirds
 * @ Email xbaojun@gmail.com
 * @ Date 2019/5/14 09:42
 */

@Service
public class ServerReportMessageHandler extends AbstractMessageHandler {
    private final Log logger = LogFactory.getLog(this.getClass());


    @Override
    protected void action(ChannelHandlerContext ctx, MessageProtobuf.Msg message) {
        int msgType = message.getHead().getMsgType();
        switch (msgType) {
            // 握手消息
            case 1001: {
                String fromId = message.getHead().getFromId();
                JSONObject jsonObj = JSON.parseObject(message.getHead().getExtend());
                String auth_token = jsonObj.getString("token");
                JSONObject resp = new JSONObject();
                if (fromId.length() > 20) {//非注册用户不校检token直接返回握手成功
                    logger.info(String.format("非注册用户 %s 连接", fromId));
                    resp.put("status", 1);
                    ChannelContainer.getInstance().saveChannel(new NettyChannel(fromId, ctx.channel()));
                }/* else if (StringUtils.isNotEmpty(auth_token) &&
                        auth_token.startsWith(tokenHead) &&
                        jwtUtils.validateToken(auth_token.substring(tokenHead.length()),
                                jwtUtils.getUserFromToken(auth_token.substring(tokenHead.length())))) {//这里校检token合法性
                    logger.info(String.format("注册用户 %s 连接", fromId));
                    resp.put("status", 1);
                    // 握手成功后，保存用户通道
                    ChannelContainer.getInstance().saveChannel(new NettyChannel(fromId, ctx.channel()));

                } */else {
                    resp.put("status", -1);
                    ChannelContainer.getInstance().removeChannelIfConnectNoActive(ctx.channel());
                }

                message = message.toBuilder().setHead(message.getHead().toBuilder().setExtend(resp.toString()).build()).build();
                ChannelContainer.getInstance().getActiveChannelByUserId(fromId).getChannel().writeAndFlush(message);
                break;
            }

            // 心跳消息
            case 1002: {
                // 收到心跳消息，原样返回
                String fromId = message.getHead().getFromId();
                ChannelContainer.getInstance().getActiveChannelByUserId(fromId).getChannel().writeAndFlush(message);
                break;
            }

            //客户端消息报告
            case 1009:{
                logger.info(message);
                break;
            }

            default:
                break;
        }
    }
}
