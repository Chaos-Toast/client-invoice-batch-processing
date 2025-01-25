package com.client.invoice.demo.demo.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.nio.file.*;
import java.time.LocalDateTime;

import javax.naming.TimeLimitExceededException;
import javax.sql.DataSource;

@Component
public class FileMonitoringService {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job INVPARSER_D01_JOB; 

    
    @Autowired
    private Job INVWORKFLOW_D01_JOB; 
    
    @Autowired
    private Job INVRECEIPT_D01_JOB; 


    @Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final Logger logger = LoggerFactory.getLogger(FileMonitoringService.class); 

    
    public void startMonitoring(String directoryToWatch) throws Exception {
        Path path = Paths.get(directoryToWatch);
        WatchService watchService = FileSystems.getDefault().newWatchService();
        path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

        while (true) {

            WatchKey key = watchService.take(); 

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();

                if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                    String fileName = directoryToWatch + "/" + event.context().toString(); 
                    // Trigger the Spring Batch job
                    launchJob(fileName); 
                }
            }

            boolean valid = key.reset();
            if (!valid) {
                break;
            }
        }
        throw new TimeLimitExceededException();
    }

    public void launchJob(String fileName) throws Exception {
        jdbcTemplate.setDataSource(dataSource);
        JobParameters jobParameters = new JobParametersBuilder()
                .addLocalDateTime("runTime", LocalDateTime.now())
                .addString("fileName", fileName) 
                .toJobParameters(); 
        logger.info("Starting invoice parser with: " + fileName);
        jobLauncher.run(INVPARSER_D01_JOB, jobParameters);
        logger.info("Starting Invoice workflow, invoice stage: " +  jdbcTemplate.queryForList("SELECT * FROM INVOICE_STG").toString());
        jobLauncher.run(INVWORKFLOW_D01_JOB, jobParameters);
        logger.info("Starting invoice receipt, invoices: " +  jdbcTemplate.queryForList("SELECT * FROM INVOICE").toString());
        jobLauncher.run(INVRECEIPT_D01_JOB, jobParameters);

    }
}