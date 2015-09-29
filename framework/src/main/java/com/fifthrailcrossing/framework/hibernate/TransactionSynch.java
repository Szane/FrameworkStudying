/*
 * Created on 2015-9-29
 */
package com.fifthrailcrossing.framework.hibernate;

import javax.transaction.Synchronization;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fifthrailcrossing.framework.exception.DAOException;

/**
 * @author zhangjz<a href="mailto:zhangjz@chsi.com.cn">zhangjz</a>
 * @version $Id$
 */

public class TransactionSynch implements Synchronization {
    private static Logger log = LoggerFactory.getLogger(TransactionSynch.class);

    SessionFactory sessionFactory = null;

    public TransactionSynch(SessionFactory sessionFactory) {
        if( null == sessionFactory ) {
            throw new IllegalArgumentException("SessionFactory to sychronize cannot be null");
        }
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void beforeCompletion() {
        Session session = HibernateSession.getSession(sessionFactory);
        if( !session.isOpen() ) {
            log.warn("Session already closed");
        } else {
            try {
                log.trace("Flushing Session");
                session.flush();
            } catch (Throwable t) {
                log.warn("Error flushing session");
                throw new DAOException("Error flushing session", t);
            }
        }
    }

    @Override
    public void afterCompletion(int status) {
        
    }

}
