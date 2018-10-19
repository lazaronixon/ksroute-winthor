package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.apis.Line;
import com.heuristica.ksroutewinthor.services.RecordService;
import com.heuristica.ksroutewinthor.services.RotaService;
import org.apache.camel.Exchange;
import static org.apache.camel.builder.PredicateBuilder.isNotNull;
import static org.apache.camel.builder.PredicateBuilder.isNull;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
class LineRouteBuilder extends RouteBuilder {
    
    private static final String LINES_URL = "https://{{ksroute.api.url}}/lines.json";
    private static final String LINE_URL = "https://{{ksroute.api.url}}/lines/${header.remoteId}.json"; 

    @Override
    public void configure() {
        from("direct:Event-Save-Rota").routeId("Event-Save-Rota")
                .bean(RotaService.class, "findByEvent")
                .filter(isNotNull(body()))
                .choice().when(isNull(simple("body.record"))).to("direct:post-line")
                .otherwise().to("direct:put-line");
        
        from("direct:Event-Delete-Rota").routeId("Event-Delete-Rota")
                .transform(simple("body.record"))
                .filter(isNotNull(body()))
                .to("direct:delete-line");
        
        from("direct:enrich-line").routeId("enrich-line")
                .transform(simple("body.rota"))
                .filter(isNull(simple("body.record")))
                .to("direct:post-line");
        
        from("direct:post-line").routeId("post-line")
                .transacted("PROPAGATION_REQUIRES_NEW")
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.HTTP_URI, simple(LINES_URL))
                .convertBodyTo(Line.class).marshal().json(JsonLibrary.Jackson)
                .to("direct:ksroute-api").unmarshal().json(JsonLibrary.Jackson, Line.class)
                .bean(RotaService.class, "saveResponse");

        from("direct:put-line").routeId("put-line")
                .transacted("PROPAGATION_REQUIRES_NEW")
                .setHeader("remoteId", simple("body.record.remoteId"))
                .setHeader(Exchange.HTTP_METHOD, constant("PUT"))
                .setHeader(Exchange.HTTP_URI, simple(LINE_URL))
                .convertBodyTo(Line.class).marshal().json(JsonLibrary.Jackson)
                .to("direct:ksroute-api").unmarshal().json(JsonLibrary.Jackson, Line.class)
                .bean(RotaService.class, "saveResponse");  
        
        from("direct:delete-line").routeId("delete-line")
                .transacted("PROPAGATION_REQUIRES_NEW")
                .setHeader("recordId", simple("body.id"))
                .setHeader("remoteId", simple("body.remoteId"))
                .setHeader(Exchange.HTTP_METHOD, constant("DELETE"))
                .setHeader(Exchange.HTTP_URI, simple(LINE_URL))
                .setBody(constant(null)).to("direct:ksroute-api")
                .bean(RecordService.class, "deleteByRecordId"); 
    }

}
