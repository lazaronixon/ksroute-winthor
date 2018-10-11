package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.apis.Order;
import com.heuristica.ksroutewinthor.models.Filial;
import com.heuristica.ksroutewinthor.models.Pedido;
import com.heuristica.ksroutewinthor.models.Cliente;
import com.heuristica.ksroutewinthor.services.PedidoService;
import org.apache.camel.Exchange;
import static org.apache.camel.builder.PredicateBuilder.isNull;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.util.toolbox.AggregationStrategies;
import org.springframework.stereotype.Component;

@Component
class OrderRouteBuilder extends RouteBuilder {
    
    private static final String POST_URL = "https://{{ksroute.api.url}}/orders.json";

    @Override
    public void configure() {        
        from("jpa:com.heuristica.ksroutewinthor.models.Pedido"
                + "?delay=15s"
                + "&namedQuery=newOrders"
                + "&consumeLockEntity=false"
                + "&consumeDelete=false").routeId("process-pedido")
                .log("Processando pedido ${body.numped}")                
                .bean(PedidoService.class, "fetchPedido(${body.numped})")
                .enrich("direct:process-filial", AggregationStrategies.bean(OrderEnricher.class, "setFilial"))
                .enrich("direct:process-cliente", AggregationStrategies.bean(OrderEnricher.class, "setCliente"))
                .filter(isNull(simple("body.ksrId"))).to("direct:post-order");
        
        from("direct:post-order").routeId("post-order")
                .transacted("PROPAGATION_REQUIRES_NEW")                
                .setHeader(Exchange.HTTP_URI, simple(POST_URL))
                .convertBodyTo(Order.class).marshal().json(JsonLibrary.Jackson)
                .to("direct:ksroute-api").unmarshal().json(JsonLibrary.Jackson, Order.class)
                .bean(PedidoService.class, "saveApiResponse");              
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
