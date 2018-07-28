package com.heuristica.ksroutewinthor;

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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DozerConfigurator {

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
