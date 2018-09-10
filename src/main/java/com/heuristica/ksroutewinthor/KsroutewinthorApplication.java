package com.heuristica.ksroutewinthor;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.heuristica.ksroutewinthor.dozer.mappings.BranchMapping;
import com.heuristica.ksroutewinthor.dozer.mappings.CustomerMapping;
import com.heuristica.ksroutewinthor.dozer.mappings.LineMapping;
import com.heuristica.ksroutewinthor.dozer.mappings.OrderMapping;
import com.heuristica.ksroutewinthor.dozer.mappings.RegionMapping;
import com.heuristica.ksroutewinthor.dozer.mappings.SubregionMapping;
import java.util.Arrays;
import org.apache.camel.CamelContext;
import org.apache.camel.component.http4.HttpComponent;
import org.apache.camel.converter.dozer.DozerBeanMapperConfiguration;
import org.apache.camel.converter.dozer.DozerTypeConverterLoader;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class KsroutewinthorApplication {

    public static void main(String[] args) {
        SpringApplication.run(KsroutewinthorApplication.class, args);
    }

    @Bean
    public ObjectMapper jacksonFormat() {
        return new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
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

    @Bean
    public boolean httpClientConfigurer(@Autowired CamelContext camelContext, @Autowired Environment env) {
        HttpComponent httpComponent = camelContext.getComponent("http4", HttpComponent.class);
        httpComponent.setClientConnectionManager(new PoolingHttpClientConnectionManager());
        httpComponent.setHttpClientConfigurer(httpClientBuilder -> {
            httpClientBuilder
                    .disableCookieManagement()
                    .setDefaultHeaders(Arrays.asList(
                            new BasicHeader("X-User-Email", env.getProperty("ksroute.api.email")),
                            new BasicHeader("X-User-Token", env.getProperty("ksroute.api.token"))));
        });
        return true;
    }
}
