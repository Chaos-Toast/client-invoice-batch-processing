package com.client.invoice.demo.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvoiceWrapper {
    
    @Autowired
    private InvoiceTypeRefService invoiceTypeRefService;

    public InvoiceTypeRefService getInvoiceTypeRefService() {
        return invoiceTypeRefService;
    }

    public void setInvoiceTypeRefService(InvoiceTypeRefService invoiceTypeRefService) {
        this.invoiceTypeRefService = invoiceTypeRefService;
    }

    


}
