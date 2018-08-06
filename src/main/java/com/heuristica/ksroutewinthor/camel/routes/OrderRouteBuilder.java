package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.models.Filial;
import com.heuristica.ksroutewinthor.models.Pedido;
import org.apache.camel.util.toolbox.AggregationStrategies;
import org.springframework.stereotype.Component;

@Component
class OrderRouteBuilder extends ApplicationRouteBuilder {

    @Override
    public void configure() {
        super.configure();

        from("jpa:com.heuristica.ksroutewinthor.models.Pedido?delay=15s&namedQuery=newOrders&consumeDelete=false").routeId("process-pedido")
                .log("Processando pedido ${body.numped}")
                .enrich("direct:process-filial", AggregationStrategies.bean(OrderEnricher.class, "setBranch"));
    }

    public class OrderEnricher {

        public Pedido setBranch(Pedido pedido, Filial filial) {
            pedido.setFilial(filial);
            return pedido;
        }
    }
}
