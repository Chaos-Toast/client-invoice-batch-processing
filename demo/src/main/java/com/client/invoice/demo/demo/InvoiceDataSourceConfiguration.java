package com.client.invoice.demo.demo;

import javax.sql.DataSource;

import org.springframework.batch.core.DefaultJobKeyGenerator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.dao.DefaultExecutionContextSerializer;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.item.database.support.DefaultDataFieldMaxValueIncrementerFactory;
import org.springframework.batch.support.DatabaseType;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;
import org.springframework.jdbc.support.incrementer.H2SequenceMaxValueIncrementer;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import com.client.invoice.demo.demo.Persistance.InvoiceTypeRef;

@Configuration
public class InvoiceDataSourceConfiguration {


    @Bean
    public DataSource dataSource() {
        EmbeddedDatabaseBuilder embeddedDatabaseBuilder = new EmbeddedDatabaseBuilder();
        DataSource dataSource = embeddedDatabaseBuilder.addScript("classpath:org/springframework/batch/core/schema-drop-h2.sql")
                .addScript("classpath:org/springframework/batch/core/schema-h2.sql")
                .setType(EmbeddedDatabaseType.H2)
                .build();
        return dataSource;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        // Set annotated classes 
        sessionFactory.setAnnotatedClasses(InvoiceTypeRef.class); 
        return sessionFactory;
    }

    @Bean
        public ResourcelessTransactionManager transactionManager() {
        return new ResourcelessTransactionManager();
    }

    @Bean
    public JobRepository jobRepository(DataSource dataSource) throws Exception {

        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDatabaseType(DatabaseType.H2.getProductName());
        factory.setDataSource(dataSource());
        factory.setTransactionManager(transactionManager());
        factory.setIncrementerFactory(new DefaultDataFieldMaxValueIncrementerFactory(dataSource) {
			@Override
			public DataFieldMaxValueIncrementer getIncrementer(String incrementerType, String incrementerName) {
                return new H2SequenceMaxValueIncrementer(dataSource, incrementerName);
			}
        }
		);
        factory.setJobKeyGenerator(new DefaultJobKeyGenerator());
        factory.setJdbcOperations(new JdbcTemplate(dataSource));
        factory.setConversionService(new DefaultFormattingConversionService());
        factory.setSerializer(new DefaultExecutionContextSerializer());
        return factory.getObject();
    } 

}
