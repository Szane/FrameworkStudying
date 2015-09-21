/*
 * Created on 2015-9-21
 */
package com.fifthrailcrossing.framework.callcontrol.dic;

/**
 * 登录验证方式分类
 * 
 * @author zhangjz<a href="mailto:zhangjz@chsi.com.cn">zhangjz</a>
 * @version $Id$
 */

public enum AuthType {
    PLAIN_BASIC, // username + password
    CODE_IMG, // PLAIN_BASIC + image code
    CODE_SMS, // PLAIN_BASIC + mphone code
    IP_WLIST; // PLAIN_BASIC + IP white list
}
