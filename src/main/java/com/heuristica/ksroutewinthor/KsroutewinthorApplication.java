package com.heuristica.ksroutewinthor;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.heuristica.ksroutewinthor.dozer.mapping.BranchMapping;
import com.heuristica.ksroutewinthor.dozer.mapping.CustomerMapping;
import com.heuristica.ksroutewinthor.dozer.mapping.LineMapping;
import com.heuristica.ksroutewinthor.dozer.mapping.OrderMapping;
import com.heuristica.ksroutewinthor.dozer.mapping.RegionMapping;
import com.heuristica.ksroutewinthor.dozer.mapping.SubregionMapping;
import java.util.Arrays;
import org.apache.camel.CamelContext;
import org.apache.camel.converter.dozer.DozerBeanMapperConfiguration;
import org.apache.camel.converter.dozer.DozerTypeConverterLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class KsroutewinthorApplication {

    public static void main(String[] args) {
        SpringApplication.run(KsroutewinthorApplication.class, args);
    }

    @Bean
    public ObjectMapper defaultJacksonFormat() {
        return new ObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Bean
    public DozerBeanMapperConfiguration mapper() {
        DozerBeanMapperConfiguration dozerConfig = new DozerBeanMapperConfiguration();
        dozerConfig.setBeanMappingBuilders(Arrays.asList(
                new BranchMapping(), new CustomerMapping(), new LineMapping(),
                new OrderMapping(), new RegionMapping(), new SubregionMapping()
        ));
        return dozerConfig;
    }

    @Bean
    public DozerTypeConverterLoader dozerConverterLoader(CamelContext camelContext, DozerBeanMapperConfiguration dozerConfig) {
        return new DozerTypeConverterLoader(camelContext, dozerConfig);
    }

}
