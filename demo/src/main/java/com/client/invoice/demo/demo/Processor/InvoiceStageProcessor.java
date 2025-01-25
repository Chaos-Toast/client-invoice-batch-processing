package com.client.invoice.demo.demo.Processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.BeanUtils;

import com.client.invoice.demo.demo.Persistance.InvoiceReceived;
import com.client.invoice.demo.demo.Persistance.InvoiceStage;
import com.client.invoice.demo.demo.Persistance.InvoiceStage.InvoiceStageId;

public class InvoiceStageProcessor implements ItemProcessor<InvoiceReceived, InvoiceStage>{
    private static final Logger logger = LoggerFactory.getLogger(InvoiceStageProcessor.class); 

    @Override
    public InvoiceStage process(InvoiceReceived item) throws Exception {
        InvoiceStage invoiceStage = new InvoiceStage();
        //sanity check to return nothing
        if(item == null){
            return null;
        }
        
        BeanUtils.copyProperties(item, invoiceStage);

        //add flags to fields submitted
        invoiceStage.setInvoiceAmountSubmissionFlag(!item.getInvoiceAmount().equals(null));
        invoiceStage.setInvoiceTypeSubmissionFlag(!item.getInvoiceType().equals(null));
        
        InvoiceStageId id = new InvoiceStageId();
        id.setClientId(item.getClientId());
        invoiceStage.setId(id);

        //Setting invoice status to accepted to go ahead and build the receipt service
        invoiceStage.setInvoiceStatus("P");

        logger.info("\nAdding invoice to staging table: " + invoiceStage.toString());

        return invoiceStage;
    }
    
}
