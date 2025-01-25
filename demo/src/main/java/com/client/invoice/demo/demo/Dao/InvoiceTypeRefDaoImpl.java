package com.client.invoice.demo.demo.Dao;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.client.invoice.demo.demo.Persistance.InvoiceTypeRef;



@Repository
public class InvoiceTypeRefDaoImpl implements InvoiceTypeRefDao{
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Set<InvoiceTypeRef> getAllInvoiceTypes() {
        try(Session session = sessionFactory.openSession()){
            return new HashSet<>(session.createQuery("FROM InvoiceTypeRef", InvoiceTypeRef.class).list());
        }
    }
    
}
