package com.client.invoice.demo.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import com.client.invoice.demo.demo.Dao.InvoiceTypeRefDaoImpl;
import com.client.invoice.demo.demo.Mapper.InvoiceRowMapper;
import com.client.invoice.demo.demo.Persistance.Invoice;
import com.client.invoice.demo.demo.Service.InvoiceTypeRefServiceImpl;
import com.client.invoice.demo.demo.Service.InvoiceWrapper;
@SpringBatchTest
@EnableAutoConfiguration
@ContextConfiguration(classes = { DataSourceConfiguration.class, INVPARSER_D01.class, INVRECEIPT_D01.class,INVWORKFLOW_D01.class
            , InvoiceWrapper.class, InvoiceTypeRefServiceImpl.class, InvoiceTypeRefDaoImpl.class})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, 
  DirtiesContextTestExecutionListener.class})
public class IntegrationTest {
    
    private static final Logger logger = LoggerFactory.getLogger(IntegrationTest.class); 

 
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
  
    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private Job INVRECEIPT_D01_JOB;
    
    @Autowired
    private Job INVPARSER_D01_JOB;
    
    @Autowired
    private Job INVWORKFLOW_D01_JOB;

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

    @Order(1)
    @Test
    public void integrationOfJobsComplete() throws Exception {
        jobLauncherTestUtils.setJob(INVPARSER_D01_JOB);
        // when
        JobExecution jobExecution1 = jobLauncherTestUtils.launchJob(defaultJobParameters());
        JobInstance actualJobInstance1 = jobExecution1.getJobInstance();
        ExitStatus actualJobExitStatus1 = jobExecution1.getExitStatus();
        
        jobLauncherTestUtils.setJob(INVWORKFLOW_D01_JOB);

        // when
        JobExecution jobExecution2 = jobLauncherTestUtils.launchJob(defaultJobParameters());
        JobInstance actualJobInstance2 = jobExecution2.getJobInstance();
        ExitStatus actualJobExitStatus2 = jobExecution2.getExitStatus();

        jobLauncherTestUtils.setJob(INVRECEIPT_D01_JOB);


        // when
        JobExecution jobExecution3 = jobLauncherTestUtils.launchJob(defaultJobParameters());
        JobInstance actualJobInstance3 = jobExecution3.getJobInstance();
        ExitStatus actualJobExitStatus3 = jobExecution3.getExitStatus();

        // then
        assertEquals("COMPLETED",actualJobExitStatus1.getExitCode());
        assertEquals("COMPLETED",actualJobExitStatus2.getExitCode());
        assertEquals("COMPLETED",actualJobExitStatus3.getExitCode());

    }

    @Order(2)
    @Test
    public void goodSubmittedInvoicesHaveNoErrors()
    {
        //the first 4 lines of invoice.csv have good invoices that should be accepted
        Invoice LGInvoice = TestUtility.populateInvoice(1L, "1", "LG", BigDecimal.valueOf(100));
        Invoice PRInvoice = TestUtility.populateInvoice(2L, "1", "BR", BigDecimal.valueOf(1000));
        Invoice SBInvoice = TestUtility.populateInvoice(3L, "1", "SB", BigDecimal.valueOf(1));
        Invoice AWInvoice = TestUtility.populateInvoice(4L, "1", "AW", BigDecimal.valueOf(21.1));

        List<Invoice> actualInvoices = jdbcTemplate.query("SELECT * FROM INVOICE ORDER BY INVOICE_ID", new InvoiceRowMapper());

        assertEquals(LGInvoice, actualInvoices.get(0));
        assertEquals(PRInvoice, actualInvoices.get(1));
        assertEquals(SBInvoice, actualInvoices.get(2));
        assertEquals(AWInvoice, actualInvoices.get(3));

    }

    @Order(3)
    @Test
    public void submittedInvoicesRejectOnError1()
    {
        //lines 5 and 6 of invoice.csv should be reject because of their invoice types
        Invoice error1InvoiceIncorrectType = TestUtility.populateInvoice(4L, "2", "SC", BigDecimal.valueOf(100));
        Invoice error1InvoiceNullType = TestUtility.populateInvoice(5L, "2", null, BigDecimal.valueOf(100));

        List<Invoice> actualInvoices = jdbcTemplate.query("SELECT * FROM INVOICE ORDER BY INVOICE_ID", new InvoiceRowMapper());

        assertEquals(true, !actualInvoices.contains(error1InvoiceIncorrectType));
        assertEquals(true, !actualInvoices.contains(error1InvoiceNullType));

    }

    @Order(4)
    @Test
    public void submittedInvoicesRejectOnError2()
    {
        //lines 7 and 8 of invoice.csv should be reject because of their invoice amounts
        Invoice error2InvoiceAboveMax = TestUtility.populateInvoice(4L, "3", "LG", BigDecimal.valueOf(1000000));
        Invoice error2InvoiceBelowZero = TestUtility.populateInvoice(5L, "3", "LG", BigDecimal.valueOf(-1));

        List<Invoice> actualInvoices = jdbcTemplate.query("SELECT * FROM INVOICE ORDER BY INVOICE_ID", new InvoiceRowMapper());

        assertEquals(true, !actualInvoices.contains(error2InvoiceAboveMax));
        assertEquals(true, !actualInvoices.contains(error2InvoiceBelowZero));

    }
}
