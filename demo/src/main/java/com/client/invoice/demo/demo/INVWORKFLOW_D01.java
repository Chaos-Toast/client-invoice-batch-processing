package com.client.invoice.demo.demo;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.client.invoice.demo.demo.Mapper.InvoiceStageRowMapper;
import com.client.invoice.demo.demo.Persistance.CompositeInvoiceContainer;
import com.client.invoice.demo.demo.Persistance.InvoiceStage;
import com.client.invoice.demo.demo.Processor.InvoiceWorkflowProcessor;
import com.client.invoice.demo.demo.Service.InvoiceWrapper;
import com.client.invoice.demo.demo.Writer.InvoiceWorkflowItemWriter;

@Configuration
@EnableBatchProcessing
public class INVWORKFLOW_D01 {

    @Autowired
    DataSource dataSource;

    @Autowired
    InvoiceWrapper invoiceWrapper;

    @Bean
    public Job INVWORKFLOW_D01_JOB(JobRepository jobRepository, Step invoiceWorkflowStep){
        return new JobBuilder("INVWORKFLOW_D01_JOB", jobRepository)
                .start(invoiceWorkflowStep)
                .build();
    }

    @Bean
    public Step invoiceWorkflowStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager,
                                    ItemReader<InvoiceStage> invoiceWorkflowReader, 
                                    ItemProcessor<InvoiceStage, CompositeInvoiceContainer> invoiceWorkflowProcessor, 
                                    ItemWriter<CompositeInvoiceContainer> invoiceWorkflowWriter){
        return new StepBuilder("reciptInvoiceStep", jobRepository)
            .<InvoiceStage, CompositeInvoiceContainer>chunk(10, platformTransactionManager)
            .reader(invoiceWorkflowReader)
            .processor(invoiceWorkflowProcessor)
            .writer(invoiceWorkflowWriter)
            .build();
    }


    //invoice flat file item reader
    @Bean
    @StepScope
    public JdbcCursorItemReader<InvoiceStage> invoiceWorkflowReader() {
        JdbcCursorItemReader<InvoiceStage> reader = new JdbcCursorItemReader<>();
        reader.setDataSource(dataSource);
        reader.setSql("SELECT * FROM INVOICE_STG WHERE INVOICE_STATUS = 'P'");
        reader.setRowMapper(new InvoiceStageRowMapper());
        return reader;
    }

    //invoice item processor
    @Bean
    public ItemProcessor<InvoiceStage, CompositeInvoiceContainer> invoiceWorkflowProcessor(){
        return new InvoiceWorkflowProcessor(invoiceWrapper);
    }

    //invoice item writer
    @Bean
    public ItemWriter<CompositeInvoiceContainer> invoiceWorkflowWriter(){
        return new InvoiceWorkflowItemWriter();
    }

}
