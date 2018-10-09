package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.apis.Vehicle;
import com.heuristica.ksroutewinthor.services.VeiculoService;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
public class VehicleRouteBuilder extends ApplicationRouteBuilder {
    
    private static final String POST_URL = "https4:{{ksroute.api.url}}/vehicles.json";  
    
    @Override
    public void configure() {
        super.configure();
        
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
                .convertBodyTo(Vehicle.class).marshal().json(JsonLibrary.Jackson)
                .throttle(MAXIMUM_REQUEST_COUNT).timePeriodMillis(TIME_PERIOD_MILLIS).to(POST_URL)
                .unmarshal().json(JsonLibrary.Jackson, Vehicle.class)
                .bean(VeiculoService.class, "saveVeiculo");       
    }
    
}
