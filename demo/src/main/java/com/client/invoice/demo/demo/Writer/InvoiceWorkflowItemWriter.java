package com.client.invoice.demo.demo.Writer;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.client.invoice.demo.demo.Persistance.CompositeInvoiceContainer;
import com.client.invoice.demo.demo.Persistance.Invoice;
import com.client.invoice.demo.demo.Persistance.InvoiceStage;

public class InvoiceWorkflowItemWriter implements ItemWriter<CompositeInvoiceContainer>{


    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void write(Chunk<? extends CompositeInvoiceContainer> chunk) throws Exception {
        String invoiceInsertSql = "INSERT INTO INVOICE (INVOICE_ID, CLIENT_ID, INVOICE_AMT, INVOICE_TYPE) VALUES ";
        String invoiceValues = "(%d, %s, %f, '%s'), ";
        String invoiceStatusRUpdateSql = "UPDATE INVOICE_STG SET INVOICE_STATUS = 'R' WHERE ";
        String invoiceIdsR = "INVOICE_STG_ID IN ( ";
        String clientIdsR = "CLIENT_ID IN ( ";
        String invoiceStatusAUpdateSql = "UPDATE INVOICE_STG SET INVOICE_STATUS = 'A' WHERE ";
        String invoiceIdsA = "INVOICE_STG_ID IN ( ";
        String clientIdsA = "CLIENT_ID IN ( ";

        boolean insertInvoices = false;
        boolean updateRejects = false;
        boolean updateAccepted = false;


        for(Object item : chunk.getItems()){
            if(item != null){
                CompositeInvoiceContainer container = (CompositeInvoiceContainer) item;
                if(container.getInvoice() != null){
                    Invoice invoice = container.getInvoice();
                    invoiceInsertSql += String.format(invoiceValues, invoice.getInvoiceId(), invoice.getClientId(), invoice.getInvoiceAmount(), invoice.getInvoiceType());
                    insertInvoices = true;
                }
                
                //update the status of the invoice
                InvoiceStage invoiceStage = container.getInvoiceStage();
                if(invoiceStage.getInvoiceStatus().equals("R")){
                    invoiceIdsR += String.format("%d, ", invoiceStage.getId().getInvoiceStageId());
                    clientIdsR += String.format("%s, ", invoiceStage.getId().getClientId());
                    updateRejects = true;
                } else if (invoiceStage.getInvoiceStatus().equals("A")){
                    invoiceIdsA += String.format("%d, ", invoiceStage.getId().getInvoiceStageId());
                    clientIdsA += String.format("%s, ", invoiceStage.getId().getClientId());
                    updateAccepted = true;
                }
            }
        }

        if(insertInvoices){
            // remove last comma and space
            invoiceInsertSql = invoiceInsertSql.substring(0, invoiceInsertSql.length() -2); 
            jdbcTemplate.execute(invoiceInsertSql);
        }

        if(updateRejects){
            invoiceIdsR = invoiceIdsR.substring(0, invoiceIdsR.length() - 2) + ") AND ";
            clientIdsR = clientIdsR.substring(0, clientIdsR.length() - 2) + ")";
            invoiceStatusRUpdateSql += invoiceIdsR + clientIdsR;
            jdbcTemplate.execute(invoiceStatusRUpdateSql);
        }
        
        if(updateAccepted){
            invoiceIdsA = invoiceIdsA.substring(0, invoiceIdsA.length() - 2) + ") AND ";
            clientIdsA = clientIdsA.substring(0, clientIdsA.length() - 2) + ")";
            invoiceStatusAUpdateSql += invoiceIdsA + clientIdsA;
            jdbcTemplate.execute(invoiceStatusAUpdateSql);
        }

    }
    
}
