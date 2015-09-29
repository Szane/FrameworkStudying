/*
 * Created on 2015-9-29
 */
package com.fifthrailcrossing.framework.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fifthrailcrossing.framework.hibernate.ThreadLocalManager;
import com.fifthrailcrossing.framework.util.UUIDUtil;

/**
 * @author zhangjz<a href="mailto:zhangjz@chsi.com.cn">zhangjz</a>
 * @version $Id$
 */

public class TransactionIdHelper {
    private static final String KEY_TRANID = "tranactionId#@#Gsw$903";
    private static final Logger log = LoggerFactory.getLogger(TransactionIdHelper.class);

    /**
     * 绑定一个代表此次事务的uuid
     */
    public static void bindTranactionId() {
        if( ThreadLocalManager.hasResource(KEY_TRANID) ) {
            if( log.isWarnEnabled() )
                log.warn("transactionId已存在");
            ThreadLocalManager.unbindResource(KEY_TRANID);
        }
        String tranId = UUIDUtil.getRandomUUID();
        ThreadLocalManager.bindResource(KEY_TRANID, tranId);
    }

    public static void bindTransactionIdIfNoExists() {
        if( !ThreadLocalManager.hasResource(KEY_TRANID) ) {
            String tranId = UUIDUtil.getRandomUUID();
            ThreadLocalManager.bindResource(KEY_TRANID, tranId);
        } else {
            if( log.isDebugEnabled() )
                log.debug("transactionId绑定已经存在");
        }
    }
}
