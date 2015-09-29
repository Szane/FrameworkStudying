/*
 * Created on 2015-9-21
 */
package com.fifthrailcrossing.framework.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhangjz<a href="mailto:zhangjz@chsi.com.cn">zhangjz</a>
 * @version $Id$
 */

public class HibernateUtil {
    SessionFactory sessionFactory = null;
    Logger log = LoggerFactory.getLogger(getClass());

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * @param factory
     */
    public void setSessionFactory(SessionFactory factory) {
        sessionFactory = factory;
    }

}
