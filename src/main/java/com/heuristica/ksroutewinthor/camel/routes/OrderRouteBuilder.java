package com.heuristica.ksroutewinthor.camel.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
class OrderRouteBuilder extends RouteBuilder {

    @Override
    public void configure() {      
        errorHandler(defaultErrorHandler().logExhausted(false));
        
        from("jpa:com.heuristica.ksroutewinthor.models.Pedido?delay=15s&namedQuery=newOrders&consumeDelete=false")
                .routeId("process-pedido")
                .log("Inicio pedido ${body.numped}")
                .to("direct:process-filial")
                .log("Fim pedido ${body.numped}");
    }
}
