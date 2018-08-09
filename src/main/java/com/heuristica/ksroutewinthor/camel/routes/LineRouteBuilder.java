package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.apis.Line;
import org.apache.camel.model.dataformat.JsonLibrary;
import static org.apache.camel.processor.idempotent.MemoryIdempotentRepository.memoryIdempotentRepository;
import org.springframework.stereotype.Component;

@Component
class LineRouteBuilder extends ApplicationRouteBuilder {

    @Override
    public void configure() {
        super.configure();

        from("direct:process-rota").routeId("process-rota")
                .transform(simple("body.rota"))
                .idempotentConsumer(simple("rota/${body.codrota}/${body.oraRowscn}"), memoryIdempotentRepository(100))
                .choice().when(simple("${body.ksrId} == null")).to("direct:create-rota")
                .otherwise().to("direct:update-rota")
                .unmarshal().json(JsonLibrary.Jackson, Line.class);

        from("direct:create-rota").routeId("create-rota")
                .convertBodyTo(Line.class).marshal().json(JsonLibrary.Jackson)
                .throttle(5).to("https4://{{ksroute.api.url}}/lines.json")
                .unmarshal().json(JsonLibrary.Jackson, Line.class)
                .toD("jpa?query=UPDATE Rota p SET p.ksrId = ${body.id} WHERE p.numrota = ${body.erpId}").end();

        from("direct:update-praca").routeId("update-praca")
                .setHeader("CamelHttpMethod", constant("PUT"))
                .setHeader("ksrId", simple("body.ksrId"))
                .convertBodyTo(Line.class).marshal().json(JsonLibrary.Jackson)
                .throttle(5).recipientList(simple("https4://{{ksroute.api.url}}/lines/${header.ksrId}.json"));
    }

}
