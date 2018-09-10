package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.models.Filial;
import com.heuristica.ksroutewinthor.models.Pedido;
import com.heuristica.ksroutewinthor.models.Cliente;
import org.apache.camel.util.toolbox.AggregationStrategies;
import org.springframework.stereotype.Component;

@Component
class OrderRouteBuilder extends ApplicationRouteBuilder {

    @Override
    public void configure() {
        super.configure();

        from("jpa:com.heuristica.ksroutewinthor.models.pedido"
                + "?delay=15s"
                + "&namedQuery=newOrders"
                + "&consumeLockEntity=false"
                + "&consumeDelete=false").routeId("process-pedido")
                .log("Processando pedido ${body.numped}")
                .enrich("direct:process-filial", AggregationStrategies.bean(OrderEnricher.class, "setFilial"));
        //.enrich("direct:process-cliente", AggregationStrategies.bean(OrderEnricher.class, "setCliente"));
    }

    public class OrderEnricher {

        public Pedido setFilial(Pedido pedido, Filial filial) {
            pedido.setFilial(filial);
            return pedido;
        }

        public Pedido setCliente(Pedido pedido, Cliente cliente) {
            pedido.setCliente(cliente);
            return pedido;
        }
    }
}
