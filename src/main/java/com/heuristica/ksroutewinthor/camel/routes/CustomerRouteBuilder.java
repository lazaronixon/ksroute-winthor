package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.apis.Customer;
import com.heuristica.ksroutewinthor.models.Cliente;
import com.heuristica.ksroutewinthor.models.Praca;
import com.heuristica.ksroutewinthor.services.ClienteService;
import org.apache.camel.Exchange;
import static org.apache.camel.builder.PredicateBuilder.isNull;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.processor.idempotent.MemoryIdempotentRepository;
import org.apache.camel.util.toolbox.AggregationStrategies;
import org.springframework.stereotype.Component;

@Component
class CustomerRouteBuilder extends RouteBuilder {
    
    private static final String POST_URL = "https://{{ksroute.api.url}}/customers.json";
    private static final String PUT_URL = "https://{{ksroute.api.url}}/customers/${body.ksrId}.json";
    private static final String CACHE_KEY = "cliente/${body.codcli}/${body.oraRowscn}";    

    @Override
    public void configure() {
        from("direct:process-cliente").routeId("process-cliente")                                
                .transform(simple("body.cliente"))
                .enrich("direct:process-praca", AggregationStrategies.bean(CustomerEnricher.class, "setPraca"))
                .choice().when(isNull(simple("body.ksrId"))).to("direct:post-customer")
                .otherwise().to("direct:put-customer");

        from("direct:post-customer").routeId("post-customer")
                .transacted("PROPAGATION_REQUIRES_NEW")
                .setHeader(Exchange.HTTP_URI, simple(POST_URL))
                .convertBodyTo(Customer.class).marshal().json(JsonLibrary.Jackson)
                .to("seda:ksroute-api").unmarshal().json(JsonLibrary.Jackson, Customer.class)
                .bean(ClienteService.class, "saveCustomer"); 

        from("direct:put-customer").routeId("put-customer")
                .idempotentConsumer(simple(CACHE_KEY), MemoryIdempotentRepository.memoryIdempotentRepository(50))
                .setHeader(Exchange.HTTP_METHOD, constant("PUT"))
                .setHeader(Exchange.HTTP_URI, simple(PUT_URL))         
                .convertBodyTo(Customer.class).marshal().json(JsonLibrary.Jackson)
                .to("seda:ksroute-api").unmarshal().json(JsonLibrary.Jackson, Customer.class)
                .bean(ClienteService.class, "saveCustomer"); 
                        
    }

    public class CustomerEnricher {

        public Cliente setPraca(Cliente cliente, Praca praca) {
            cliente.setPraca(praca);
            return cliente;
        }
    }

}
