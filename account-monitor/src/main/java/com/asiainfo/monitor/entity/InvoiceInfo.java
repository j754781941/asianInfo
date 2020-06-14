package com.asiainfo.monitor.entity;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 专票
 * @author Dekun Wang
 */
public class InvoiceInfo implements Serializable {

    private static final long serialVersionUID  = -8664247263998221819L;

    private String invoiceDate;

    private BigDecimal invoiceCount;

    public InvoiceInfo() {
    }

    public InvoiceInfo(String invoiceDate, BigDecimal invoiceCount) {
        this.invoiceDate = invoiceDate;
        this.invoiceCount = invoiceCount;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public BigDecimal getInvoiceCount() {
        return invoiceCount;
    }

    public void setInvoiceCount(BigDecimal invoiceCount) {
        this.invoiceCount = invoiceCount;
    }
}
