package com.cfa.jobs;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.integration.chunk.RemoteChunkingManagerStepBuilderFactory;
import org.springframework.batch.integration.config.annotation.EnableBatchIntegration;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.kafka.inbound.KafkaMessageDrivenChannelAdapter;
import org.springframework.integration.kafka.outbound.KafkaProducerMessageHandler;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;

/**
 * Configuration example for creating a job & Step with tasklet
 */
@Configuration
@EnableBatchProcessing
@EnableBatchIntegration
@EnableIntegration
public class MasterLetterJobConfiguration {

    public static String TOPIC_SEND = "workern-send";
    public static String TOPIC_RECEIVE = "workern-receive";
    public static String GROUP_ID = "product";
    @Autowired
    private KafkaTemplate kafkaTemplate;
    @Autowired
    private ConsumerFactory kafkaFactory;
    @Autowired
    public JobBuilderFactory jobBuilderFactory;
    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    @Autowired
    public StepBuilderFactory stepBuilderFactoryList;
    @Autowired
    private RemoteChunkingManagerStepBuilderFactory managerStepBuilderFactory;


    @Bean
    public DirectChannel requests() {
        return new DirectChannel();
    }

    @Bean
    public QueueChannel replies() {
        return new QueueChannel();
    }


    @Bean
    public IntegrationFlow inboundFlow() {
        final ContainerProperties containerProps = new ContainerProperties(TOPIC_RECEIVE);
        containerProps.setGroupId(GROUP_ID);

        final KafkaMessageListenerContainer container = new KafkaMessageListenerContainer(kafkaFactory, containerProps);
        final KafkaMessageDrivenChannelAdapter kafkaMessageChannel = new KafkaMessageDrivenChannelAdapter(container);

        return IntegrationFlows
                .from(kafkaMessageChannel)
                .channel(replies())
                .get();
    }

    @Bean
    public IntegrationFlow outboundFlow() {
        final KafkaProducerMessageHandler kafkaMessageHandler = new KafkaProducerMessageHandler(kafkaTemplate);
        kafkaMessageHandler.setTopicExpression(new LiteralExpression(TOPIC_SEND));
        return IntegrationFlows
                .from(requests())
                .handle(kafkaMessageHandler)
                .get();
    }


    public class PassThroughLineMapper implements LineMapper<String> {
        @Override
        public String mapLine(String line, int lineNumber) throws Exception {
            return line;
        }
    }
    protected FlatFileItemReader<String> reader(){
        FlatFileItemReader<String> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource("/Users/aziz__abderrahmen/Desktop/sample.txt"));
        reader.setLineMapper(new PassThroughLineMapper());
        return reader;
    }


    @Bean
    public Job masterLetterJob() {
        return jobBuilderFactory
                .get("masterLetterJob")
                .start(masterLetterStep())
                .build();
    }

    @Bean
    public Step masterLetterStep() {
        return this.managerStepBuilderFactory.get("masterLetterStep")
                .<String, String>chunk(10)
                .reader(reader())
                .outputChannel(requests())
                .inputChannel(replies())
                .build();
    }
}
