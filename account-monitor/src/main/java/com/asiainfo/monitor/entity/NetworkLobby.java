package com.asiainfo.monitor.entity;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 网厅
 * @author Dekun Wang
 */
public class NetworkLobby implements Serializable {
    private static final long serialVersionUID = 171863377259326622L;
    private String networkDate;
    private BigDecimal networkCount;

    public NetworkLobby() {
    }

    public NetworkLobby(String networkDate, BigDecimal networkCount) {
        this.networkDate = networkDate;
        this.networkCount = networkCount;
    }

    public String getNetworkDate() {
        return networkDate;
    }

    public void setNetworkDate(String networkDate) {
        this.networkDate = networkDate;
    }

    public BigDecimal getNetworkCount() {
        return networkCount;
    }

    public void setNetworkCount(BigDecimal networkCount) {
        this.networkCount = networkCount;
    }
}
