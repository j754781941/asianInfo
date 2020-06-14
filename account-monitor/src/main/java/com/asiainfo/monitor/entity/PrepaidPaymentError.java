package com.asiainfo.monitor.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class PrepaidPaymentError implements Serializable {
    private static final long serialVersionUID = 3595239705447651497L;

    private String prepaidErrorDate;
    private BigDecimal prepaidErrorCount;

    public PrepaidPaymentError(String prepaidErrorDate, BigDecimal prepaidErrorCount) {
        this.prepaidErrorDate = prepaidErrorDate;
        this.prepaidErrorCount = prepaidErrorCount;
    }

    public PrepaidPaymentError() {
    }

    public String getPrepaidErrorDate() {
        return prepaidErrorDate;
    }

    public void setPrepaidErrorDate(String prepaidErrorDate) {
        this.prepaidErrorDate = prepaidErrorDate;
    }

    public BigDecimal getPrepaidErrorCount() {
        return prepaidErrorCount;
    }

    public void setPrepaidErrorCount(BigDecimal prepaidErrorCount) {
        this.prepaidErrorCount = prepaidErrorCount;
    }
}
