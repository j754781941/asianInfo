package com.asiainfo.monitor.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 营业前台（CRM）
 * @author Dekun Wang
 */
public class CRMInfo implements Serializable {

    private static final long serialVersionUID = -8369366338020007628L;
    private String crmDate;
    private BigDecimal crmCount;

    public CRMInfo(String crmDate, BigDecimal crmCount) {
        this.crmDate = crmDate;
        this.crmCount = crmCount;
    }

    public CRMInfo() {
    }

    public String getCrmDate() {
        return crmDate;
    }

    public void setCrmDate(String crmDate) {
        this.crmDate = crmDate;
    }

    public BigDecimal getCrmCount() {
        return crmCount;
    }

    public void setCrmCount(BigDecimal crmCount) {
        this.crmCount = crmCount;
    }
}
