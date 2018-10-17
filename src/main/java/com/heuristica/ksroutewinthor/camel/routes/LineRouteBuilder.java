package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.apis.Line;
import com.heuristica.ksroutewinthor.services.RotaService;
import org.apache.camel.Exchange;
import static org.apache.camel.builder.PredicateBuilder.isNull;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
class LineRouteBuilder extends RouteBuilder {
    
    private static final String LINES_URL = "https://{{ksroute.api.url}}/lines.json";
    private static final String LINE_URL = "https://{{ksroute.api.url}}/lines/${body.ksrId}.json";

    @Override
    public void configure() {
//        from("direct:save-line").routeId("save-line")
//                .bean(RotaService.class, "getEventable").filter(body().isNotNull())
//                .choice().when(isNull(simple("body.ksrId"))).to("direct:post-line")
//                .otherwise().to("direct:put-line");
        
        from("direct:enrich-line").routeId("enrich-line")
                .transform(simple("body.rota")).filter(body().isNotNull())
                .filter(isNull(simple("body.ksrId"))).to("direct:post-line");        
        
        from("direct:post-line").routeId("post-line")
                .transacted("PROPAGATION_REQUIRES_NEW")
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.HTTP_URI, simple(LINES_URL))
                .convertBodyTo(Line.class).marshal().json(JsonLibrary.Jackson)
                .to("direct:ksroute-api").unmarshal().json(JsonLibrary.Jackson, Line.class)
                .bean(RotaService.class, "saveApiResponse");

        from("direct:put-line").routeId("put-line")
                .setHeader(Exchange.HTTP_METHOD, constant("PUT"))
                .setHeader(Exchange.HTTP_URI, simple(LINE_URL))
                .convertBodyTo(Line.class).marshal().json(JsonLibrary.Jackson)
                .to("direct:ksroute-api");

        from("direct:delete-line").routeId("delete-line")
                .setHeader(Exchange.HTTP_METHOD, constant("DELETE"))
                .setHeader(Exchange.HTTP_URI, simple(LINE_URL))
                .setBody(constant(null)).to("direct:ksroute-api");
    }

}
