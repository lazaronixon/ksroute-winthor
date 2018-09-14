package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.apis.Order;
import com.heuristica.ksroutewinthor.models.Filial;
import com.heuristica.ksroutewinthor.models.Pedido;
import com.heuristica.ksroutewinthor.models.Cliente;
import com.heuristica.ksroutewinthor.services.PedidoService;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.util.toolbox.AggregationStrategies;
import org.springframework.stereotype.Component;

@Component
class OrderRouteBuilder extends ApplicationRouteBuilder {

    @Override
    public void configure() {
        super.configure();
        from("jpa:com.heuristica.ksroutewinthor.models.Pedido"
                + "?delay=15s"
                + "&namedQuery=newOrders"
                + "&consumeLockEntity=false"
                + "&consumeDelete=false").routeId("process-pedido")
                .log("Processando pedido ${body.numped}")
                .enrich("direct:process-filial", AggregationStrategies.bean(OrderEnricher.class, "setFilial"))
                .enrich("direct:process-cliente", AggregationStrategies.bean(OrderEnricher.class, "setCliente"))
                .to("direct:create-pedido").bean(PedidoService.class, "savePedido(${body})");
        
        from("direct:create-pedido").routeId("create-pedido")
                .filter(simple("${body.ksrId} == null"))             
                .convertBodyTo(Order.class).marshal().json(JsonLibrary.Jackson)
                .throttle(5).to("https4://{{ksroute.api.url}}/orders.json")
                .unmarshal().json(JsonLibrary.Jackson, Order.class);              
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
