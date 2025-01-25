package com.client.invoice.demo.demo.Processor;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.client.invoice.demo.demo.InvoiceWorkflow.InvoiceError;
import com.client.invoice.demo.demo.Persistance.CompositeInvoiceContainer;
import com.client.invoice.demo.demo.Persistance.Invoice;
import com.client.invoice.demo.demo.Persistance.InvoiceStage;
import com.client.invoice.demo.demo.Service.InvoiceWrapper;

public class InvoiceWorkflowProcessor implements ItemProcessor<InvoiceStage, CompositeInvoiceContainer>{

    private static final Logger logger = LoggerFactory.getLogger(InvoiceWorkflowProcessor.class); 

    private InvoiceWrapper invoiceWrapper;

    @Override
    public CompositeInvoiceContainer process(InvoiceStage item) throws Exception {
        CompositeInvoiceContainer container = new CompositeInvoiceContainer(invoiceWrapper);

        
        container.setInvoiceStage(item);
        String invoiceStatus = "A";

        //go through every edit for this staged invoice
        for(InvoiceError invoiceError : InvoiceError.values()){
            Boolean shouldReject = invoiceError.getErrorFunction().apply(container);
            if(shouldReject){
                invoiceStatus = "R";
                logger.info("\nRejecing invoice on error " + invoiceError.getErrorTag());
                break;
            }
        }
        //if the staged invoice should be accepted, migrate it to the invoice table
        if(invoiceStatus.equals("A")){
            Invoice invoice = new Invoice();
            invoice.setInvoiceId(item.getId().getInvoiceStageId());
            invoice.setClientId(item.getId().getClientId());
            invoice.setInvoiceAmount(item.getInvoiceAmount());
            invoice.setInvoiceType(item.getInvoiceType());
            logger.info("\nCreating new invoice: " +  invoice.toString());
            container.setInvoice(invoice);
        }
        item.setInvoiceStatus(invoiceStatus);
        container.setInvoiceStage(item);

        return container;
    }


    public InvoiceWorkflowProcessor(InvoiceWrapper invoiceWrapper){
        this.invoiceWrapper = invoiceWrapper;
    }

    public InvoiceWrapper getInvoiceWrapper() {
        return invoiceWrapper;
    }

    public void setInvoiceWrapper(InvoiceWrapper invoiceWrapper) {
        this.invoiceWrapper = invoiceWrapper;
    }

    
    
}
