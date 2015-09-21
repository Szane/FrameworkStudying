/*
 * Created on 2015-9-21
 */
package com.fifthrailcrossing.framework.callcontrol.provider;

/**
 * 认证信息提供者
 * @author zhangjz<a href="mailto:zhangjz@chsi.com.cn">zhangjz</a>
 * @version $Id$
 */

public interface AuthInfoProvider {

    String getUsername();

    String getPassword();
}
