package com.heuristica.ksroutewinthor;

import com.heuristica.ksroutewinthor.services.OrderService;
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
            from("jpa:com.heuristica.ksroutewinthor.model.Pedido"
                    + "?consumer.delay=5s"
                    + "&consumer.namedQuery=newOrders"
                    + "&consumeDelete=false"
                    + "&consumeLockEntity=false")
                    .routeId("process-order")
                    .bean(OrderService.class, "processPedido")
                    .log("Pedido processado: #id ${body}");
        }
    }
}
