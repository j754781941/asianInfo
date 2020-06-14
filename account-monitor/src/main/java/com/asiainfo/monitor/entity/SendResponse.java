package com.asiainfo.monitor.entity;

import java.io.Serializable;

/**
 * @author hy
 * @blame Development Group
 * @date createDate:2019/12/24/024 14:52
 * @since
 */

public class SendResponse implements Serializable {
    private static final long serialVersionUID = -2682940454941286083L;
    private Integer code;
    private String msg;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
