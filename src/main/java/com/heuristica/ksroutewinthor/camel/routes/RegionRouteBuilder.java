package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.apis.Region;
import com.heuristica.ksroutewinthor.services.RegiaoService;
import org.apache.camel.Exchange;
import static org.apache.camel.builder.PredicateBuilder.isNull;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.processor.idempotent.MemoryIdempotentRepository;
import org.springframework.stereotype.Component;

@Component
class RegionRouteBuilder extends RouteBuilder {
    
    private static final String POST_URL = "https://{{ksroute.api.url}}/regions.json";
    private static final String PUT_URL = "https://{{ksroute.api.url}}/regions/${body.ksrId}.json";
    private static final String CACHE_KEY = "regiao/${body.numregiao}/${body.oraRowscn}";

    @Override
    public void configure() {
        from("direct:process-regiao").routeId("process-regiao")               
                .transform(simple("body.regiao"))                   
                .choice().when(isNull(simple("body.ksrId"))).to("direct:post-region")
                .otherwise().to("direct:put-region");

        from("direct:post-region").routeId("post-region")
                .transacted("PROPAGATION_REQUIRES_NEW")                
                .setHeader(Exchange.HTTP_URI, simple(POST_URL))
                .convertBodyTo(Region.class).marshal().json(JsonLibrary.Jackson)
                .to("direct:ksroute-api").unmarshal().json(JsonLibrary.Jackson, Region.class)
                .bean(RegiaoService.class, "saveApiResponse");

        from("direct:put-region").routeId("put-region")                
                .idempotentConsumer(simple(CACHE_KEY), MemoryIdempotentRepository.memoryIdempotentRepository())
                .setHeader(Exchange.HTTP_METHOD, constant("PUT"))
                .setHeader(Exchange.HTTP_URI, simple(PUT_URL))                
                .convertBodyTo(Region.class).marshal().json(JsonLibrary.Jackson)
                .to("direct:ksroute-api").unmarshal().json(JsonLibrary.Jackson, Region.class);
    }
}
