/*
 * Created on 2015-9-29
 */
package com.fifthrailcrossing.framework.exception;

/**
 * @author zhangjz<a href="mailto:zhangjz@chsi.com.cn">zhangjz</a>
 * @version $Id$
 */

public class DAOException extends RuntimeException {
    private static final long serialVersionUID = 3115550600830215791L;

    /**
     * 
     */
    public DAOException() {
        super();
    }

    /**
     * @param arg0
     */
    public DAOException(String arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     * @param arg1
     */
    public DAOException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    /**
     * @param arg0
     */
    public DAOException(Throwable arg0) {
        super(arg0);
    }
}
