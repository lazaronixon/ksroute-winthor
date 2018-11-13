package com.heuristica.ksroutewinthor;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.heuristica.ksroutewinthor.dozer.mappings.BranchMapping;
import com.heuristica.ksroutewinthor.dozer.mappings.CustomerMapping;
import com.heuristica.ksroutewinthor.dozer.mappings.LineMapping;
import com.heuristica.ksroutewinthor.dozer.mappings.OrderMapping;
import com.heuristica.ksroutewinthor.dozer.mappings.RegionMapping;
import com.heuristica.ksroutewinthor.dozer.mappings.SubregionMapping;
import com.heuristica.ksroutewinthor.dozer.mappings.VehicleMapping;
import java.util.Arrays;
import org.apache.camel.CamelContext;
import org.apache.camel.converter.dozer.DozerBeanMapperConfiguration;
import org.apache.camel.converter.dozer.DozerTypeConverterLoader;
import org.apache.camel.spring.spi.SpringTransactionPolicy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.PlatformTransactionManager;

@SpringBootApplication
@EnableJpaAuditing
public class KsroutewinthorApplication {    
    
    public static void main(String[] args) {
        SpringApplication.run(KsroutewinthorApplication.class, args);
    }

    @Bean
    public ObjectMapper jacksonFormat() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Bean
    public DozerBeanMapperConfiguration mapper() {
        DozerBeanMapperConfiguration dozerConfig = new DozerBeanMapperConfiguration();
        dozerConfig.setBeanMappingBuilders(Arrays.asList(
                new BranchMapping(), new CustomerMapping(), new LineMapping(),
                new OrderMapping(), new RegionMapping(), new SubregionMapping(),
                new VehicleMapping()
        ));
        return dozerConfig;
    }

    @Bean
    public DozerTypeConverterLoader dozerConverterLoader(CamelContext camelContext, DozerBeanMapperConfiguration dozerConfig) {
        return new DozerTypeConverterLoader(camelContext, dozerConfig);
    }

    @Bean(name = "PROPAGATION_REQUIRED")
    public SpringTransactionPolicy propagationRequired(PlatformTransactionManager transactionManager) {
        SpringTransactionPolicy policy = new SpringTransactionPolicy();
        policy.setTransactionManager(transactionManager);
        policy.setPropagationBehaviorName("PROPAGATION_REQUIRED");
        return policy;
    }

    @Bean(name = "PROPAGATION_REQUIRES_NEW")
    public SpringTransactionPolicy propagationRequiresNew(PlatformTransactionManager transactionManager) {
        SpringTransactionPolicy policy = new SpringTransactionPolicy();
        policy.setTransactionManager(transactionManager);
        policy.setPropagationBehaviorName("PROPAGATION_REQUIRES_NEW");
        return policy;
    }    
}
