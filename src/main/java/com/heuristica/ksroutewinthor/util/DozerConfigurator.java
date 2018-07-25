package com.heuristica.ksroutewinthor.util;

import com.heuristica.ksroutewinthor.dozer.OrderMapping;
import org.dozer.DozerBeanMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DozerConfigurator {

    @Bean
    public DozerBeanMapper dozerBean() {
        DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();
        dozerBeanMapper.addMapping(new OrderMapping());
        return dozerBeanMapper;
    }
}
