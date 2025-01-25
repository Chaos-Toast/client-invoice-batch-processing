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
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

import com.client.invoice.demo.demo.Mapper.InvoiceFieldSetMapper;
import com.client.invoice.demo.demo.Persistance.InvoiceReceived;
import com.client.invoice.demo.demo.Persistance.InvoiceStage;
import com.client.invoice.demo.demo.Processor.InvoiceStageProcessor;

@Configuration
@EnableBatchProcessing
public class INVPARSER_D01 {

    @Autowired
    DataSource dataSource;

    @Bean
    public Job INVPARSER_D01_JOB(JobRepository jobRepository, Step parseInvoicesStep){
        return new JobBuilder("INVPARSER_D01_JOB", jobRepository)
                .start(parseInvoicesStep)
                .build();
    }

    @Bean
    public Step parseInvoicesStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager,
                                    ItemReader<InvoiceReceived> parseInvoiceReader, 
                                    ItemProcessor<InvoiceReceived, InvoiceStage> parseInvoiceProcessor, 
                                    ItemWriter<InvoiceStage> parseInvoiceWriter){
        return new StepBuilder("parseInvoicesStep", jobRepository)
            .<InvoiceReceived, InvoiceStage>chunk(10, platformTransactionManager)
            .reader(parseInvoiceReader)
            .processor(parseInvoiceProcessor)
            .writer(parseInvoiceWriter)
            .build();
    }


    //invoice flat file item reader
    @Bean
    @StepScope
    public FlatFileItemReader<InvoiceReceived> parseInvoiceReader(@Value("#{jobParameters['fileName']}") String filePath) {
        Resource resource = new FileSystemResource(filePath);
        FlatFileItemReader<InvoiceReceived> reader = new FlatFileItemReader<>();
        reader.setResource(resource);
        reader.setLineMapper(invoiceLineMapper());
        return reader;
    }

    @Bean
    public DefaultLineMapper<InvoiceReceived> invoiceLineMapper(){
        DefaultLineMapper<InvoiceReceived> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(invoiceTokenizer());
        lineMapper.setFieldSetMapper(new InvoiceFieldSetMapper());
        lineMapper.afterPropertiesSet();
        return lineMapper;
    }

    @Bean
    public DelimitedLineTokenizer invoiceTokenizer(){
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(",");
        tokenizer.setNames("invoiceType", "clientId", "invoiceAmount");
        return tokenizer;
    }

    //invoice item processor
    @Bean
    public ItemProcessor<InvoiceReceived, InvoiceStage> parseInvoiceProcessor(){
        return new InvoiceStageProcessor();
    }

    //invoice item writer
    @Bean
    public JdbcBatchItemWriter<InvoiceStage> parseInvoiceWriter(){
        return new JdbcBatchItemWriterBuilder<InvoiceStage>()
        .dataSource(dataSource)
        .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
        .sql("INSERT INTO INVOICE_STG " +
                 "(INVOICE_STG_ID, CLIENT_ID, INVOICE_AMT, INVOICE_AMT_SUB_FLG, INVOICE_TYPE, INVOICE_TYPE_SUB_FLG, INVOICE_STATUS) " +
                 "VALUES ((SELECT NEXT VALUE FOR INVOICE_STG_ID_SEQ), :id.clientId, :invoiceAmount, :invoiceAmountSubmissionFlag, :invoiceType, :invoiceTypeSubmissionFlag, :invoiceStatus)")
        .build();
    }
}
