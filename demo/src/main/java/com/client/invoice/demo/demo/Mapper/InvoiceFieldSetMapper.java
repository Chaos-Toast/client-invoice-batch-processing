package com.client.invoice.demo.demo.Mapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.client.invoice.demo.demo.Persistance.InvoiceReceived;

public class InvoiceFieldSetMapper implements FieldSetMapper<InvoiceReceived> {

    @Override
    public InvoiceReceived mapFieldSet(FieldSet fieldSet) throws BindException {
        InvoiceReceived invoiceReceived = new InvoiceReceived();
        invoiceReceived.setInvoiceType(fieldSet.readString("invoiceType"));
        invoiceReceived.setClientId(fieldSet.readString("clientId"));
        invoiceReceived.setInvoiceAmount(fieldSet.readBigDecimal("invoiceAmount"));

        return invoiceReceived;
    }
    
}
