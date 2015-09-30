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

    public static void unbindTransactionId() {
        if( ThreadLocalManager.hasResource(KEY_TRANID) )
            ThreadLocalManager.unbindResource(KEY_TRANID);
        else {
            if( log.isDebugEnabled() )
                // 多个sessionFactory时存在这种情况
                log.debug("transactionId已经被取消绑定");
        }
    }

    public static String getTransactionId() {
        String transId = (String)ThreadLocalManager.getResource(KEY_TRANID);
        if( null == transId ) {
            transId = UUIDUtil.getRandomUUID();
            if( log.isDebugEnabled() )
                log.debug("自动提交食物，产生一个信息的transactionId:" + transId);
        }
        return (String)transId;
    }

}
