package com.asiainfo.monitor.entity;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 负余额账本异常
 * @author Dekun Wang
 */
public class NegativeBalanceInfo implements Serializable {

    private static final long serialVersionUID = -1219633869210445952L;
    private String negativeBalanceDate;

    private BigDecimal negativeBalanceCount;

    public NegativeBalanceInfo() {
    }

    public NegativeBalanceInfo(String negativeBalanceDate, BigDecimal negativeBalanceCount) {
        this.negativeBalanceDate = negativeBalanceDate;
        this.negativeBalanceCount = negativeBalanceCount;
    }

    public String getNegativeBalanceDate() {
        return negativeBalanceDate;
    }

    public void setNegativeBalanceDate(String negativeBalanceDate) {
        this.negativeBalanceDate = negativeBalanceDate;
    }

    public BigDecimal getNegativeBalanceCount() {
        return negativeBalanceCount;
    }

    public void setNegativeBalanceCount(BigDecimal negativeBalanceCount) {
        this.negativeBalanceCount = negativeBalanceCount;
    }
}
