package com.client.invoice.demo.demo.Service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.client.invoice.demo.demo.Dao.InvoiceTypeRefDao;
import com.client.invoice.demo.demo.Persistance.InvoiceTypeRef;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class InvoiceTypeRefServiceImpl implements InvoiceTypeRefService {

    @Autowired
    InvoiceTypeRefDao invoiceTypeRefDao;

    @Override
    public Set<InvoiceTypeRef> getAllInvoiceTypes() {
        return invoiceTypeRefDao.getAllInvoiceTypes();
    }
    
}
