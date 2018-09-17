package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.apis.Region;
import com.heuristica.ksroutewinthor.services.RegiaoService;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.processor.idempotent.MemoryIdempotentRepository;
import org.springframework.stereotype.Component;

@Component
class RegionRouteBuilder extends ApplicationRouteBuilder {

    @Override
    public void configure() {
        super.configure();

        from("direct:process-regiao").routeId("process-regiao")
                .transform(simple("body.regiao"))                   
                .choice()
                .when(simple("${body.ksrId} == null")).to("direct:create-regiao")
                .otherwise().to("direct:update-regiao");

        from("direct:create-regiao").routeId("create-regiao")
                .idempotentConsumer(simple("regiao/${body.oraRowscn}"), MemoryIdempotentRepository.memoryIdempotentRepository())
                .convertBodyTo(Region.class).marshal().json(JsonLibrary.Jackson)
                .throttle(50).timePeriodMillis(10000).to("https4://{{ksroute.api.url}}/regions.json")
                .unmarshal().json(JsonLibrary.Jackson, Region.class)
                .bean(RegiaoService.class, "saveRegion(${body})");

        from("direct:update-regiao").routeId("update-regiao")
                .idempotentConsumer(simple("regiao/${body.oraRowscn}"), MemoryIdempotentRepository.memoryIdempotentRepository())
                .setHeader("CamelHttpMethod", constant("PUT"))
                .setHeader("ksrId", simple("body.ksrId"))                
                .convertBodyTo(Region.class).marshal().json(JsonLibrary.Jackson)
                .throttle(50).timePeriodMillis(10000).recipientList(simple("https4://{{ksroute.api.url}}/regions/${header.ksrId}.json"))
                .unmarshal().json(JsonLibrary.Jackson, Region.class)
                .bean(RegiaoService.class, "saveRegion(${body})");
    }
}
