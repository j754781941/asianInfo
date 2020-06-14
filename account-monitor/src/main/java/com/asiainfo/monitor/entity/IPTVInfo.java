package com.asiainfo.monitor.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * IPTV
 * @author Dekun Wang
 */
public class IPTVInfo implements Serializable {

    private static final long serialVersionUID = -1792012589149257419L;
    private String iptvDate;
    private BigDecimal iptvCount;

    public IPTVInfo() {
    }

    public IPTVInfo(String iptvDate, BigDecimal iptvCount) {
        this.iptvDate = iptvDate;
        this.iptvCount = iptvCount;
    }

    public String getIptvDate() {
        return iptvDate;
    }

    public void setIptvDate(String iptvDate) {
        this.iptvDate = iptvDate;
    }

    public BigDecimal getIptvCount() {
        return iptvCount;
    }

    public void setIptvCount(BigDecimal iptvCount) {
        this.iptvCount = iptvCount;
    }
}
