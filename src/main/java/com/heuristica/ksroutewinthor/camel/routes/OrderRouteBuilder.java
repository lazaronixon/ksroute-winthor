package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.apis.Order;
import com.heuristica.ksroutewinthor.models.Filial;
import com.heuristica.ksroutewinthor.models.Pedido;
import com.heuristica.ksroutewinthor.models.Cliente;
import com.heuristica.ksroutewinthor.services.PedidoService;
import com.heuristica.ksroutewinthor.services.RecordService;
import org.apache.camel.Exchange;
import static org.apache.camel.builder.PredicateBuilder.isNotNull;
import static org.apache.camel.builder.PredicateBuilder.isNull;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.util.toolbox.AggregationStrategies;
import org.springframework.stereotype.Component;

@Component
class OrderRouteBuilder extends RouteBuilder {
    
    private static final String ORDERS_URL = "https://{{ksroute.api.url}}/orders.json";
    private static final String ORDER_URL = "https://{{ksroute.api.url}}/orders/${header.remoteId}.json";

    @Override
    public void configure() {        
        from("direct:Event-Save-Pedido").routeId("Event-Save-Pedido")
                .bean(PedidoService.class, "findByEvent")
                .filter(isNotNull(body()))
                .choice().when(isNull(simple("body.record"))).to("direct:post-order")
                .otherwise().to("direct:put-order");
        
        from("direct:Event-Delete-Pedido").routeId("Event-Delete-Pedido")
                .bean(RecordService.class, "findByEvent")
                .filter(isNotNull(body()))
                .to("direct:delete-order");
        
        from("direct:post-order").routeId("post-order")
                .transacted("PROPAGATION_REQUIRES_NEW")
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.HTTP_URI, simple(ORDERS_URL))
                .enrich("direct:enrich-branch", AggregationStrategies.bean(OrderEnricher.class, "setFilial"))
                .enrich("direct:enrich-customer", AggregationStrategies.bean(OrderEnricher.class, "setCliente"))                
                .convertBodyTo(Order.class).marshal().json(JsonLibrary.Jackson)
                .to("direct:ksroute-api").unmarshal().json(JsonLibrary.Jackson, Order.class)
                .bean(PedidoService.class, "saveResponse");

        from("direct:put-order").routeId("put-order")
                .transacted("PROPAGATION_REQUIRES_NEW")
                .setHeader("remoteId", simple("body.record.remoteId"))
                .setHeader(Exchange.HTTP_METHOD, constant("PUT"))
                .setHeader(Exchange.HTTP_URI, simple(ORDER_URL))
                .enrich("direct:enrich-branch", AggregationStrategies.bean(OrderEnricher.class, "setFilial"))
                .enrich("direct:enrich-customer", AggregationStrategies.bean(OrderEnricher.class, "setCliente")) 
                .convertBodyTo(Order.class).marshal().json(JsonLibrary.Jackson)
                .to("direct:ksroute-api").unmarshal().json(JsonLibrary.Jackson, Order.class)
                .bean(PedidoService.class, "saveResponse");  
        
        from("direct:delete-order").routeId("delete-order")
                .transacted("PROPAGATION_REQUIRES_NEW")
                .setHeader("recordId", simple("body.id"))
                .setHeader("remoteId", simple("body.remoteId"))
                .setHeader(Exchange.HTTP_METHOD, constant("DELETE"))
                .setHeader(Exchange.HTTP_URI, simple(ORDER_URL))
                .setBody(constant(null)).to("direct:ksroute-api")
                .bean(RecordService.class, "deleteByRecordId");    
        
        from("direct:enrich-order").routeId("enrich-order")
                .transform(simple("body.pedido"))
                .bean(RecordService.class, "fetchRecord")
                .filter(isNull(simple("body.record")))
                .to("direct:post-order");               
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
