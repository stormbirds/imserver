package cn.stormbirds.stormim.imserver.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @ Description com.snhanyue.banbancommon.utils
 * @ Author StormBirds
 * @ Email xbaojun@gmail.com
 * @ Date 2019/5/16 11:20
 */


public class DateUtils {
    /**
     * LocalDateTime 转 Date工具类
     * @param localDateTime
     * @return Date
     */
    public static Date LocalDateTimeToUdate(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Date.from(instant);
    }
}
