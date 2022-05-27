package com.cfa.configs.batch;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class JobConfiguration {

  @Autowired
  private JobRegistry jobRegistry; // keep track of the jobs available in the context
  @Autowired
  private JobLauncher jobLauncher; // execute a job and have the associated JobExecution instance
  @Autowired
  private JobRepository jobRepository; // retrieve information from the database and make changes
  @Autowired
  private JobExplorer jobExplorer; // get information on a job, step, jobexecution ....

  @Bean
  public JobRegistryBeanPostProcessor jobRepositoryBeanProcessor() throws Exception {
    final JobRegistryBeanPostProcessor locPostProcessor = new JobRegistryBeanPostProcessor();
    locPostProcessor.setJobRegistry(jobRegistry);
    locPostProcessor.afterPropertiesSet();

    return locPostProcessor;
  }

  @Bean
  public JobOperator jobOperator() {
    final SimpleJobOperator jobOperator = new SimpleJobOperator();
    jobOperator.setJobLauncher(jobLauncher);
    jobOperator.setJobRepository(jobRepository);
    jobOperator.setJobRegistry(jobRegistry);
    jobOperator.setJobExplorer(jobExplorer);
    return jobOperator;
  }

  @Bean
  public JdbcOperations jdbcOperations(final DataSource parDataSource) {
    return new JdbcTemplate(parDataSource);
  }

  @Bean
  public JobExplorer jobExplorer(final DataSource parDataSource) throws Exception {
    final JobExplorerFactoryBean bean = new JobExplorerFactoryBean();
    bean.setDataSource(parDataSource);
    bean.setTablePrefix("BATCH_");
    bean.setJdbcOperations(new JdbcTemplate(parDataSource));
    bean.afterPropertiesSet();
    return bean.getObject();
  }

  @Bean
  public JobLauncher jobLauncher() throws Exception {
    final SimpleJobLauncher locLauncher = new SimpleJobLauncher();
    locLauncher.setJobRepository(jobRepository);
    locLauncher.afterPropertiesSet();

    return locLauncher;
  }

  @Bean
  protected JobRepository createJobRepository(final DataSource dataSource, final PlatformTransactionManager transactionManager) throws Exception {
    final JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
    factory.setDataSource(dataSource);
    factory.setTransactionManager(transactionManager);
    factory.setIsolationLevelForCreate("ISOLATION_READ_UNCOMMITTED");
    factory.afterPropertiesSet();
    return factory.getObject();
  }

  @Bean
  public JobBuilderFactory jobBuilderFactory() throws Exception {
    return new JobBuilderFactory(jobRepository);
  }


  @Bean
  public PlatformTransactionManager getTransactionManager() {
    return new ResourcelessTransactionManager();
  }

}
