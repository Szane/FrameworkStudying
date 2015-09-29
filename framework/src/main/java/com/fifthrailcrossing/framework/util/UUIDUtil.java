/*
 * Created on 2015-9-29
 */
package com.fifthrailcrossing.framework.util;

import java.util.UUID;
import org.apache.commons.lang.StringUtils;

/**
 * 获取UUID
 * 
 * @author zhangjz<a href="mailto:zhangjz@chsi.com.cn">zhangjz</a>
 * @version $Id$
 */

public class UUIDUtil {

    static public String getRandomUUID() {
        String uuid = UUID.randomUUID().toString();
        uuid = StringUtils.replace(uuid, "-", "");
        return uuid;
    }
}
