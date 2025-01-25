package com.client.invoice.demo.demo.Persistance;

import java.math.BigDecimal;

public class Invoice {
    
    private String invoiceType;
    private Long invoiceId;
    private String clientId;
    private BigDecimal invoiceAmount;
    public String getInvoiceType() {
        return invoiceType;
    }
    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }
    public Long getInvoiceId() {
        return invoiceId;
    }
    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
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
    
    @Override
    public String toString(){
        String result = "Invoice[invoiceId=%d, clientId=%s, invoiceType=%s, invoiceAmount=%f]";
        return String.format(result, this.invoiceId, this.clientId, this.invoiceType, this.invoiceAmount);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Invoice other = (Invoice) obj;
        if (invoiceId == null) {
            if (other.invoiceId != null)
                return false;
        } else if (!invoiceId.equals(other.invoiceId))
            return false;
        if (clientId == null) {
            if (other.clientId != null)
                return false;
        } else if (!clientId.equals(other.clientId))
            return false;
        return true;
    }
}
