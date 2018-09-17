package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.apis.Line;
import com.heuristica.ksroutewinthor.services.RotaService;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.processor.idempotent.MemoryIdempotentRepository;
import org.springframework.stereotype.Component;

@Component
class LineRouteBuilder extends ApplicationRouteBuilder {

    @Override
    public void configure() {
        super.configure();

        from("direct:process-rota").routeId("process-rota")
                .transform(simple("body.rota"))                
                .choice()
                .when(simple("${body.ksrId} == null")).to("direct:create-rota")
                .otherwise().to("direct:update-rota");

        from("direct:create-rota").routeId("create-rota")
                .idempotentConsumer(simple("rota/${body.oraRowscn}"), MemoryIdempotentRepository.memoryIdempotentRepository())
                .convertBodyTo(Line.class).marshal().json(JsonLibrary.Jackson)
                .throttle(50).timePeriodMillis(10000).to("https4://{{ksroute.api.url}}/lines.json")
                .unmarshal().json(JsonLibrary.Jackson, Line.class)
                .bean(RotaService.class, "saveLine(${body})");

        from("direct:update-rota").routeId("update-rota")
                .idempotentConsumer(simple("rota/${body.oraRowscn}"), MemoryIdempotentRepository.memoryIdempotentRepository())
                .setHeader("CamelHttpMethod", constant("PUT"))
                .setHeader("ksrId", simple("body.ksrId"))                
                .convertBodyTo(Line.class).marshal().json(JsonLibrary.Jackson)
                .throttle(50).timePeriodMillis(10000).recipientList(simple("https4://{{ksroute.api.url}}/lines/${header.ksrId}.json"))
                .unmarshal().json(JsonLibrary.Jackson, Line.class)
                .bean(RotaService.class, "saveLine(${body})");
    }

}
