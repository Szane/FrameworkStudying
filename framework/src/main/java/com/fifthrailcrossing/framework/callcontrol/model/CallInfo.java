/*
 * Created on 2015-9-21
 */
package com.fifthrailcrossing.framework.callcontrol.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fifthrailcrossing.framework.callcontrol.dic.AuthType;

/**
 * 调用者信息,此信息将绑定到thread中
 * 
 * @author zhangjz<a href="mailto:zhangjz@chsi.com.cn">zhangjz</a>
 * @version $Id$
 */

public class CallInfo implements Serializable {
    
    private static final long serialVersionUID = 3063114383426563853L;
    private String systemId;
    private String moduleId;
    private String userIp;
    private String serverIp;// 发起调用的服务器IP

    private String userId;
    private boolean pubUser;
    private String callId;// 只有在远程方法调用时才有意义
    private List<AuthType> authType = new ArrayList<AuthType>();

    public String getSystemId() {
        return systemId;
    }

    public String getModuleId() {
        return moduleId;
    }

    public String getUserIp() {
        return userIp;
    }

    public String getServerIp() {
        return serverIp;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isPubUser() {
        return pubUser;
    }

    public String getCallId() {
        return callId;
    }

    public List<AuthType> getAuthType() {
        return authType;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPubUser(boolean pubUser) {
        this.pubUser = pubUser;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public void setAuthType(List<AuthType> authType) {
        this.authType = authType;
    }

}
