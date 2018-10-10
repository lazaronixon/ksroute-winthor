package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.apis.Line;
import static com.heuristica.ksroutewinthor.camel.routes.ApplicationRouteBuilder.MAXIMUM_REQUEST_COUNT;
import static com.heuristica.ksroutewinthor.camel.routes.ApplicationRouteBuilder.TIME_PERIOD_MILLIS;
import com.heuristica.ksroutewinthor.services.RotaService;
import org.apache.camel.Exchange;
import static org.apache.camel.builder.PredicateBuilder.isNull;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.processor.idempotent.MemoryIdempotentRepository;
import org.springframework.stereotype.Component;

@Component
class LineRouteBuilder extends ApplicationRouteBuilder {
    
    private static final String POST_URL = "https4:{{ksroute.api.url}}/lines.json";
    private static final String PUT_URL = "https4:{{ksroute.api.url}}/lines/${header.ksrId}.json";
    private static final String CACHE_KEY = "rota/${body.codrota}/${body.oraRowscn}";    

    @Override
    public void configure() {
        super.configure();

        from("direct:process-rota").routeId("process-rota")                
                .transform(simple("body.rota"))                
                .choice().when(isNull(simple("body.ksrId"))).to("direct:post-line")
                .otherwise().to("direct:put-line");

        from("direct:post-line").routeId("post-line")
                .convertBodyTo(Line.class).marshal().json(JsonLibrary.Jackson)
                .throttle(MAXIMUM_REQUEST_COUNT).timePeriodMillis(TIME_PERIOD_MILLIS).to(POST_URL)
                .unmarshal().json(JsonLibrary.Jackson, Line.class)
                .bean(RotaService.class, "saveLine");

        from("direct:put-line").routeId("put-line")
                .idempotentConsumer(simple(CACHE_KEY), MemoryIdempotentRepository.memoryIdempotentRepository())
                .setHeader(Exchange.HTTP_METHOD, constant("PUT"))
                .setHeader("ksrId", simple("body.ksrId"))        
                .convertBodyTo(Line.class).marshal().json(JsonLibrary.Jackson)
                .throttle(MAXIMUM_REQUEST_COUNT).timePeriodMillis(TIME_PERIOD_MILLIS).toD(PUT_URL)
                .unmarshal().json(JsonLibrary.Jackson, Line.class)
                .bean(RotaService.class, "saveLine");
    }

}
