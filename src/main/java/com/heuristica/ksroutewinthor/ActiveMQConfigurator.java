
package com.heuristica.ksroutewinthor;

import java.util.concurrent.TimeUnit;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ActiveMQConfigurator {
    
    @Value("${spring.activemq.broker-url}")
    private String ACTIVEMQ_URL;    
    
    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory() {        
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        activeMQConnectionFactory.setRedeliveryPolicy(defaultRedeliveryPolicy());
        return activeMQConnectionFactory;
    }
    
    private RedeliveryPolicy defaultRedeliveryPolicy() {
        RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
        redeliveryPolicy.setUseExponentialBackOff(true);
        redeliveryPolicy.setBackOffMultiplier(1.6);
        redeliveryPolicy.setMaximumRedeliveries(25);
        redeliveryPolicy.setRedeliveryDelay(TimeUnit.SECONDS.toMillis(25));
        return redeliveryPolicy;
    }    
    
}
