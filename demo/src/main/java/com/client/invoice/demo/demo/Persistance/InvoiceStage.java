package com.client.invoice.demo.demo.Persistance;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "INVOICE_STG")
public class InvoiceStage implements Serializable {
    private static final long serialVersionUID = 933352989L;

    public InvoiceStage() {
        // Default constructor required by Hibernate
    }

    @EmbeddedId
    private InvoiceStageId id;

    @Embeddable
    public static class InvoiceStageId implements Serializable {
        private static final long serialVersionUID = 933352089L;

        public InvoiceStageId() {
            // Default constructor required by Hibernate
        }

        @Column(name = "INVOICE_STG_ID")
        private Long invoiceStageId;

        @Column(name = "CLIENT_ID")
        private String clientId;

        
        public Long getInvoiceStageId() {
            return invoiceStageId;
        }

        public void setInvoiceStageId(Long invoiceStageId) {
            this.invoiceStageId = invoiceStageId;
        }

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((invoiceStageId == null) ? 0 : invoiceStageId.hashCode());
            result = prime * result + ((clientId == null) ? 0 : clientId.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            InvoiceStageId other = (InvoiceStageId) obj;
            if (invoiceStageId == null) {
                if (other.invoiceStageId != null)
                    return false;
            } else if (!invoiceStageId.equals(other.invoiceStageId))
                return false;
            if (clientId == null) {
                if (other.clientId != null)
                    return false;
            } else if (!clientId.equals(other.clientId))
                return false;
            return true;
        }

        
    }


    @Column(name = "INVOICE_AMT")
    private BigDecimal invoiceAmount;

    @Column(name = "INVOICE_AMT_SUB_FLG")
    private Boolean invoiceAmountSubmissionFlag;

    @Column(name = "INVOICE_TYPE")
    private String invoiceType;

    @Column(name = "INVOICE_TYPE_SUB_FLG")
    private Boolean invoiceTypeSubmissionFlag;

    @Column(name = "INVOICE_STATUS")
    private String invoiceStatus;

    public InvoiceStageId getId() {
        return id;
    }

    public void setId(InvoiceStageId id) {
        this.id = id;
    }

    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public Boolean getInvoiceAmountSubmissionFlag() {
        return invoiceAmountSubmissionFlag;
    }

    public void setInvoiceAmountSubmissionFlag(Boolean invoiceAmountSubmissionFlag) {
        this.invoiceAmountSubmissionFlag = invoiceAmountSubmissionFlag;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public Boolean getInvoiceTypeSubmissionFlag() {
        return invoiceTypeSubmissionFlag;
    }

    public void setInvoiceTypeSubmissionFlag(Boolean invoiceTypeSubmissionFlag) {
        this.invoiceTypeSubmissionFlag = invoiceTypeSubmissionFlag;
    }
    

    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        InvoiceStage other = (InvoiceStage) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    @Override
    public String toString(){
        String result = "InvoiceStage[invoiceStgId=%d, clientId=%s, invoiceType=%s, invoiceAmount=%f]";
        return String.format(result, this.id.invoiceStageId, this.id.clientId, this.invoiceType, this.invoiceAmount);
    }

    
}
