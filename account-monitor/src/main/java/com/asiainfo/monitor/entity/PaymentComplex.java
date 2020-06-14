package com.asiainfo.monitor.entity;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 缴费复机异常
 * @author Dekun Wang
 */
public class PaymentComplex implements Serializable {

    private static final long serialVersionUID = -4118149051512487531L;
    private String complexDate;
    private BigDecimal complexCount;

    public PaymentComplex() {
    }

    public PaymentComplex(String complexDate, BigDecimal complexCount) {
        this.complexDate = complexDate;
        this.complexCount = complexCount;
    }

    public String getComplexDate() {
        return complexDate;
    }

    public void setComplexDate(String complexDate) {
        this.complexDate = complexDate;
    }

    public BigDecimal getComplexCount() {
        return complexCount;
    }

    public void setComplexCount(BigDecimal complexCount) {
        this.complexCount = complexCount;
    }
}
