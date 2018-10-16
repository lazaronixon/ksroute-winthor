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
    
    private static final String ORDERS_URL = "https://{{ksroute.api.url}}/orders.json";
    private static final String ORDER_URL = "https://{{ksroute.api.url}}/orders/${body.ksrId}.json";

    @Override
    public void configure() {        
        from("direct:save-order").routeId("save-order")
                .bean(PedidoService.class, "getEventable").filter(body().isNotNull())
                .enrich("direct:enrich-branch", AggregationStrategies.bean(OrderEnricher.class, "setFilial"))
                .enrich("direct:enrich-customer", AggregationStrategies.bean(OrderEnricher.class, "setCliente"))
                .choice().when(isNull(simple("body.ksrId"))).to("direct:post-order")
                .otherwise().to("direct:put-order");
        
        from("direct:post-order").routeId("post-order")
                .transacted("PROPAGATION_REQUIRES_NEW")
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.HTTP_URI, simple(ORDERS_URL))
                .convertBodyTo(Order.class).marshal().json(JsonLibrary.Jackson)
                .to("seda:ksroute-api").unmarshal().json(JsonLibrary.Jackson, Order.class)
                .bean(PedidoService.class, "saveApiResponse");

        from("direct:put-order").routeId("put-order")
                .setHeader(Exchange.HTTP_METHOD, constant("PUT"))
                .setHeader(Exchange.HTTP_URI, simple(ORDER_URL))
                .convertBodyTo(Order.class).marshal().json(JsonLibrary.Jackson)
                .to("seda:ksroute-api");

        from("direct:delete-order").routeId("delete-order")
                .setHeader(Exchange.HTTP_METHOD, constant("DELETE"))
                .setHeader(Exchange.HTTP_URI, simple(ORDER_URL))
                .setBody(constant(null)).to("seda:ksroute-api");            
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
