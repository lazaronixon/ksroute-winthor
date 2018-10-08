package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.apis.Region;
import static com.heuristica.ksroutewinthor.camel.routes.ApplicationRouteBuilder.TIME_PERIOD_MILLIS;
import com.heuristica.ksroutewinthor.services.RegiaoService;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.processor.idempotent.MemoryIdempotentRepository;
import org.springframework.stereotype.Component;

@Component
class RegionRouteBuilder extends ApplicationRouteBuilder {
    
    private static final String CACHE_KEY = "regiao/${body.numregiao}/${body.oraRowscn}";
    private static final String POST_URL = "https4://{{ksroute.api.url}}/regions.json";
    private static final String PUT_URL = "https4://{{ksroute.api.url}}/regions/${header.ksrId}.json";      

    @Override
    public void configure() {
        super.configure();

        from("direct:process-regiao").routeId("process-regiao")                
                .transform(simple("body.regiao"))                   
                .choice().when(simple("${body.ksrId} == null")).to("direct:create-regiao")
                .otherwise().to("direct:update-regiao");

        from("direct:create-regiao").routeId("create-regiao")
                .transacted("PROPAGATION_REQUIRES_NEW")
                .idempotentConsumer(simple(CACHE_KEY), MemoryIdempotentRepository.memoryIdempotentRepository())
                .convertBodyTo(Region.class).marshal().json(JsonLibrary.Jackson)
                .throttle(MAXIMUM_REQUEST_COUNT).timePeriodMillis(TIME_PERIOD_MILLIS).to(POST_URL)
                .unmarshal().json(JsonLibrary.Jackson, Region.class)
                .bean(RegiaoService.class, "saveRegion");

        from("direct:update-regiao").routeId("update-regiao")
                .transacted("PROPAGATION_REQUIRES_NEW")
                .idempotentConsumer(simple(CACHE_KEY), MemoryIdempotentRepository.memoryIdempotentRepository())
                .setHeader("CamelHttpMethod", constant("PUT"))
                .setHeader("ksrId", simple("body.ksrId"))                
                .convertBodyTo(Region.class).marshal().json(JsonLibrary.Jackson)
                .throttle(MAXIMUM_REQUEST_COUNT).timePeriodMillis(TIME_PERIOD_MILLIS).recipientList(simple(PUT_URL))
                .unmarshal().json(JsonLibrary.Jackson, Region.class)
                .bean(RegiaoService.class, "saveRegion");
    }
}
