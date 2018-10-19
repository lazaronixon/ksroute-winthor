package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.apis.Region;
import com.heuristica.ksroutewinthor.services.RecordService;
import com.heuristica.ksroutewinthor.services.RegiaoService;
import org.apache.camel.Exchange;
import static org.apache.camel.builder.PredicateBuilder.isNotNull;
import static org.apache.camel.builder.PredicateBuilder.isNull;
import static org.apache.camel.builder.PredicateBuilder.not;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
class RegionRouteBuilder extends RouteBuilder {
    
    private static final String REGIONS_URL = "https://{{ksroute.api.url}}/regions.json";
    private static final String REGION_URL = "https://{{ksroute.api.url}}/regions/${header.remoteId}.json";

    @Override
    public void configure() {
        from("direct:Event-Save-Regiao").routeId("Event-Save-Regiao")
                .bean(RegiaoService.class, "findByEvent")
                .filter(isNotNull(body()))
                .choice().when(isNull(simple("body.record"))).to("direct:post-region")
                .otherwise().to("direct:put-region");
        
        from("direct:Event-Delete-Regiao").routeId("Event-Delete-Regiao")
                .bean(RecordService.class, "findByEvent")
                .filter(isNotNull(body()))
                .to("direct:delete-region");
        
        from("direct:enrich-region").routeId("enrich-region")
                .transform(simple("body.regiao"))
                .filter(not(method(RecordService.class, "existisByRecordable")))
                .to("direct:post-region");   
        
        from("direct:post-region").routeId("post-region")
                .transacted("PROPAGATION_REQUIRES_NEW")
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.HTTP_URI, simple(REGIONS_URL))
                .convertBodyTo(Region.class).marshal().json(JsonLibrary.Jackson)
                .to("direct:ksroute-api").unmarshal().json(JsonLibrary.Jackson, Region.class)
                .bean(RegiaoService.class, "saveResponse");

        from("direct:put-region").routeId("put-region")
                .transacted("PROPAGATION_REQUIRES_NEW")
                .setHeader("remoteId", simple("body.record.remoteId"))
                .setHeader(Exchange.HTTP_METHOD, constant("PUT"))
                .setHeader(Exchange.HTTP_URI, simple(REGION_URL))
                .convertBodyTo(Region.class).marshal().json(JsonLibrary.Jackson)
                .to("direct:ksroute-api").unmarshal().json(JsonLibrary.Jackson, Region.class)
                .bean(RegiaoService.class, "saveResponse");  
        
        from("direct:delete-region").routeId("delete-region")
                .transacted("PROPAGATION_REQUIRES_NEW")
                .setHeader("recordId", simple("body.id"))
                .setHeader("remoteId", simple("body.remoteId"))
                .setHeader(Exchange.HTTP_METHOD, constant("DELETE"))
                .setHeader(Exchange.HTTP_URI, simple(REGION_URL))
                .setBody(constant(null)).to("direct:ksroute-api")
                .bean(RecordService.class, "deleteByRecordId");       
    }
}
