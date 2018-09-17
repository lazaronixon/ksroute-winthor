package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.apis.Branch;
import com.heuristica.ksroutewinthor.services.FilialService;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.processor.idempotent.MemoryIdempotentRepository;
import org.springframework.stereotype.Component;

@Component
class BranchRouteBuilder extends ApplicationRouteBuilder {

    @Override
    public void configure() {
        super.configure();

        from("direct:process-filial").routeId("process-filial")
                .transform(simple("body.filial"))
                .choice()
                .when(simple("${body.ksrId} == null")).to("direct:create-filial")
                .otherwise().to("direct:update-filial");

        from("direct:create-filial").routeId("create-filial")
                .idempotentConsumer(simple("filial/${body.oraRowscn}"), MemoryIdempotentRepository.memoryIdempotentRepository())
                .convertBodyTo(Branch.class).marshal().json(JsonLibrary.Jackson)
                .throttle(50).timePeriodMillis(10000).to("https4://{{ksroute.api.url}}/branches.json")
                .unmarshal().json(JsonLibrary.Jackson, Branch.class)
                .bean(FilialService.class, "saveBranch(${body})");

        from("direct:update-filial").routeId("update-filial")
                .idempotentConsumer(simple("filial/${body.oraRowscn}"), MemoryIdempotentRepository.memoryIdempotentRepository())
                .setHeader("CamelHttpMethod", constant("PUT"))
                .setHeader("ksrId", simple("body.ksrId"))
                .convertBodyTo(Branch.class).marshal().json(JsonLibrary.Jackson)
                .throttle(50).timePeriodMillis(10000).recipientList(simple("https4://{{ksroute.api.url}}/branches/${header.ksrId}.json"))
                .unmarshal().json(JsonLibrary.Jackson, Branch.class)
                .bean(FilialService.class, "saveBranch(${body})");
    }
}
