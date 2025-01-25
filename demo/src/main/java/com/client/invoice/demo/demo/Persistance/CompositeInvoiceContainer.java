package com.client.invoice.demo.demo.Persistance;

import com.client.invoice.demo.demo.Service.InvoiceWrapper;

public class CompositeInvoiceContainer {
    
    private Invoice invoice;
    private InvoiceStage invoiceStage;
    private InvoiceWrapper invoiceWrapper;


    public CompositeInvoiceContainer(InvoiceWrapper invoiceWrapper) {
        this.invoiceWrapper = invoiceWrapper;
    }

    public Invoice getInvoice() {
        return invoice;
    }
    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }
    public InvoiceStage getInvoiceStage() {
        return invoiceStage;
    }
    public void setInvoiceStage(InvoiceStage invoiceStage) {
        this.invoiceStage = invoiceStage;
    }
    public InvoiceWrapper getInvoiceWrapper() {
        return invoiceWrapper;
    }
    public void setInvoiceWrapper(InvoiceWrapper invoiceWrapper) {
        this.invoiceWrapper = invoiceWrapper;
    }

    

    
}
