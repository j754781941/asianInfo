package com.asiainfo.monitor.entity;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 预存支付计划
 * @author Dekun Wang
 */
public class PrepaidPayment implements Serializable {
    private static final long serialVersionUID = -8462404749880830742L;
    private String prepaidDate;
    private BigDecimal prepaidCount;

    public PrepaidPayment() {
    }

    public PrepaidPayment(String prepaidDate, BigDecimal prepaidCount) {
        this.prepaidDate = prepaidDate;
        this.prepaidCount = prepaidCount;
    }

    public String getPrepaidDate() {
        return prepaidDate;
    }

    public void setPrepaidDate(String prepaidDate) {
        this.prepaidDate = prepaidDate;
    }

    public BigDecimal getPrepaidCount() {
        return prepaidCount;
    }

    public void setPrepaidCount(BigDecimal prepaidCount) {
        this.prepaidCount = prepaidCount;
    }
}
