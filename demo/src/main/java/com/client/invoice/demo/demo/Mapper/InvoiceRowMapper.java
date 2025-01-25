package com.client.invoice.demo.demo.Mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.client.invoice.demo.demo.Persistance.Invoice;

public class InvoiceRowMapper implements RowMapper<Invoice>{

    @Override
    public Invoice mapRow(ResultSet rs, int rowNum) throws SQLException {
        Invoice invoice = new Invoice();

        invoice.setInvoiceId(rs.getLong("INVOICE_ID"));
        invoice.setClientId(rs.getString("CLIENT_ID"));
        invoice.setInvoiceType(rs.getString("INVOICE_TYPE"));
        invoice.setInvoiceAmount(rs.getBigDecimal("INVOICE_AMT"));


        return invoice;
    }
    
}
