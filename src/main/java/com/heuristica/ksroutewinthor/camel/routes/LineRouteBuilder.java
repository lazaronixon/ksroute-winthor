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
    private static final String LINE_URL = "https://{{ksroute.api.url}}/lines/${header.resourceId}.json"; 

    @Override
    public void configure() {
        from("direct:Event-Insert-Rota").routeId("Event-Insert-Rota")
                .bean(RotaService.class, "findByEvent")
                .filter(isNotNull(body()))
                .filter(isNull(simple("body.record")))
                .to("direct:post-line");        
        
        from("direct:Event-Update-Rota").routeId("Event-Update-Rota")
                .bean(RotaService.class, "findByEvent")
                .filter(isNotNull(body()))
                .choice().when(isNull(simple("body.record"))).to("direct:post-line")
                .otherwise().to("direct:put-line");
        
        from("direct:Event-Delete-Rota").routeId("Event-Delete-Rota")
                .bean(RecordService.class, "findByEvent")
                .filter(isNotNull(body()))
                .to("direct:delete-line");
        
        from("direct:post-line").routeId("post-line")
                .transacted("PROPAGATION_REQUIRES_NEW")
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.HTTP_URI, simple(LINES_URL))
                .convertBodyTo(Line.class).marshal().json(JsonLibrary.Jackson)
                .to("direct:ksroute-api").unmarshal().json(JsonLibrary.Jackson, Line.class)
                .bean(RotaService.class, "saveResponse");

        from("direct:put-line").routeId("put-line")
                .transacted("PROPAGATION_REQUIRES_NEW")
                .setHeader("resourceId", simple("body.record.remoteId"))
                .setHeader(Exchange.HTTP_METHOD, constant("PUT"))
                .setHeader(Exchange.HTTP_URI, simple(LINE_URL))
                .convertBodyTo(Line.class).marshal().json(JsonLibrary.Jackson)
                .to("direct:ksroute-api").unmarshal().json(JsonLibrary.Jackson, Line.class)
                .bean(RotaService.class, "saveResponse");  
        
        from("direct:delete-line").routeId("delete-line")
                .transacted("PROPAGATION_REQUIRES_NEW")
                .setHeader("resourceId", simple("body.remoteId"))
                .setHeader(Exchange.HTTP_METHOD, constant("DELETE"))
                .setHeader(Exchange.HTTP_URI, simple(LINE_URL))
                .bean(RecordService.class, "delete")
                .setBody(constant(null)).to("direct:ksroute-api");    
        
        from("direct:enrich-line").routeId("enrich-line")
                .transform(simple("body.rota"))
                .bean(RotaService.class, "fetchRecord")
                .filter(isNull(simple("body.record")))
                .to("direct:post-line");   
    }

}
