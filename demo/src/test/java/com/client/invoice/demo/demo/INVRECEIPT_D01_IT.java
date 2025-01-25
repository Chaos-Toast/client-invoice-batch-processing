package com.client.invoice.demo.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
@SpringBatchTest
@EnableAutoConfiguration
@ContextConfiguration(classes = { DataSourceConfiguration.class, INVRECEIPT_D01.class})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, 
  DirtiesContextTestExecutionListener.class})
public class INVRECEIPT_D01_IT {
    
    // other test constants
 
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
  
    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Autowired
    private Job INVRECEIPT_D01_JOB;
  
    @AfterJob
    public void cleanUp() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    private JobParameters defaultJobParameters() {

        ClassPathResource testData = new ClassPathResource("src/test/java/com/client/invoice/demo/demo/resources/invoice.csv");

        JobParametersBuilder paramsBuilder = new JobParametersBuilder();
        paramsBuilder.addString("fileName", testData.getPath());
        return paramsBuilder.toJobParameters();
    }
    @Test
    public void jobCompletes() throws Exception {
        jobLauncherTestUtils.setJob(INVRECEIPT_D01_JOB);
        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(defaultJobParameters());
        JobInstance actualJobInstance = jobExecution.getJobInstance();
        ExitStatus actualJobExitStatus = jobExecution.getExitStatus();
    
        // then
        assertEquals("COMPLETED",actualJobExitStatus.getExitCode());
    }
}
