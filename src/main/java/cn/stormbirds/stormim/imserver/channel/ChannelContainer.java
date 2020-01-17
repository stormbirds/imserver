package cn.stormbirds.stormim.imserver.channel;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @ Description 信道容器，保存所有通话信道
 * @ Author StormBirds
 * @ Email xbaojun@gmail.com
 * @ Date 2019/5/13 17:15
 *
 */
@Slf4j
@Component
public class ChannelContainer {

    private static final ChannelContainer INSTANCE = new ChannelContainer();

    public static ChannelContainer getInstance() {
        return INSTANCE;
    }

    public final Map<String, NettyChannel> CHANNELS = new ConcurrentHashMap<>();
    /**
     * 非注册用户数
     */
    private static AtomicInteger NonRegisteredCount = new AtomicInteger(0);
    /**
     * 注册用户数
     */
    private static AtomicInteger RegisteredCount = new AtomicInteger(0);

    public void saveChannel(NettyChannel channel) {
        if (channel == null) {
            return;
        }
        CHANNELS.put(channel.getChannelId(), channel);
        log.info("总连接数："+CHANNELS.size());
        if(channel.getUserId().length()>20)
            NonRegisteredCount.incrementAndGet();
        else RegisteredCount.incrementAndGet();
    }

    public NettyChannel removeChannelIfConnectNoActive(Channel channel) {
        if (channel == null) {
            return null;
        }

        String channelId = channel.id().toString();

        return removeChannelIfConnectNoActive(channelId);
    }

    /**
     * Netty通道关闭时从通道管理器移除，并判断用户是否有未正常结束队列
     * @param channelId 通道ID
     * @return 通道信息
     */
    public NettyChannel removeChannelIfConnectNoActive(String channelId) {
        if (CHANNELS.containsKey(channelId) && !CHANNELS.get(channelId).isActive()) {
            if(CHANNELS.get(channelId).getUserId().length()>20){//加入用户ID长度超过20则为未注册用户
                NonRegisteredCount.decrementAndGet();
            }else{
                RegisteredCount.decrementAndGet();

            }

            return CHANNELS.remove(channelId);
        }

        return null;
    }

    public String getUserIdByChannel(Channel channel) {
        return getUserIdByChannel(channel.id().toString());
    }

    public String getUserIdByChannel(String channelId) {
        if (CHANNELS.containsKey(channelId)) {
            return CHANNELS.get(channelId).getUserId();
        }

        return null;
    }

    public NettyChannel getActiveChannelByUserId(String userId) {
        for (Map.Entry<String, NettyChannel> entry : CHANNELS.entrySet()) {
            if (entry.getValue().getUserId().equals(userId) && entry.getValue().isActive()) {
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * 获取所有在线用户数量
     * @return 用户数 int型
     */
    public int getCountActiveChannel(){
        return CHANNELS.size();
    }

    /**
     * 获取非注册用户在线数量
     * @return 用户数 int型
     */
    public int getNonRegisteredActiveChannel(){
        return NonRegisteredCount.get();
    }

    /**
     * 获取注册用户在线数量
     * @return 用户数 int型
     */
    public int getRegisteredActiveChannel(){
        return RegisteredCount.get();
    }
}