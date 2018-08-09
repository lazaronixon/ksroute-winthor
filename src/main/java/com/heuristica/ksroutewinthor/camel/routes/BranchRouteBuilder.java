package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.apis.Branch;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.util.toolbox.AggregationStrategies;
import org.springframework.stereotype.Component;

@Component
class BranchRouteBuilder extends ApplicationRouteBuilder {

    @Override
    public void configure() {
        super.configure();

        from("direct:process-filial").routeId("process-filial")
                .toD("jpa?query=SELECT p FROM Filial p WHERE p.codigo = ${body.filial.codigo}").split(body())
                .log("${body}")
                .choice().when(simple("${body.ksrId} == null")).to("direct:create-filial")
                .otherwise().to("direct:update-filial").endChoice();

        from("direct:create-filial").routeId("create-filial")
                .convertBodyTo(Branch.class).marshal().json(JsonLibrary.Jackson)
                .throttle(5).to("https4://{{ksroute.api.url}}/branches.json")
                .unmarshal().json(JsonLibrary.Jackson, Branch.class)
                .enrich("direct:persist-filial", AggregationStrategies.useOriginal());

        from("direct:update-filial").routeId("update-filial")
                .setHeader("CamelHttpMethod", constant("PUT"))
                .setHeader("ksrId", simple("body.ksrId"))
                .convertBodyTo(Branch.class).marshal().json(JsonLibrary.Jackson)
                .throttle(5).recipientList(simple("https4://{{ksroute.api.url}}/branches/${header.ksrId}.json"))
                .unmarshal().json(JsonLibrary.Jackson, Branch.class);

        from("direct:persist-filial").routeId("persist-filial")
                .toD("jpa?query=UPDATE Filial p SET p.ksrId = ${body.id} WHERE p.codigo = ${body.erpId}");

        //.idempotentConsumer(simple("filial/${body.codigo}/${body.oraRowscn}"), MemoryIdempotentRepository.memoryIdempotentRepository(10))
    }
}
