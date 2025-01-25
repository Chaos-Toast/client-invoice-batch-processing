package com.client.invoice.demo.demo.Processor;

import org.springframework.batch.item.ItemProcessor;

import com.client.invoice.demo.demo.Persistance.Client;

public class ClientItemProcessor implements ItemProcessor<Client, Client> {

    public Client process(Client item) throws Exception {
        return item;
    }
}