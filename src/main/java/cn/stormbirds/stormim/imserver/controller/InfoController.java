package cn.stormbirds.stormim.imserver.controller;

import cn.stormbirds.stormim.imserver.channel.ChannelContainer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * cn.stormbirds.stormim.imserver.controller
 * </p>
 *
 * @author StormBirds Email：xbaojun@gmail.com
 * @since 2020/1/17 10:27
 */
@RestController
@RequestMapping("/api/v1")
public class InfoController {

    @GetMapping(value = "/getConnectNum")
    public String getConnectNum(){
        return String.valueOf(ChannelContainer.getInstance().getCountActiveChannel()) + " Registered " +String.valueOf(ChannelContainer.getInstance().getRegisteredActiveChannel())
                + " NonRegistered " +String.valueOf(ChannelContainer.getInstance().getNonRegisteredActiveChannel());
    }
}
