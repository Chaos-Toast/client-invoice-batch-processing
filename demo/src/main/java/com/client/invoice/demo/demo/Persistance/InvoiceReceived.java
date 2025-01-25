package com.client.invoice.demo.demo.Persistance;

import java.math.BigDecimal;

public class InvoiceReceived {
    private String invoiceType;
    private String clientId;
    private BigDecimal invoiceAmount;
    public String getInvoiceType() {
        return invoiceType;
    }
    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }
    public String getClientId() {
        return clientId;
    }
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }
    public void setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }
    
}
