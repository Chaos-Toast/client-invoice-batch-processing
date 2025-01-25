package com.client.invoice.demo.demo;

import java.math.BigDecimal;

import com.client.invoice.demo.demo.Persistance.Invoice;

public class TestUtility {
 
    
    public static Invoice populateInvoice(Long invoiceId, String clientId, String invoiceType, BigDecimal invoiceAmount){
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(invoiceId);
        invoice.setClientId(clientId);
        invoice.setInvoiceType(invoiceType);
        invoice.setInvoiceAmount(invoiceAmount);

        return invoice;
    }
}
