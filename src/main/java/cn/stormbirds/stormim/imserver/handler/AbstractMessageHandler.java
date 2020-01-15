package cn.stormbirds.stormim.imserver.handler;


import cn.stormbirds.stormim.imserver.protobuf.MessageProtobuf;
import io.netty.channel.ChannelHandlerContext;

/**
  * Copyright (c) 小宝 @ 2019
  *
  * @ Package Name:    cn.stormbirds.stimlib.im.handler
  * @ Author：         stormbirds
  * @ Email：          xbaojun@gmail.com
  * @ Created At：     2019/5/13 11:21
  * @ Description：    抽象的MessageHandler
  *
  */
public abstract class AbstractMessageHandler implements IMessageHandler {

    @Override
    public void execute(ChannelHandlerContext ctx, MessageProtobuf.Msg message) {
        action(ctx,message);
    }

    protected abstract void action(ChannelHandlerContext ctx, MessageProtobuf.Msg message);
}