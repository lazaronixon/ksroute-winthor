package com.heuristica.ksroutewinthor;

import com.heuristica.ksroutewinthor.dozer.mappings.BranchMapping;
import com.heuristica.ksroutewinthor.dozer.mappings.CustomerMapping;
import com.heuristica.ksroutewinthor.dozer.mappings.LineMapping;
import com.heuristica.ksroutewinthor.dozer.mappings.OrderMapping;
import com.heuristica.ksroutewinthor.dozer.mappings.RegionMapping;
import com.heuristica.ksroutewinthor.dozer.mappings.SubregionMapping;
import java.util.Arrays;
import org.apache.camel.converter.dozer.DozerBeanMapperConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class KsroutewinthorApplication {

    public static void main(String[] args) {
        SpringApplication.run(KsroutewinthorApplication.class, args);
    }

    @Bean(name = "mapper")
    public DozerBeanMapperConfiguration mapper() {
        DozerBeanMapperConfiguration beanMapperConfiguration = new DozerBeanMapperConfiguration();
        beanMapperConfiguration.setBeanMappingBuilders(Arrays.asList(
                new BranchMapping(), new CustomerMapping(), new LineMapping(),
                new OrderMapping(), new RegionMapping(), new SubregionMapping()
        ));
        return beanMapperConfiguration;
    }

}
