package com.client.invoice.demo.demo.InvoiceWorkflow;

import java.util.function.Function;

import com.client.invoice.demo.demo.Persistance.CompositeInvoiceContainer;

public enum InvoiceError {
    error1("error1", InvoiceErrorUtils::shouldTriggerError1 ,"invoiceType"),
    error2("error2", InvoiceErrorUtils::shouldTriggerError2, "invoiceType");
    

    private String errorTag;
    private Function<CompositeInvoiceContainer, Boolean> errorFunction;
    private String fieldName;
    
    InvoiceError(String errorTag, Function<CompositeInvoiceContainer, Boolean> errorFunction, String fieldName) {
        this.errorTag = errorTag;
        this.errorFunction = errorFunction;
        this.fieldName = fieldName;
    }

    public String getErrorTag() {
        return errorTag;
    }
    public void setErrorTag(String errorTag) {
        this.errorTag = errorTag;
    }
    public Function<CompositeInvoiceContainer, Boolean> getErrorFunction() {
        return errorFunction;
    }
    public void setErrorFunction(Function<CompositeInvoiceContainer, Boolean> errorFunction) {
        this.errorFunction = errorFunction;
    }
    public String getFieldName() {
        return fieldName;
    }
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    
}
