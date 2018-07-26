package com.heuristica.ksroutewinthor.camel.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
class BranchRouteBuilder extends RouteBuilder {

    @Override
    public void configure() {
        from("direct:process-filial")
                .routeId("process-filial")
                .split().simple("body.filial")
                .to("dozer:transformFilial?targetModel=com.heuristica.ksroutewinthor.apis.Branch")
                .log("Filial processada: ${body}");

    }
}
