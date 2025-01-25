package com.client.invoice.demo.demo.Mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.client.invoice.demo.demo.Persistance.InvoiceStage;
import com.client.invoice.demo.demo.Persistance.InvoiceStage.InvoiceStageId;

public class InvoiceStageRowMapper implements RowMapper<InvoiceStage>{

    @Override
    public InvoiceStage mapRow(ResultSet rs, int rowNum) throws SQLException {
        InvoiceStage invoiceStage = new InvoiceStage();

        InvoiceStageId invoiceStageId = new InvoiceStageId();
        
        invoiceStageId.setInvoiceStageId(rs.getLong("INVOICE_STG_ID"));
        invoiceStageId.setClientId(rs.getString("CLIENT_ID"));

        invoiceStage.setId(invoiceStageId);
        invoiceStage.setInvoiceAmount(rs.getBigDecimal("INVOICE_AMT"));
        invoiceStage.setInvoiceAmountSubmissionFlag(rs.getBoolean("INVOICE_AMT_SUB_FLG"));
        invoiceStage.setInvoiceType(rs.getString("INVOICE_TYPE"));
        invoiceStage.setInvoiceTypeSubmissionFlag(rs.getBoolean("INVOICE_TYPE_SUB_FLG"));
        invoiceStage.setInvoiceStatus(rs.getString("INVOICE_STATUS"));


        return invoiceStage;
    }
    
}
