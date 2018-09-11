package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.apis.Region;
import com.heuristica.ksroutewinthor.services.RegiaoService;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
class RegionRouteBuilder extends ApplicationRouteBuilder {

    @Override
    public void configure() {
        super.configure();

        from("direct:process-regiao").routeId("process-regiao")
                .bean(RegiaoService.class, "findRegiao(${body.regiao.numregiao})")
                .choice().when(simple("${body.ksrId} == null")).to("direct:create-regiao")
                .otherwise().to("direct:update-regiao").end()               
                .unmarshal().json(JsonLibrary.Jackson, Region.class)
                .bean(RegiaoService.class, "saveRegiao(${body})");

        from("direct:create-regiao").routeId("create-regiao")
                .convertBodyTo(Region.class).marshal().json(JsonLibrary.Jackson)
                .throttle(5).to("https4://{{ksroute.api.url}}/regions.json");

        from("direct:update-regiao").routeId("update-regiao")
                .setHeader("CamelHttpMethod", constant("PUT"))
                .setHeader("ksrId", simple("body.ksrId"))
                .convertBodyTo(Region.class).marshal().json(JsonLibrary.Jackson)
                .throttle(5).recipientList(simple("https4://{{ksroute.api.url}}/regions/${header.ksrId}.json"));
    }
}
