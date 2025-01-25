package com.client.invoice.demo.demo.Persistance;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "INVOICE_TYPE_REF")
public class InvoiceTypeRef {
    
    @Id
    @Column(name = "INVOICE_TYPE_REF_ID")
    private String invoiceTypeRefId;

    @Column(name = "DESCR")
    private String description;

    @Column(name = "MAX_AMT")
    private BigDecimal maxAmount;

    public String getInvoiceTypeRefId() {
        return invoiceTypeRefId;
    }

    public void setInvoiceTypeRefId(String invoiceTypeRefId) {
        this.invoiceTypeRefId = invoiceTypeRefId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(BigDecimal maxAmount) {
        this.maxAmount = maxAmount;
    }

    
}
