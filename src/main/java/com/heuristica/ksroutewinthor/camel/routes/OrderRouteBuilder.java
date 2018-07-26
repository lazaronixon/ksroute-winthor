package com.heuristica.ksroutewinthor.camel.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
class OrderRouteBuilder extends RouteBuilder {

    @Override
    public void configure() {
        from("jpa:com.heuristica.ksroutewinthor.models.Pedido"
                + "?consumer.delay=5s"
                + "&consumer.namedQuery=newOrders"
                + "&consumeDelete=false"
                + "&consumeLockEntity=false")
                .routeId("process-order")
                .to("direct:process-filial")
                .log("Pedido processado: ${body.numped}");
    }
}
