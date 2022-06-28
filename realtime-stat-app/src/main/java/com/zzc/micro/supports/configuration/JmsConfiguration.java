package com.zzc.micro.supports.configuration;

import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.MessageTransformer;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.jms.JmsPoolConnectionFactoryProperties;
import org.springframework.boot.autoconfigure.jms.JmsProperties;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.*;
import java.net.InetAddress;

/**
 * <p>Filename: com.zzc.micro.supports.configuration.JmsConfiguration.java</p>
 * <p>Date: 2022-06-27, 周一, 19:19:49.</p>
 *
 * @author zhichuanzhang
 * @version 0.1.0
 */
@Slf4j
@EnableJms
@Configuration
@EnableConfigurationProperties(ActiveMQProperties.class)
public class JmsConfiguration {

    @Configuration
    static class ConnectionFactoryConfiguration {
        @Value("${info.app.name:realtime_statistics}")
        private String appName;

        @Bean
        @ConditionalOnMissingBean
        public MessageTransformer messageTransformer() {
            return new MessageTransformer() {
                @Override
                public Message producerTransform(Session session, MessageProducer producer, Message message) throws JMSException {
                    message.setStringProperty("senderApp", appName);
                    String ip = "127.0.0.1";
                    try {
                        ip = InetAddress.getLocalHost().getHostAddress();
                    } catch (Throwable t) {
                        log.error(t.getMessage());
                    }
                    message.setStringProperty("senderIp", ip);
                    return message;
                }

                @Override
                public Message consumerTransform(Session session, MessageConsumer consumer, Message message) {
                    return message;
                }
            };
        }

        @Primary
        @Bean(name = {"producerConnectionFactory", "queueConsumerConnectionFactory", "jmsConnectionFactory"}, destroyMethod = "stop")
        @ConditionalOnProperty(prefix = "spring.activemq.pool", name = "enabled", havingValue = "true")
        public PooledConnectionFactory producerPoolConnectionFactory(final ActiveMQProperties properties) {
            return this.createPoolConnectionFactory(properties, this.createNativeConnectionFactory(properties));
        }

        @Primary
        @Bean(name = {"producerConnectionFactory", "queueConsumerConnectionFactory", "jmsConnectionFactory"})
        @ConditionalOnProperty(prefix = "spring.activemq.pool", name = "enabled", havingValue = "false", matchIfMissing = true)
        public ConnectionFactory producerConnectionFactory(final ActiveMQProperties properties) {
            return this.createNativeConnectionFactory(properties);
        }

        @Bean(name = "topicConsumerConnectionFactory")
        public ConnectionFactory topicConsumerPoolConnectionFactory(final ActiveMQProperties properties) {
            return this.createNativeConnectionFactory(properties);
        }

        private PooledConnectionFactory createPoolConnectionFactory(final ActiveMQProperties properties, final ConnectionFactory connectionFactory) {
            JmsPoolConnectionFactoryProperties pool = properties.getPool();
            final PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory() {
            };
            pooledConnectionFactory.setConnectionFactory(connectionFactory);
            pooledConnectionFactory.setMaxConnections(pool.getMaxConnections());
            pooledConnectionFactory.setIdleTimeout(pool.getIdleTimeout().getNano());
            pooledConnectionFactory.setExpiryTimeout(properties.getCloseTimeout().getNano());
            return pooledConnectionFactory;
        }

        private ActiveMQConnectionFactory createNativeConnectionFactory(final ActiveMQProperties properties) {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
            connectionFactory.setBrokerURL(properties.getBrokerUrl());
            connectionFactory.setUserName(properties.getUser());
            connectionFactory.setPassword(properties.getPassword());
            connectionFactory.setTransformer(this.messageTransformer());
            if (properties.getCloseTimeout() != null) {
                connectionFactory.setCloseTimeout((int) properties.getCloseTimeout().toMillis());
            }
            connectionFactory.setNonBlockingRedelivery(properties.isNonBlockingRedelivery());

            if (properties.getSendTimeout() != null) {
                connectionFactory.setSendTimeout((int) properties.getSendTimeout().toMillis());
            }
            ActiveMQProperties.Packages packages = properties.getPackages();
            if (packages.getTrustAll() != null) {
                connectionFactory.setTrustAllPackages(packages.getTrustAll());
            }
            if (!packages.getTrusted().isEmpty()) {
                connectionFactory.setTrustedPackages(packages.getTrusted());
            }

            return connectionFactory;
        }
    }

    @Configuration
    static class ListenerContainerFactoryConfiguration {

        @Value("${info.app.name:realtime_statistics}")
        private String appName;

        @Bean(name = JmsListenerNames.userChangeTopicListenerContainerFactory)
        public JmsListenerContainerFactory<?> userChangeTopicListenerContainerFactory(
                @Qualifier("topicConsumerConnectionFactory") final ConnectionFactory connectionFactory,
                final DefaultJmsListenerContainerFactoryConfigurer configurer) {
            return this.createTopicJmsListenerContainerFactory(configurer, connectionFactory, "userChangeListener");
        }

        @Bean(name = JmsListenerNames.shopChangeTopicListenerContainerFactory)
        public JmsListenerContainerFactory<?> shopChangeTopicListenerContainerFactory(
                @Qualifier("topicConsumerConnectionFactory") final ConnectionFactory connectionFactory,
                final DefaultJmsListenerContainerFactoryConfigurer configurer) {
            return this.createTopicJmsListenerContainerFactory(configurer, connectionFactory, "shopChangeListener");
        }

        @Bean(name = JmsListenerNames.tradeOperateTopicListenerContainerFactory)
        public JmsListenerContainerFactory<?> tradeOperateTopicListenerContainerFactory(
                @Qualifier("topicConsumerConnectionFactory") final ConnectionFactory connectionFactory,
                final DefaultJmsListenerContainerFactoryConfigurer configurer) {
            return this.createTopicJmsListenerContainerFactory(configurer, connectionFactory, "tradeOperateListener");
        }


        private DefaultJmsListenerContainerFactory createTopicJmsListenerContainerFactory(final DefaultJmsListenerContainerFactoryConfigurer configurer,
                final ConnectionFactory connectionFactory,
                final String uniqueClientId) {
            DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
            configurer.configure(factory, connectionFactory);
            factory.setClientId(String.join(":", appName, uniqueClientId));
            factory.setSessionAcknowledgeMode(JmsProperties.AcknowledgeMode.CLIENT.getMode());
            factory.setSubscriptionDurable(true);
            factory.setSessionTransacted(false);
            factory.setPubSubDomain(true);
            return factory;
        }
    }

    @Bean(name = "topicTemplate")
    public JmsTemplate topicTemplate(@Qualifier("producerConnectionFactory") final ConnectionFactory connectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setPubSubDomain(true);
        return jmsTemplate;
    }

    @Bean(name = "queueTemplate")
    public JmsTemplate queueTemplate(@Qualifier("producerConnectionFactory") final ConnectionFactory connectionFactory) {
        return new JmsTemplate(connectionFactory);
    }
}
