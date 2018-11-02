package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.apis.Order;
import com.heuristica.ksroutewinthor.models.Filial;
import com.heuristica.ksroutewinthor.models.Pedido;
import com.heuristica.ksroutewinthor.models.Cliente;
import com.heuristica.ksroutewinthor.services.PedidoService;
import com.heuristica.ksroutewinthor.services.RecordService;
import org.apache.camel.Exchange;
import static org.apache.camel.builder.PredicateBuilder.isNull;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.util.toolbox.AggregationStrategies;
import org.springframework.stereotype.Component;

@Component
class OrderRouteBuilder extends ApplicationRouteBuilder {
    
    private static final String ORDERS_URL = "https://{{ksroute.api.url}}/orders.json";
    private static final String ORDER_URL = "https://{{ksroute.api.url}}/orders/${header.remoteId}.json";

    @Override
    public void configure() throws Exception {     
        super.configure();
        
        from("direct:event-save-pedido").routeId("event-save-pedido")
                .bean(PedidoService.class, "findByEvent")
                .choice().when(isNull(simple("body.record"))).to("direct:post-order")
                .otherwise().to("direct:put-order");
        
        from("direct:event-delete-pedido").routeId("event-delete-pedido")
                .transform(simple("body.deletedRecord"))
                .to("direct:delete-order");
        
        from("direct:post-order").routeId("post-order")
                .transacted("PROPAGATION_REQUIRES_NEW")
                .enrich("direct:enrich-branch", AggregationStrategies.bean(OrderEnricher.class, "setFilial"))
                .enrich("direct:enrich-customer", AggregationStrategies.bean(OrderEnricher.class, "setCliente"))                  
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.HTTP_URI, simple(ORDERS_URL))              
                .convertBodyTo(Order.class).marshal().json(JsonLibrary.Jackson)
                .to("direct:ksroute-api").unmarshal().json(JsonLibrary.Jackson, Order.class)
                .bean(PedidoService.class, "saveResponse");

        from("direct:put-order").routeId("put-order")
                .transacted("PROPAGATION_REQUIRES_NEW")
                .enrich("direct:enrich-branch", AggregationStrategies.bean(OrderEnricher.class, "setFilial"))
                .enrich("direct:enrich-customer", AggregationStrategies.bean(OrderEnricher.class, "setCliente"))
                .setHeader("remoteId", simple("body.record.remoteId"))
                .setHeader(Exchange.HTTP_METHOD, constant("PUT"))
                .setHeader(Exchange.HTTP_URI, simple(ORDER_URL))
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
