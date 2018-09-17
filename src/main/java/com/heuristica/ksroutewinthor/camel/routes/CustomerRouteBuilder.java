package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.apis.Customer;
import com.heuristica.ksroutewinthor.models.Cliente;
import com.heuristica.ksroutewinthor.models.Praca;
import com.heuristica.ksroutewinthor.services.ClienteService;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.util.toolbox.AggregationStrategies;
import org.springframework.stereotype.Component;

@Component
class CustomerRouteBuilder extends ApplicationRouteBuilder {

    @Override
    public void configure() {
        super.configure();

        from("direct:process-cliente").routeId("process-cliente")
                .transform(simple("body.cliente"))
                .enrich("direct:process-praca", AggregationStrategies.bean(CustomerEnricher.class, "setPraca"))
                .choice().when(simple("${body.ksrId} == null")).to("direct:create-cliente")
                .otherwise().to("direct:update-cliente");

        from("direct:create-cliente").routeId("create-cliente")
                .convertBodyTo(Customer.class).marshal().json(JsonLibrary.Jackson)
                .throttle(5).to("https4://{{ksroute.api.url}}/customers.json")
                .unmarshal().json(JsonLibrary.Jackson, Customer.class)
                .bean(ClienteService.class, "saveCustomer(${body})"); 

        from("direct:update-cliente").routeId("update-cliente")               
                .setHeader("CamelHttpMethod", constant("PUT"))
                .setHeader("ksrId", simple("body.ksrId"))                
                .convertBodyTo(Customer.class).marshal().json(JsonLibrary.Jackson)
                .throttle(5).recipientList(simple("https4://{{ksroute.api.url}}/customers/${header.ksrId}.json"))
                .unmarshal().json(JsonLibrary.Jackson, Customer.class)
                .bean(ClienteService.class, "saveCustomer(${body})");       
    }

    public class CustomerEnricher {

        public Cliente setPraca(Cliente cliente, Praca praca) {
            cliente.setPraca(praca);
            return cliente;
        }
    }

}
