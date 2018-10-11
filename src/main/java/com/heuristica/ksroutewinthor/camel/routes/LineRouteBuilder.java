package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.apis.Line;
import com.heuristica.ksroutewinthor.services.RotaService;
import org.apache.camel.Exchange;
import static org.apache.camel.builder.PredicateBuilder.isNull;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.processor.idempotent.MemoryIdempotentRepository;
import org.springframework.stereotype.Component;

@Component
class LineRouteBuilder extends RouteBuilder {
    
    private static final String POST_URL = "https://{{ksroute.api.url}}/lines.json";
    private static final String PUT_URL = "https://{{ksroute.api.url}}/lines/${body.ksrId}.json";
    private static final String CACHE_KEY = "rota/${body.codrota}/${body.oraRowscn}";    

    @Override
    public void configure() {
        from("direct:process-rota").routeId("process-rota")                                
                .transform(simple("body.rota"))                
                .choice().when(isNull(simple("body.ksrId"))).to("direct:post-line")
                .otherwise().to("direct:put-line");

        from("direct:post-line").routeId("post-line")
                .transacted("PROPAGATION_REQUIRES_NEW")
                .setHeader(Exchange.HTTP_URI, simple(POST_URL))
                .convertBodyTo(Line.class).marshal().json(JsonLibrary.Jackson)
                .to("direct:ksroute-api").unmarshal().json(JsonLibrary.Jackson, Line.class)
                .bean(RotaService.class, "saveApiResponse");

        from("direct:put-line").routeId("put-line")             
                .idempotentConsumer(simple(CACHE_KEY), MemoryIdempotentRepository.memoryIdempotentRepository())
                .setHeader(Exchange.HTTP_METHOD, constant("PUT"))
                .setHeader(Exchange.HTTP_URI, simple(PUT_URL))        
                .convertBodyTo(Line.class).marshal().json(JsonLibrary.Jackson)
                .to("direct:ksroute-api").unmarshal().json(JsonLibrary.Jackson, Line.class);
    }

}
