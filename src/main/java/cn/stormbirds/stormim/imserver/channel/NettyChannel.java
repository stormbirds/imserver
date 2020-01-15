package cn.stormbirds.stormim.imserver.channel;

import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

/**
 *
 * @ Description 定义用户信道，类似Session
 * @ Author StormBirds
 * @ Email xbaojun@gmail.com
 * @ Date 2019/5/13 17:24
 *
 */

@Component
public class NettyChannel {

    private String userId;
    private Channel channel;

    public NettyChannel(String userId, Channel channel) {
        this.userId = userId;
        this.channel = channel;
    }

    public NettyChannel() {
    }

    public String getChannelId() {
        return channel.id().toString();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public boolean isActive() {
        return channel.isActive();
    }
}