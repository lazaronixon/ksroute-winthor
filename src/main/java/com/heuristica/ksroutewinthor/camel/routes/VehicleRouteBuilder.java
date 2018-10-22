package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.apis.Vehicle;
import com.heuristica.ksroutewinthor.services.RecordService;
import com.heuristica.ksroutewinthor.services.VeiculoService;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import static org.apache.camel.builder.PredicateBuilder.isNotNull;
import static org.apache.camel.builder.PredicateBuilder.isNull;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.common.HttpOperationFailedException;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
public class VehicleRouteBuilder extends RouteBuilder {
    
    private static final String VEHICLES_URL = "https://{{ksroute.api.url}}/vehicles.json";
    private static final String VEHICLE_URL = "https://{{ksroute.api.url}}/vehicles/${header.remoteId}.json";  
    
    @Override
    public void configure() {  
        from("direct:Event-Save-Veiculo").routeId("Event-Save-Veiculo")
                .bean(VeiculoService.class, "findByEvent")
                .filter(isNotNull(body()))
                .choice().when(isNull(simple("body.record"))).to("direct:post-vehicle")
                .otherwise().to("direct:put-vehicle");
        
        from("direct:Event-Delete-Veiculo").routeId("Event-Delete-Veiculo")
                .transform(simple("body.deletedRecord"))
                .filter(isNotNull(body()))
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
                .doTry()
                    .setHeader("remoteId", simple("body.record.remoteId"))
                    .setHeader(Exchange.HTTP_METHOD, constant("PUT"))
                    .setHeader(Exchange.HTTP_URI, simple(VEHICLE_URL))
                    .convertBodyTo(Vehicle.class).marshal().json(JsonLibrary.Jackson)
                    .to("direct:ksroute-api").unmarshal().json(JsonLibrary.Jackson, Vehicle.class)
                    .bean(VeiculoService.class, "saveResponse")
                .doCatch(HttpOperationFailedException.class)
                    .log(LoggingLevel.WARN, "Erro ao alterar: ${body}")
                .end();
        
        from("direct:delete-vehicle").routeId("delete-vehicle")
                .transacted("PROPAGATION_REQUIRES_NEW")
                .doTry()
                    .setHeader("recordId", simple("body.id"))
                    .setHeader("remoteId", simple("body.remoteId"))
                    .setHeader(Exchange.HTTP_METHOD, constant("DELETE"))
                    .setHeader(Exchange.HTTP_URI, simple(VEHICLE_URL))
                    .setBody(constant(null)).to("direct:ksroute-api")
                    .bean(RecordService.class, "deleteByRecordId")
                .doCatch(HttpOperationFailedException.class)
                    .log(LoggingLevel.WARN, "Erro ao apagar: ${body}")
                .end();
    }
    
}
