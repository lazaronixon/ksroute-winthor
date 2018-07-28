package com.heuristica.ksroutewinthor;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.apache.camel.model.dataformat.JsonDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfigurator {
    
    @Bean
    public JsonDataFormat json() {
        JsonDataFormat dataFormat = new JsonDataFormat(JsonLibrary.Jackson);
        dataFormat.setObjectMapper("defaultJacksonFormat");
        return dataFormat;
    }

    @Bean
    public ObjectMapper defaultJacksonFormat() {
        return new ObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }    
    
}
