package com.asiainfo.monitor.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class PaymentComplexError implements Serializable {

    private static final long serialVersionUID = 8142906088253596895L;
    private String complexErrorDate;
    private BigDecimal complexErrorCount;

    public PaymentComplexError() {
    }

    public PaymentComplexError(String complexErrorDate, BigDecimal complexErrorCount) {
        this.complexErrorDate = complexErrorDate;
        this.complexErrorCount = complexErrorCount;
    }

    public String getComplexErrorDate() {
        return complexErrorDate;
    }

    public void setComplexErrorDate(String complexErrorDate) {
        this.complexErrorDate = complexErrorDate;
    }

    public BigDecimal getComplexErrorCount() {
        return complexErrorCount;
    }

    public void setComplexErrorCount(BigDecimal complexErrorCount) {
        this.complexErrorCount = complexErrorCount;
    }
}
