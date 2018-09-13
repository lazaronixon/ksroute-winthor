package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.apis.Line;
import com.heuristica.ksroutewinthor.services.RotaService;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
class LineRouteBuilder extends ApplicationRouteBuilder {

    @Override
    public void configure() {
        super.configure();

        from("direct:process-rota").routeId("process-rota")
                .bean(RotaService.class, "findRota(${body.rota.codrota})")
                .choice().when(simple("${body.ksrId} == null")).to("direct:create-rota")
                .otherwise().to("direct:update-rota").end()
                .unmarshal().json(JsonLibrary.Jackson, Line.class)
                .bean(RotaService.class, "saveLine(${body})");

        from("direct:create-rota").routeId("create-rota")
                .convertBodyTo(Line.class).marshal().json(JsonLibrary.Jackson)
                .throttle(5).to("https4://{{ksroute.api.url}}/lines.json");

        from("direct:update-rota").routeId("update-rota")
                .setHeader("CamelHttpMethod", constant("PUT"))
                .setHeader("ksrId", simple("body.ksrId"))
                .convertBodyTo(Line.class).marshal().json(JsonLibrary.Jackson)
                .throttle(5).recipientList(simple("https4://{{ksroute.api.url}}/lines/${header.ksrId}.json"));
    }

}
