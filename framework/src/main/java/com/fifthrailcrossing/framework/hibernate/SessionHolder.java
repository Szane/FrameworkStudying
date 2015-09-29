/*
 * Created on 2015-9-29
 */
package com.fifthrailcrossing.framework.hibernate;

import org.hibernate.Session;

/**
 * @author zhangjz<a href="mailto:zhangjz@chsi.com.cn">zhangjz</a>
 * @version $Id$
 */

public class SessionHolder {
    private final Session session;

    public SessionHolder(Session session) {
        this.session = session;
    }

    public Session getSession() {
        return session;
    }
}
