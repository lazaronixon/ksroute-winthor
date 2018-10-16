package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.apis.Region;
import com.heuristica.ksroutewinthor.services.RegiaoService;
import org.apache.camel.Exchange;
import static org.apache.camel.builder.PredicateBuilder.isNull;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
class RegionRouteBuilder extends RouteBuilder {
    
    private static final String REGIONS_URL = "https://{{ksroute.api.url}}/regions.json";
    private static final String REGION_URL = "https://{{ksroute.api.url}}/regions/${body.ksrId}.json";

    @Override
    public void configure() {
        from("direct:save-region").routeId("save-region")
                .bean(RegiaoService.class, "getEventable")
                .filter(body().isNotNull())
                .choice().when(isNull(simple("body.ksrId"))).to("direct:post-region")
                .otherwise().to("direct:put-region");
        
        from("direct:enrich-region").routeId("enrich-region")
                .transform(simple("body.regiao"))
                .filter(isNull(simple("body.ksrId"))).to("direct:post-region");        
        
        from("direct:post-region").routeId("post-region")
                .transacted("PROPAGATION_REQUIRES_NEW")
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.HTTP_URI, simple(REGIONS_URL))
                .convertBodyTo(Region.class).marshal().json(JsonLibrary.Jackson)
                .to("direct:ksroute-api").unmarshal().json(JsonLibrary.Jackson, Region.class)
                .bean(RegiaoService.class, "saveApiResponse");

        from("direct:put-region").routeId("put-region")
                .setHeader(Exchange.HTTP_METHOD, constant("PUT"))
                .setHeader(Exchange.HTTP_URI, simple(REGION_URL))
                .convertBodyTo(Region.class).marshal().json(JsonLibrary.Jackson)
                .to("direct:ksroute-api");

        from("direct:delete-region").routeId("delete-region")
                .setHeader(Exchange.HTTP_METHOD, constant("DELETE"))
                .setHeader(Exchange.HTTP_URI, simple(REGION_URL))
                .setBody(constant(null)).to("direct:ksroute-api");               
    }
}
