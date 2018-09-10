package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.apis.Branch;
import com.heuristica.ksroutewinthor.services.FilialService;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
class BranchRouteBuilder extends ApplicationRouteBuilder {

    @Override
    public void configure() {
        super.configure();

        from("direct:process-filial").routeId("process-filial")
                .bean(FilialService.class, "findFilial(${body.filial.codigo})")
                .choice().when(simple("${body.ksrId} == null")).to("direct:create-filial")
                .otherwise().to("direct:update-filial").endChoice()
                .unmarshal().json(JsonLibrary.Jackson, Branch.class)
                .bean(FilialService.class, "saveFilial(${body})");

        from("direct:create-filial").routeId("create-filial")
                .convertBodyTo(Branch.class).marshal().json(JsonLibrary.Jackson)
                .throttle(5).to("https4://{{ksroute.api.url}}/branches.json");

        from("direct:update-filial").routeId("update-filial")
                .setHeader("CamelHttpMethod", constant("PUT"))
                .setHeader("ksrId", simple("body.ksrId"))
                .convertBodyTo(Branch.class).marshal().json(JsonLibrary.Jackson)
                .throttle(5).recipientList(simple("https4://{{ksroute.api.url}}/branches/${header.ksrId}.json"));

        //from("direct:persist-filial").routeId("persist-filial")
        //        .toD("jpa?query=UPDATE Filial p SET p.ksrId = ${body.id} WHERE p.codigo = ${body.erpId}");

        //.enrich("direct:persist-filial", AggregationStrategies.useOriginal());
        //.idempotentConsumer(simple("filial/${body.codigo}/${body.oraRowscn}"), MemoryIdempotentRepository.memoryIdempotentRepository(10))
    }
}
