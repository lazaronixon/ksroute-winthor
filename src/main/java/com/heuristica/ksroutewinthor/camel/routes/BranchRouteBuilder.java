package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.apis.Branch;
import com.heuristica.ksroutewinthor.models.Filial;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
class BranchRouteBuilder extends ApplicationRouteBuilder {

    @Override
    public void configure() {
        super.configure();

        from("direct:process-filial").routeId("process-filial")
                .transform(simple("body.filial"))     
                .idempotentConsumer(simple("branches/${body.codigo}"), getIdempotentExpirableCache())
                .choice().when(simple("${body.ksrId} == null")).to("direct:create-filial")
                .otherwise().to("direct:update-filial")
                .unmarshal().json(JsonLibrary.Jackson, Filial.class);

        from("direct:create-filial").routeId("create-filial")
                .convertBodyTo(Branch.class).marshal().json(JsonLibrary.Jackson)
                .throttle(5).to("https4://{{ksroute.api.url}}/branches.json")
                .unmarshal().json(JsonLibrary.Jackson, Branch.class)
                .toD("jpa?query=UPDATE Filial p SET p.ksrId = ${body.id} WHERE p.codigo = ${body.erpId}");

        from("direct:update-filial").routeId("update-filial")
                .setHeader("CamelHttpMethod", constant("PUT"))
                .setHeader("ksrId", simple("body.ksrId"))
                .convertBodyTo(Branch.class).marshal().json(JsonLibrary.Jackson)
                .throttle(5).recipientList(simple("https4://{{ksroute.api.url}}/branches/${header.ksrId}.json"));
    }
}
