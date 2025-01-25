package com.client.invoice.demo.demo.Processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.client.invoice.demo.demo.Persistance.Invoice;

public class InvoiceReceiptProcessor implements ItemProcessor<Invoice, Invoice>{
    private static final Logger logger = LoggerFactory.getLogger(InvoiceReceiptProcessor.class); 

    @Override
    public Invoice process(Invoice item) throws Exception {
        

        logger.info("\nReceipt will include this invoice: " + item.toString());

        return item;
    }
    
    
}
