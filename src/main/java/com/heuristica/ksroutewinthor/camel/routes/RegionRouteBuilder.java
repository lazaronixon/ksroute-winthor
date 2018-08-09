package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.apis.Region;
import com.heuristica.ksroutewinthor.models.Regiao;
import org.apache.camel.model.dataformat.JsonLibrary;
import static org.apache.camel.processor.idempotent.MemoryIdempotentRepository.memoryIdempotentRepository;
import org.springframework.stereotype.Component;

@Component
class RegionRouteBuilder extends ApplicationRouteBuilder {

    @Override
    public void configure() {
        super.configure();

        from("direct:process-regiao").routeId("process-regiao")
                .transform(simple("body.regiao"))
                .idempotentConsumer(simple("regiao/${body.numregiao}/${body.oraRowscn}"), memoryIdempotentRepository(100))
                .choice().when(simple("${body.ksrId} == null")).to("direct:create-regiao")
                .otherwise().to("direct:update-regiao")
                .unmarshal().json(JsonLibrary.Jackson, Regiao.class);

        from("direct:create-regiao").routeId("create-regiao")
                .convertBodyTo(Region.class).marshal().json(JsonLibrary.Jackson)
                .throttle(5).to("https4://{{ksroute.api.url}}/regions.json").end()
                .unmarshal().json(JsonLibrary.Jackson, Region.class).toD("jpa?query=UPDATE Regiao p SET p.ksrId = ${body.id} WHERE p.numregiao = ${body.erpId}").end();

        from("direct:update-regiao").routeId("update-regiao")
                .setHeader("CamelHttpMethod", constant("PUT"))
                .setHeader("ksrId", simple("body.ksrId"))
                .convertBodyTo(Region.class).marshal().json(JsonLibrary.Jackson)
                .throttle(5).recipientList(simple("https4://{{ksroute.api.url}}/regions/${header.ksrId}.json"));
    }
}
