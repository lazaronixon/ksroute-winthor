package com.heuristica.ksroutewinthor;

import java.util.Arrays;
import org.apache.camel.CamelContext;
import org.apache.camel.converter.dozer.DozerBeanMapperConfiguration;
import org.apache.camel.converter.dozer.DozerTypeConverterLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DozerConfigurator {
    
    @Bean
    public DozerBeanMapperConfiguration mapper() {
        DozerBeanMapperConfiguration dozerConfig = new DozerBeanMapperConfiguration();
        dozerConfig.setMappingFiles(Arrays.asList("dozerBeanMapping.xml"));
        return dozerConfig;
    }       

    @Bean
    public DozerTypeConverterLoader dozerConverterLoader(CamelContext camelContext, DozerBeanMapperConfiguration dozerConfig) {
        return new DozerTypeConverterLoader(camelContext, dozerConfig);
    }    
    
}
