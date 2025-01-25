package com.client.invoice.demo.demo.InvoiceWorkflow;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.client.invoice.demo.demo.Persistance.CompositeInvoiceContainer;
import com.client.invoice.demo.demo.Persistance.InvoiceStage;
import com.client.invoice.demo.demo.Persistance.InvoiceTypeRef;

public class InvoiceErrorUtils {
    private static final Logger logger = LoggerFactory.getLogger(InvoiceErrorUtils.class); 

    private static Set<InvoiceTypeRef> invoiceTypes = new HashSet<>();
    private static Set<String> invoiceTypeCodes;
    private static Map<String, BigDecimal> invoiceCodeMaxAmounts;
    
    //Setup for all error methods
    //called in each method for testing and consistency,
    //but only populates data once
    public static void setup(CompositeInvoiceContainer container){
        //make one service call to reference the available invoice types
        if(invoiceTypes.size() == 0){
            populateInvoiceTypes(container);
        }
    }

    public static void populateInvoiceTypes(CompositeInvoiceContainer container){
        invoiceTypes = new HashSet<>();
        invoiceTypeCodes = new HashSet<>();
        invoiceCodeMaxAmounts = new HashMap<>();
        invoiceTypes = container.getInvoiceWrapper().getInvoiceTypeRefService().getAllInvoiceTypes();
        for(InvoiceTypeRef invoiceTypeRef : invoiceTypes){
            invoiceTypeCodes.add(invoiceTypeRef.getInvoiceTypeRefId());
            invoiceCodeMaxAmounts.put(invoiceTypeRef.getInvoiceTypeRefId(), invoiceTypeRef.getMaxAmount());
        }
    }

    /*
     * ERROR 1 - InvoiceType
     * 
     * invoice type submitted is null
     * OR
     * invoice type is not in the database
     */
    public static Boolean shouldTriggerError1(CompositeInvoiceContainer container){
        setup(container);
        InvoiceStage invoiceStage = container.getInvoiceStage();
        boolean invoiceTypeSubmittedAndIsNull = invoiceStage.getInvoiceTypeSubmissionFlag() && invoiceStage.getInvoiceType()==null;
        Set<String> validInvoiceTypes = invoiceTypeCodes;
        logger.info("invoice type: " + invoiceStage.getInvoiceType());
        for(String refType : validInvoiceTypes){
            logger.info("ref type : " +refType);
        }
        boolean isValidInvoiceType = validInvoiceTypes.contains(invoiceStage.getInvoiceType());
        if(invoiceTypeSubmittedAndIsNull || !isValidInvoiceType){
            return true;
        }
        return false;
    }

    /* ERROR 2
     * 
     * submitted invoice amount is
     * less than zero
     * or
     * more than the max amount for specified type
     */
    public static Boolean shouldTriggerError2(CompositeInvoiceContainer container){
        setup(container);
        InvoiceStage invoiceStage = container.getInvoiceStage();
        if(invoiceStage.getInvoiceAmount()==null){
            return false;
        }

        Boolean isMoreThanMaxAmount = invoiceStage.getInvoiceAmount().compareTo(invoiceCodeMaxAmounts.get(invoiceStage.getInvoiceType())) >= 0;
        if(isMoreThanMaxAmount || invoiceStage.getInvoiceAmount().compareTo(BigDecimal.ZERO) < 0){
            return true;
        }
        return false;
    }
}
