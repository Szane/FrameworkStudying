/*
 * Created on 2015-9-29
 */
package com.fifthrailcrossing.framework.hibernate;

import javax.naming.InitialContext;
import javax.transaction.Synchronization;
import javax.transaction.TransactionManager;
import javax.transaction.TransactionSynchronizationRegistry;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fifthrailcrossing.framework.exception.DAOException;

/**
 * 
 * This class can used in multiserver envionment Example: <code>   
 *   public void businessMethod()throws BusinessException {
 *      Object identity = null; 
 *      try {
 *          identity = HibernateSession.createSession(sessionFactory);
 *         //Execute business 
 *      }catch (Exception e){
 *        throw new BusinessException ("Business Exception");
 *      } finally {
 *        HibernateSession.closeSession(sessionFactory, identity);
 *      }
 *    } 
 *   </code>
 * 
 * @author zhangjz<a href="mailto:zhangjz@chsi.com.cn">zhangjz</a>
 * @version $Id$
 */

public class HibernateSession {
    private static final Logger logger = LoggerFactory.getLogger(HibernateSession.class);

    /** The constants for describing the ownerships **/
    private static final Owner trueOwener = new Owner(true);
    private static final Owner fakeOwener = new Owner(false);

    /**
     * Internal class , for handling the identity. Hidden for the developers
     */
    protected static class Owner {
        public Owner(boolean identity) {
            this.identity = identity;
        }

        boolean identity = false;
    }

    public static void createTxManagedSession(SessionFactory sessionFactory) throws HibernateException {
        SessionHolder sessionHolder = getSessionHolder(sessionFactory);
        if( null != sessionHolder ) {
            if( logger.isDebugEnabled() ) {
                logger.debug("Session Found - Give a Fake identity");
            }
        }else{
            if(logger.isDebugEnabled()){
                logger.debug("No Session Found - Create and give the identity");
            }
            Session session = null;
            try{
                session = sessionFactory.openSession()
            }catch (HibernateException e){
                throw new DAOException("Create session error!",e);
            }
            sessionHolder = new SessionHolder(session);
            bindSessionHolder(sessionFactory,sessionHolder);
            prepareSession()
        }
    }

    public static void prepareSession(SessionFactory sesionFactory) throws HibernateException {
        InitialContext context = null;
        try{
            context = new InitialContext();
            TransactionManager tm = (TransactionManager)context.lookup("java:/TransactionManager");
            tm.getTransaction().registerSynchronization(new TransactionSynch()) {
                
        }
        
    }

    public static void closeSession(SessionFactory sessionFactory, Object ownership, boolean flush) {
        if( ((Owner)ownership).identity ) {
            if( logger.isDebugEnabled() ) {
                logger.debug("Identity is accepted. Now closing the session");
            }
        }
    }

    public static Session getSession(SessionFactory sessionFactory, boolean allowCreate) {
        SessionHolder sessionHolder = getSessionHolder(sessionFactory);
        if( sessionHolder != null ) {
            return sessionHolder.getSession();
        }
        if( allowCreate ) {
            Session session = null;
            try {
                session = sessionFactory.openSession();
            } catch (HibernateException e) {
                throw new DAOException("Create session error!", e);
            }
            sessionHolder = new SessionHolder(session);
            bindSessionHolder(sessionFactory, sessionHolder);
            return session;
        } else {
            throw new DAOException("Create session not allowed");
        }
    }

    /**
     * 返回当前session，如果不存在，说明没有按照规定的调用方式调用
     * 
     * @param sessionFactory
     * @return
     */
    public static Session getSession(SessionFactory sessionFactory) {
        return getSession(sessionFactory, false);
    }

    private static SessionHolder getSessionHolder(SessionFactory sessionFactory) {
        String key = getSessionFactoryBindKey(sessionFactory);
        SessionHolder sessionHolder = (SessionHolder)ThreadLocalManager.getResource(key);
        return sessionHolder;
    }

    private static void bindSessionHolder(SessionFactory sessionFactory, SessionHolder sessionHolder) {
        String key = getSessionFactoryBindKey(sessionFactory);
        ThreadLocalManager.bindResource(key, sessionHolder);
    }

    protected static String getSessionFactoryBindKey(SessionFactory sessionFactory) {
        return sessionFactory.toString();
    }

}
