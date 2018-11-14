package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.apis.Line;
import com.heuristica.ksroutewinthor.services.RecordService;
import com.heuristica.ksroutewinthor.services.RotaService;
import org.apache.camel.Exchange;
import static org.apache.camel.builder.PredicateBuilder.isNull;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
class LineRouteBuilder extends ApplicationRouteBuilder {
    
    private static final String LINES_URL = "https://{{ksroute.api.url}}/lines.json";
    private static final String LINE_URL = "https://{{ksroute.api.url}}/lines/${property.remoteId}.json"; 

    @Override
    public void configure() throws Exception {
        super.configure();
        
        from("direct:event-save-rota").routeId("event-save-rota")
                .bean(RotaService.class, "findByEvent")
                .choice().when(isNull(simple("body.record"))).to("direct:post-line")
                .otherwise().to("direct:put-line");
        
        from("direct:event-delete-rota").routeId("event-delete-rota")
                .transform(simple("body.deletedRecord"))
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
                .setProperty("remoteId", simple("body.record.remoteId"))
                .setHeader(Exchange.HTTP_METHOD, constant("PUT"))
                .setHeader(Exchange.HTTP_URI, simple(LINE_URL))
                .convertBodyTo(Line.class).marshal().json(JsonLibrary.Jackson)
                .to("direct:ksroute-api").unmarshal().json(JsonLibrary.Jackson, Line.class)
                .bean(RotaService.class, "saveResponse");
        
        from("direct:delete-line").routeId("delete-line")
                .transacted("PROPAGATION_REQUIRES_NEW")
                .setProperty("recordId", simple("body.id"))
                .setProperty("remoteId", simple("body.remoteId"))
                .setHeader(Exchange.HTTP_METHOD, constant("DELETE"))
                .setHeader(Exchange.HTTP_URI, simple(LINE_URL))
                .setBody(constant(null)).to("direct:ksroute-api")
                .bean(RecordService.class, "deleteByRecordId");
    }

}
