package com.client.invoice.demo.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.client.invoice.demo.demo.Service.FileMonitoringService;

@SpringBootApplication
public class ClientInvoiceDemoApplication {
    private static final Logger logger = LoggerFactory.getLogger(ClientInvoiceDemoApplication.class); 


    @Autowired
    private FileMonitoringService fileMonitoringService;

	//this currently points to demo\target\classes\inputDirectory
	//feel free to change this to any directory 
	//--really just try to change this to a directory tou have setup
	//all you need to do is drop a csv into this directory
	//that contains invoices similar to invoice.csv in the resources folder
	//those will be parsed, put through business logic, and a receipt for valid invoices will be created
	//the receipts will show up in the same level as the demo folder in target\receipts
	@Value("classpath:inputDirectory/")
	private Resource inputDirectory;


	public static void main(String[] args) {
		
		SpringApplication.run(ClientInvoiceDemoApplication.class, args);
	}

	@Bean
	public boolean startMonitoring() throws Exception {
		
		logger.info("Monitoring files in " + inputDirectory.getFile().getAbsolutePath());
        fileMonitoringService.startMonitoring(inputDirectory.getFile().getAbsolutePath());
		return true;
    }

}
