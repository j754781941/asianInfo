package com.asiainfo.monitor.entity;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 格式八
 * @author Dekun Wang
 */
public class Format8 implements Serializable {

    private static final long serialVersionUID = 8125480499549681807L;

    private String formatDate;

    private BigDecimal formatCount;

    public Format8() {
    }

    public Format8(String formatDate, BigDecimal formatCount) {
        this.formatDate = formatDate;
        this.formatCount = formatCount;
    }

    public String getFormatDate() {
        return formatDate;
    }

    public void setFormatDate(String formatDate) {
        this.formatDate = formatDate;
    }

    public BigDecimal getFormatCount() {
        return formatCount;
    }

    public void setFormatCount(BigDecimal formatCount) {
        this.formatCount = formatCount;
    }
}
