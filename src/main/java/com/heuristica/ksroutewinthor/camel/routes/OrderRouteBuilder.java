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
    
    private static final String POST_URL = "https4:{{ksroute.api.url}}/orders.json";

    @Override
    public void configure() {
        super.configure();
        
        from("jpa:com.heuristica.ksroutewinthor.models.Pedido"
                + "?delay=15s"
                + "&namedQuery=newOrders"
                + "&consumeLockEntity=false"
                + "&consumeDelete=false").routeId("process-pedido")
                .log("Processando pedido ${body.numped}")               
                .bean(PedidoService.class, "findPedido(${body.numped})")
                .enrich("direct:process-filial", AggregationStrategies.bean(OrderEnricher.class, "setFilial"))
                .enrich("direct:process-cliente", AggregationStrategies.bean(OrderEnricher.class, "setCliente"))
                .to("direct:post-order");
        
        from("direct:post-order").routeId("post-order")
                .transacted("PROPAGATION_REQUIRES_NEW")
                .convertBodyTo(Order.class).marshal().json(JsonLibrary.Jackson)
                .throttle(MAXIMUM_REQUEST_COUNT).timePeriodMillis(TIME_PERIOD_MILLIS).to(POST_URL)
                .unmarshal().json(JsonLibrary.Jackson, Order.class)
                .bean(PedidoService.class, "savePedido");              
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
