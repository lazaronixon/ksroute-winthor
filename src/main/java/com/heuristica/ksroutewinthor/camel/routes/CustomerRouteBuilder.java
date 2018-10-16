package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.apis.Customer;
import com.heuristica.ksroutewinthor.models.Cliente;
import com.heuristica.ksroutewinthor.models.Praca;
import com.heuristica.ksroutewinthor.services.ClienteService;
import org.apache.camel.Exchange;
import static org.apache.camel.builder.PredicateBuilder.isNull;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.util.toolbox.AggregationStrategies;
import org.springframework.stereotype.Component;

@Component
class CustomerRouteBuilder extends RouteBuilder {
    
    private static final String CUSTOMERS_URL = "https://{{ksroute.api.url}}/customers.json";
    private static final String CUSTOMER_URL = "https://{{ksroute.api.url}}/customers/${body.ksrId}.json"; 

    @Override
    public void configure() {
        from("direct:save-customer").routeId("save-customer")
                .bean(ClienteService.class, "getEventable")
                .enrich("direct:enrich-subregion", AggregationStrategies.bean(CustomerEnricher.class, "setPraca"))
                .choice().when(isNull(simple("body.ksrId"))).to("direct:post-customer")
                .otherwise().to("direct:put-customer");
        
        from("direct:enrich-customer").routeId("enrich-customer")
                .transform(simple("body.cliente"))
                .enrich("direct:enrich-subregion", AggregationStrategies.bean(CustomerEnricher.class, "setPraca"))
                .filter(isNull(simple("body.ksrId"))).to("direct:post-customer");            
        
        from("direct:post-customer").routeId("post-customer")
                .transacted("PROPAGATION_REQUIRES_NEW")
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.HTTP_URI, simple(CUSTOMERS_URL))
                .convertBodyTo(Customer.class).marshal().json(JsonLibrary.Jackson)
                .to("direct:ksroute-api").unmarshal().json(JsonLibrary.Jackson, Customer.class)
                .bean(ClienteService.class, "saveApiResponse");

        from("direct:put-customer").routeId("put-customer")
                .setHeader(Exchange.HTTP_METHOD, constant("PUT"))
                .setHeader(Exchange.HTTP_URI, simple(CUSTOMER_URL))
                .convertBodyTo(Customer.class).marshal().json(JsonLibrary.Jackson)
                .to("direct:ksroute-api");

        from("direct:delete-customer").routeId("delete-customer")
                .setHeader(Exchange.HTTP_METHOD, constant("DELETE"))
                .setHeader(Exchange.HTTP_URI, simple(CUSTOMER_URL))
                .setBody(constant(null)).to("direct:ksroute-api");      
    }

    public class CustomerEnricher {

        public Cliente setPraca(Cliente cliente, Praca praca) {
            cliente.setPraca(praca);
            return cliente;
        }
    }

}
