package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.apis.Vehicle;
import com.heuristica.ksroutewinthor.services.RecordService;
import com.heuristica.ksroutewinthor.services.VeiculoService;
import org.apache.camel.Exchange;
import static org.apache.camel.builder.PredicateBuilder.isNull;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
public class VehicleRouteBuilder extends ApplicationRouteBuilder {
    
    private static final String VEHICLES_URL = "https://{{ksroute.api.url}}/vehicles.json";
    private static final String VEHICLE_URL = "https://{{ksroute.api.url}}/vehicles/${property.remoteId}.json";  
    
    @Override
    public void configure() throws Exception {  
        super.configure();
        
        from("direct:event-save-veiculo").routeId("event-save-veiculo")
                .bean(VeiculoService.class, "findByEvent")
                .choice().when(isNull(simple("body.record"))).to("direct:post-vehicle")
                .otherwise().to("direct:put-vehicle");
        
        from("direct:event-delete-veiculo").routeId("event-delete-veiculo")
                .transform(simple("body.deletedRecord"))
                .to("direct:delete-vehicle");
        
        from("direct:post-vehicle").routeId("post-vehicle")
                .transacted("PROPAGATION_REQUIRES_NEW")
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.HTTP_URI, simple(VEHICLES_URL))
                .convertBodyTo(Vehicle.class).marshal().json(JsonLibrary.Jackson)
                .to("direct:ksroute-api").unmarshal().json(JsonLibrary.Jackson, Vehicle.class)
                .bean(VeiculoService.class, "saveResponse");

        from("direct:put-vehicle").routeId("put-vehicle")
                .transacted("PROPAGATION_REQUIRES_NEW")
                .setProperty("remoteId", simple("body.record.remoteId"))
                .setHeader(Exchange.HTTP_METHOD, constant("PUT"))
                .setHeader(Exchange.HTTP_URI, simple(VEHICLE_URL))
                .convertBodyTo(Vehicle.class).marshal().json(JsonLibrary.Jackson)
                .to("direct:ksroute-api").unmarshal().json(JsonLibrary.Jackson, Vehicle.class)
                .bean(VeiculoService.class, "saveResponse");
        
        from("direct:delete-vehicle").routeId("delete-vehicle")
                .transacted("PROPAGATION_REQUIRES_NEW")
                .setProperty("recordId", simple("body.id"))
                .setProperty("remoteId", simple("body.remoteId"))
                .setHeader(Exchange.HTTP_METHOD, constant("DELETE"))
                .setHeader(Exchange.HTTP_URI, simple(VEHICLE_URL))
                .setBody(constant(null)).to("direct:ksroute-api")
                .bean(RecordService.class, "deleteByRecordId");
    }
    
}
