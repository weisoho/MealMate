package org.soho.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wesoho
 * @version 1.0
 * @description: TODO
 * @date 2024/11/5 0:41
 */
public class DateUtil {
    public static String now(String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }
}
