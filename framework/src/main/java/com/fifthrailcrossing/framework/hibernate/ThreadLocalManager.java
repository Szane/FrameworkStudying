/*
 * Created on 2015-9-21
 */
package com.fifthrailcrossing.framework.hibernate;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhangjz<a href="mailto:zhangjz@chsi.com.cn">zhangjz</a>
 * @version $Id$
 */

public class ThreadLocalManager {
    private static final Logger logger = LoggerFactory.getLogger(ThreadLocalManager.class);

    private static ThreadLocal<?> resources = new ThreadLocal<Object>() {
        protected Object initialValue() {
            return new HashMap<Object, Object>();
        }
    };

    @SuppressWarnings("unchecked")
    public static Map<Object, Object> getResourceMap() {
        return (Map<Object, Object>)resources.get();
    }

    public static Object getResource(Object key) {
        Object value = getResourceMap().get(key);
        if( value != null && logger.isDebugEnabled() ) {
            logger.debug("Retrieved value [" + value + "] for key [" + key + "] bound to thread ["
                    + Thread.currentThread().getName() + "]");
        }
        return value;
    }

    /**
     * Check if there is a resource for the given key bound to the current thread.
     * 
     * @param key
     *            key to check
     * @return if there is a value bound to the current thread
     */
    public static boolean hasResource(Object key) {
        return getResourceMap().containsKey(key);
    }

    public static void bindResource(Object key, Object value) throws IllegalStateException {
        if( hasResource(key) ) {
            throw new IllegalStateException("Already a value for key [" + key + "] bound to thread");
        }
        getResourceMap().put(key, value);
        if( logger.isDebugEnabled() ) {
            logger.debug("Bound value [" + value + "] for key [" + key + "] to thread [" + Thread.currentThread().getName()
                    + "]");
        }
    }

    public static Object unbindResource(Object key) throws IllegalStateException {
        if( !hasResource(key) ) {
            throw new IllegalStateException("No value for key [" + key + "] bound to thread");
        }
        Object value = getResourceMap().remove(key);
        if( logger.isDebugEnabled() ) {
            logger.debug("Removed value [" + value + "] for key [" + key + "] from thread ["
                    + Thread.currentThread().getName() + "]");
        }
        return value;
    }

}
