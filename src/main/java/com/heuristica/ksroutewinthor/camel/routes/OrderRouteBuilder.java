package com.heuristica.ksroutewinthor.camel.routes;

import org.springframework.stereotype.Component;

@Component
class OrderRouteBuilder extends ApplicationRouteBuilder {

    @Override
    public void configure() { 
        super.configure();
        
        from("jpa:com.heuristica.ksroutewinthor.models.Pedido?delay=15s&namedQuery=newOrders&consumeDelete=false")
                .routeId("process-pedido")
                .log(">>>>>>> Inicio pedido ${body.numped}")
                .to("direct:process-filial")
                .log(">>>>>>> Fim pedido ${body.numped}");
    }
}
