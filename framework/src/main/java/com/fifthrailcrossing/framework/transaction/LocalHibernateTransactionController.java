/*
 * Created on 2015-9-29
 */
package com.fifthrailcrossing.framework.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fifthrailcrossing.framework.hibernate.ThreadLocalManager;

/**
 * @author zhangjz<a href="mailto:zhangjz@chsi.com.cn">zhangjz</a>
 * @version $Id$
 */

public class LocalHibernateTransactionController {
    private final static String KEY = "sessionsHolder_#!$@)ijeTE";
    protected static Logger log = LoggerFactory.getLogger(LocalHibernateTransactionController.class);

    public static SessionsInTransactionHolder getTransactionHolder() {
        return (SessionsInTransactionHolder)ThreadLocalManager.getResource(KEY);
    }

    public static void beginTransaction() {
        SessionsInTransactionHolder holder = getTransactionHolder();

    }
}
