package cn.stormbirds.stormim.imserver.config;


import cn.stormbirds.stormim.imserver.channel.ImProtocolInitalizer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

@Configuration
public class NettyConfig {

    //读取yml中配置 
    @Value("${stim.boss.thread.count}")
    private int bossCount;

    @Value("${stim.worker.thread.count}")
    private int workerCount;

    @Value("${stim.tcp.port}")
    private int tcpPort;

    @Value("${stim.tcp.nodelay}")
    private boolean tcpNoDelay;

    @Value("${stim.so.keepalive}")
    private boolean keepAlive;

    @Value("${stim.so.backlog}")
    private int backlog;

    @Autowired
    @Qualifier("imProtocolInitializer")
    private ImProtocolInitalizer protocolInitalizer;

    //bootstrap配置
    @SuppressWarnings("unchecked")
    @Bean(name = "serverBootstrap")
    public ServerBootstrap bootstrap() {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup(), workerGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(protocolInitalizer);

        //设置TCP参数
        //1.链接缓冲池的大小（ServerSocketChannel的设置）
        bootstrap.option(ChannelOption.SO_BACKLOG, backlog);
        //维持链接的活跃，清除死链接(SocketChannel的设置)
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, keepAlive);
        //关闭延迟发送
        bootstrap.childOption(ChannelOption.TCP_NODELAY, tcpNoDelay);
        return bootstrap;
    }

    @Bean(name = "bossGroup", destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup bossGroup() {
        return new NioEventLoopGroup(bossCount);
    }

    @Bean(name = "workerGroup", destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup workerGroup() {
        return new NioEventLoopGroup(workerCount);
    }

    @Bean(name = "tcpSocketAddress")
    public InetSocketAddress tcpPort() {
        return new InetSocketAddress(tcpPort);
    }

    @Bean(name = "stringEncoder")
    public StringEncoder stringEncoder() {
        return new StringEncoder();
    }

    @Bean(name = "stringDecoder")
    public StringDecoder stringDecoder() {
        return new StringDecoder();
    }

//    /**
//     * Necessary to make the Value annotations work.
//     *
//     * @return
//     */
//    @Bean
//    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
//        return new PropertySourcesPlaceholderConfigurer();
//    }
}