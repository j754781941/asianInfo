package com.asiainfo.monitor.entity;


import java.io.Serializable;

/**
 * @author hy
 * @blame Development Group
 * @date createDate:2019/12/24/024 14:44
 * @since
 */

public class SendRequest implements Serializable {
    private static final long serialVersionUID = -5810879337791698045L;
    private Integer groupId;
    private String logContent;

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getLogContent() {
        return logContent;
    }

    public void setLogContent(String logContent) {
        this.logContent = logContent;
    }
}
