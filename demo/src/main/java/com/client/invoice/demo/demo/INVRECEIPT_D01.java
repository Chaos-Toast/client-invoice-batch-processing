package com.client.invoice.demo.demo;

import java.time.LocalDateTime;
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
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import com.client.invoice.demo.demo.Mapper.InvoiceRowMapper;
import com.client.invoice.demo.demo.Persistance.Invoice;
import com.client.invoice.demo.demo.Processor.InvoiceReceiptProcessor;

@Configuration
@EnableBatchProcessing
public class INVRECEIPT_D01 {

    @Autowired
    DataSource dataSource;

    @Bean
    public Job INVRECEIPT_D01_JOB(JobRepository jobRepository, Step receiptInvoiceStep){
        return new JobBuilder("INVRECEIPT_D01_JOB", jobRepository)
                .start(receiptInvoiceStep)
                .build();
    }

    @Bean
    public Step receiptInvoiceStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager,
                                    ItemReader<Invoice> receiptInvoiceReader, 
                                    ItemProcessor<Invoice, Invoice> receiptInvoiceProcessor, 
                                    ItemWriter<Invoice> receiptInvoiceWriter){
        return new StepBuilder("reciptInvoiceStep", jobRepository)
            .<Invoice, Invoice>chunk(10, platformTransactionManager)
            .reader(receiptInvoiceReader)
            .processor(receiptInvoiceProcessor)
            .writer(receiptInvoiceWriter)
            .build();
    }


    //invoice flat file item reader
    @Bean
    @StepScope
    public JdbcCursorItemReader<Invoice> receiptInvoiceReader() {
        JdbcCursorItemReader<Invoice> reader = new JdbcCursorItemReader<>();
        reader.setDataSource(dataSource);
        reader.setSql("SELECT * FROM INVOICE");
        reader.setRowMapper(new InvoiceRowMapper());
        return reader;
    }

    //invoice item processor
    @Bean
    public ItemProcessor<Invoice, Invoice> receiptInvoiceProcessor(){
        return new InvoiceReceiptProcessor();
    }

    //invoice item writer
    @Bean
    public FlatFileItemWriter<Invoice> receiptInvoiceWriter(){
        
        LocalDateTime timestamp = LocalDateTime.now();
        String timestamppath = timestamp.toString().replaceAll(":", " ");


        BeanWrapperFieldExtractor<Invoice> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[] {"invoiceType", "invoiceId", "clientId", "invoiceAmount"});
        fieldExtractor.afterPropertiesSet();

        DelimitedLineAggregator<Invoice> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");
        lineAggregator.setFieldExtractor(fieldExtractor);


        return new FlatFileItemWriterBuilder<Invoice>()
            .name("receiptInvoiceWriter")
            .resource(new FileSystemResource("target/receipts/invoice-receipt" + timestamppath + ".csv"))
            .lineAggregator(lineAggregator)
            .build();
    }

    @Bean
    public DelimitedLineAggregator<Invoice> invoiceDelimitedLineAggregator(){
        DelimitedLineAggregator<Invoice> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");
        lineAggregator.setFieldExtractor(new BeanWrapperFieldExtractor<Invoice>(){
            {
                setNames(new String[]{"invoiceType, invoiceId, cleintId, invoiceAmount"});
            }
        });
        return lineAggregator;
    }
}
