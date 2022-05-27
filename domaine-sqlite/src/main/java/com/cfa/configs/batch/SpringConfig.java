package com.cfa.configs.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * Spring batch & sqlite db configurations
 */
@Configuration
@EnableBatchProcessing
public class SpringConfig {

  @Bean
  public JobRepository getJobRepository(final DataSource dataSource) throws Exception {
    JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
    factory.setDataSource(dataSource);
    factory.setTransactionManager(getTransactionManager());
    factory.afterPropertiesSet();

    return factory.getObject();
  }

  @Bean
  public JobLauncher getJobLauncher(final DataSource dataSource) throws Exception {
    SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
    jobLauncher.setJobRepository(getJobRepository(dataSource));
    jobLauncher.afterPropertiesSet();
    return jobLauncher;
  }

  @Bean
  public PlatformTransactionManager getTransactionManager() {
    return new ResourcelessTransactionManager();
  }

}