/*
 * Created on 2015-9-29
 */
package com.fifthrailcrossing.framework.transaction;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fifthrailcrossing.framework.exception.DAOException;
import com.fifthrailcrossing.framework.hibernate.HibernateSession;
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
        if( null != holder ) {
            Throwable toTrace = new Throwable();
            log.error("事务没有清理干净", toTrace);
            commit(false);
        }
        holder = new SessionsInTransactionHolder();
        ThreadLocalManager.bindResource(KEY, holder);
        TransactionIdHelper.bindTranactionId();
    }

    public static void commit(boolean commit) {
        try {
            doCommit(commit);
        } finally {
            unbindSessions();
        }
    }

    private static void unbindSessions() {
        ThreadLocalManager.unbindResource(KEY);
    }

    private static void doCommit(boolean commit) {
        SessionsInTransactionHolder holder = getTransactionHolder();
        if( (null == holder) || (0 == holder.getSessions().size()) ) {
            if( null == holder ) {
                log.error("没有对应的事务，下面的异常信息只是为了记录调用位置.", new Throwable());
            } else {
                if( log.isDebugEnabled() ) {
                    Throwable toTrace = new Throwable();
                    log.debug("没有数据库操作，因此没有对应的事务。记录调用堆栈", toTrace);
                }
                TransactionIdHelper.unbindTransactionId();
            }
        } else {
            if( commit ) {
                boolean commitError = commitAllSession(holder);
                if( commitError ) {
                    throw new DAOException("结束事务时出错");
                }
            } else
                rollbackAllSession(holder);
        }

    }

    private static boolean commitAllSession(SessionsInTransactionHolder holder) {
        boolean hasError = false;

        log.debug("准备提交事务");
        List<Session> sessions = new ArrayList<Session>(holder.getSessions().size());

        hasError = flushAllSession(sessions);
        if( hasError ) {
            rollbackAllSession(holder);
        } else {
            hasError = flushAllSession(holder.getSessions());
            if( hasError )
                rollbackAllSession(holder);
        }
        if( !hasError ) {
            int errorTimes = 0;
            for( Session session : holder.getSessions() ) {
                SessionFactory sessionFactory = null;
                try {
                    sessionFactory = session.getSessionFactory();
                    if( !session.isOpen() ) {
                        log.error("session已经关闭");
                        continue;
                    }
                    try {
                        session.getTransaction().commit();
                        log.debug("事务提交完毕");
                    } catch (Throwable e) {
                        log.error("事务提交异常", e);
                        errorTimes++;
                        hasError = true;
                        try {
                            session.getTransaction().rollback();
                            log.debug("事务回滚完毕");
                        } catch (Throwable e2) {
                            log.error("Rollback session error!", e2);
                        }
                    } finally {
                        try {
                            session.close();
                            if( log.isDebugEnabled() )
                                log.debug("关闭session");
                        } catch (Throwable e) {
                            log.error("Close session error!", e);
                            hasError = true;
                        }
                    }
                } finally {
                    HibernateSession.unbind(sessionFactory);
                }
            }
            if( hasError && holder.getSessions().size() > errorTimes )
                log.error("多个session时提交事务出错，可能存在事务不一致.{}/{}", holder.getSessions().size(), errorTimes);
        }
        return hasError;
    }

    private static boolean flushAllSession(List<Session> sessions) {
        boolean hasError = false;
        for( Session session : sessions ) {
            if( !session.isOpen() ) {
                log.error("session已经关闭");
            } else {
                try {
                    if( session.isDirty() )
                        session.flush();
                } catch (Exception e) {
                    log.error("Flush session error", e);
                    hasError = true;
                }
            }
        }
        return hasError;
    }

    private static void rollbackAllSession(SessionsInTransactionHolder holder) {
        if( log.isDebugEnabled() )
            log.debug("准备回滚事务");
        for( Session session : holder.getSessions() ) {
            SessionFactory sessionFactory = null;
            try {
                if( !session.isOpen() ) {
                    log.error("session已经关闭");
                } else {
                    try {
                        try {
                            session.getTransaction().rollback();
                            if( log.isDebugEnabled() )
                                log.debug("事务回滚完毕");
                        } catch (Throwable e) {
                            log.error("Rollback session error!", e);
                        }

                        try {
                            session.close();
                            if( log.isDebugEnabled() )
                                log.debug("关闭session");
                        } catch (Exception e) {
                            log.error("Close session error!", e);
                        }
                    } catch (Throwable e) {
                        // 不直接抛出异常时为了每一个session对应的finally里面的语句都能得到执行
                        log.error("结束事务时出错", e);
                    }
                }
            } finally {
                HibernateSession.unbind(sessionFactory);
            }

        }
    }
}
