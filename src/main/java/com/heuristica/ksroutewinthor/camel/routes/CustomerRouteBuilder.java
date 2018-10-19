package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.apis.Customer;
import com.heuristica.ksroutewinthor.models.Cliente;
import com.heuristica.ksroutewinthor.models.Praca;
import com.heuristica.ksroutewinthor.services.ClienteService;
import com.heuristica.ksroutewinthor.services.RecordService;
import org.apache.camel.Exchange;
import static org.apache.camel.builder.PredicateBuilder.isNotNull;
import static org.apache.camel.builder.PredicateBuilder.isNull;
import static org.apache.camel.builder.PredicateBuilder.not;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.util.toolbox.AggregationStrategies;
import org.springframework.stereotype.Component;

@Component
class CustomerRouteBuilder extends RouteBuilder {
    
    private static final String CUSTOMERS_URL = "https://{{ksroute.api.url}}/customers.json";
    private static final String CUSTOMER_URL = "https://{{ksroute.api.url}}/customers/${header.remoteId}.json"; 

    @Override
    public void configure() {    
        from("direct:Event-Save-Cliente").routeId("Event-Save-Cliente")
                .bean(ClienteService.class, "findByEvent")
                .filter(isNotNull(body()))
                .choice().when(isNull(simple("body.record"))).to("direct:post-customer")
                .otherwise().to("direct:put-customer");
        
        from("direct:Event-Delete-Cliente").routeId("Event-Delete-Cliente")
                .bean(RecordService.class, "findByEvent")
                .filter(isNotNull(body()))
                .to("direct:delete-customer");
        
        from("direct:enrich-customer").routeId("enrich-customer")
                .transform(simple("body.cliente"))
                .filter(not(method(RecordService.class, "existisByRecordable")))
                .to("direct:post-customer");           
        
        from("direct:post-customer").routeId("post-customer")
                .transacted("PROPAGATION_REQUIRES_NEW")
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.HTTP_URI, simple(CUSTOMERS_URL))
                .enrich("direct:enrich-subregion", AggregationStrategies.bean(CustomerEnricher.class, "setPraca"))
                .convertBodyTo(Customer.class).marshal().json(JsonLibrary.Jackson)
                .to("direct:ksroute-api").unmarshal().json(JsonLibrary.Jackson, Customer.class)
                .bean(ClienteService.class, "saveResponse");

        from("direct:put-customer").routeId("put-customer")
                .transacted("PROPAGATION_REQUIRES_NEW")
                .setHeader("remoteId", simple("body.record.remoteId"))
                .setHeader(Exchange.HTTP_METHOD, constant("PUT"))
                .setHeader(Exchange.HTTP_URI, simple(CUSTOMER_URL))
                .enrich("direct:enrich-subregion", AggregationStrategies.bean(CustomerEnricher.class, "setPraca"))
                .convertBodyTo(Customer.class).marshal().json(JsonLibrary.Jackson)
                .to("direct:ksroute-api").unmarshal().json(JsonLibrary.Jackson, Customer.class)
                .bean(ClienteService.class, "saveResponse");  
        
        from("direct:delete-customer").routeId("delete-customer")
                .transacted("PROPAGATION_REQUIRES_NEW")
                .setHeader("recordId", simple("body.id"))
                .setHeader("remoteId", simple("body.remoteId"))
                .setHeader(Exchange.HTTP_METHOD, constant("DELETE"))
                .setHeader(Exchange.HTTP_URI, simple(CUSTOMER_URL))
                .setBody(constant(null)).to("direct:ksroute-api")
                .bean(RecordService.class, "deleteByRecordId");      
    }

    public class CustomerEnricher {

        public Cliente setPraca(Cliente cliente, Praca praca) {
            cliente.setPraca(praca);
            return cliente;
        }
    }

}
