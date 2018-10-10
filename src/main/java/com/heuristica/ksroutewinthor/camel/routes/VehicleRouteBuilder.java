package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.apis.Vehicle;
import com.heuristica.ksroutewinthor.services.VeiculoService;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
public class VehicleRouteBuilder extends RouteBuilder {
    
    private static final String POST_URL = "https://{{ksroute.api.url}}/vehicles.json";  
    
    @Override
    public void configure() {
        from("jpa:com.heuristica.ksroutewinthor.models.Vehicle"
                + "?delay=15s"
                + "&namedQuery=newVehicles"
                + "&consumeLockEntity=false"
                + "&consumeDelete=false").routeId("process-vehicle")
                .log("Processando veiculo ${body.codveiculo}")
                .bean(VeiculoService.class, "setFromEnviromentValues")
                .to("direct:post-vehicle");
        
        from("direct:post-vehicle").routeId("post-vehicle")
                .transacted("PROPAGATION_REQUIRES_NEW")
                .setHeader(Exchange.HTTP_URI, simple(POST_URL))
                .convertBodyTo(Vehicle.class).marshal().json(JsonLibrary.Jackson)
                .to("direct:ksroute-api").unmarshal().json(JsonLibrary.Jackson, Vehicle.class)
                .bean(VeiculoService.class, "saveVeiculo");       
    }
    
}
