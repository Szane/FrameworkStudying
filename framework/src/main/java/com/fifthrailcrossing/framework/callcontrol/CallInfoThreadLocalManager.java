/*
 * Created on 2015-9-21
 */
package com.fifthrailcrossing.framework.callcontrol;

import com.fifthrailcrossing.framework.callcontrol.model.CallInfo;
import com.fifthrailcrossing.framework.hibernate.ThreadLocalManager;

/**
 * 辅助类
 * 
 * @author zhangjz<a href="mailto:zhangjz@chsi.com.cn">zhangjz</a>
 * @version $Id$
 */

public class CallInfoThreadLocalManager {
    public static final String CALL_INFO = "callInfo#DQH87DcseZa";

    public static CallInfo getCallInfo() {
        return (CallInfo)ThreadLocalManager.getResource(CALL_INFO);
    }

    public static void bindCallInfo(CallInfo callInfo) {
        if( null == callInfo )
            throw new IllegalArgumentException();
        ThreadLocalManager.bindResource(CALL_INFO, callInfo);
    }

    public static void unbind() {
        ThreadLocalManager.unbindResource(CALL_INFO);
    }
}
