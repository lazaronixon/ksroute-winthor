package com.heuristica.ksroutewinthor;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class KsroutewinthorApplication {

    public static void main(String[] args) {
        SpringApplication.run(KsroutewinthorApplication.class, args);
    }

    @Component
    class Backend extends RouteBuilder {
        @Override
        public void configure() {
            // Polls the DB for new orders and processes them
            from("jpa:com.heuristica.ksroutewinthor.models.Pedido"
                    + "?consumer.namedQuery=newOrders"
                    + "&consumer.delay=5s"
                    + "&consumeDelete=false")
                    .routeId("process-order")
                    .log("Pedido processado: #id ${body.numped}");
        }
    }
}
