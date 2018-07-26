package com.heuristica.ksroutewinthor;

import com.google.gson.FieldNamingPolicy;
import org.apache.camel.component.gson.GsonDataFormat;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class KsroutewinthorApplication {

    public static void main(String[] args) {
        SpringApplication.run(KsroutewinthorApplication.class, args);
    }

    @Bean
    public GsonDataFormat defaultGson() {
        GsonDataFormat dataFormat = new GsonDataFormat();
        dataFormat.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return dataFormat;
    }

}
