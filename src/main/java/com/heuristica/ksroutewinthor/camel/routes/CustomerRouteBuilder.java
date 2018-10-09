package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.apis.Customer;
import com.heuristica.ksroutewinthor.models.Cliente;
import com.heuristica.ksroutewinthor.models.Praca;
import com.heuristica.ksroutewinthor.services.ClienteService;
import org.apache.camel.Exchange;
import static org.apache.camel.builder.PredicateBuilder.isNull;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.util.toolbox.AggregationStrategies;
import org.springframework.stereotype.Component;

@Component
class CustomerRouteBuilder extends ApplicationRouteBuilder {
    
    private static final String POST_URL = "https4:{{ksroute.api.url}}/customers.json";
    private static final String PUT_URL = "https4:{{ksroute.api.url}}/customers/${header.ksrId}.json";    

    @Override
    public void configure() {
        super.configure();

        from("direct:process-cliente").routeId("process-cliente")
                .transform(simple("body.cliente"))
                .enrich("direct:process-praca", AggregationStrategies.bean(CustomerEnricher.class, "setPraca"))
                .choice().when(isNull(simple("body.ksrId"))).to("direct:post-customer")
                .otherwise().to("direct:put-customer");

        from("direct:post-customer").routeId("post-customer")
                .convertBodyTo(Customer.class).marshal().json(JsonLibrary.Jackson)
                .throttle(MAXIMUM_REQUEST_COUNT).timePeriodMillis(TIME_PERIOD_MILLIS).to(POST_URL)
                .unmarshal().json(JsonLibrary.Jackson, Customer.class)
                .bean(ClienteService.class, "saveCustomer"); 

        from("direct:put-customer").routeId("put-customer")               
                .setHeader(Exchange.HTTP_METHOD, constant("PUT"))
                .setHeader("ksrId", simple("body.ksrId"))              
                .convertBodyTo(Customer.class).marshal().json(JsonLibrary.Jackson)
                .throttle(MAXIMUM_REQUEST_COUNT).timePeriodMillis(TIME_PERIOD_MILLIS).toD(PUT_URL)
                .unmarshal().json(JsonLibrary.Jackson, Customer.class)
                .bean(ClienteService.class, "saveCustomer"); 
                        
    }

    public class CustomerEnricher {

        public Cliente setPraca(Cliente cliente, Praca praca) {
            cliente.setPraca(praca);
            return cliente;
        }
    }

}
