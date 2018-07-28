package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.apis.Branch;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
class BranchRouteBuilder extends ApplicationRouteBuilder {    
    
    @Override
    public void configure() {
        super.configure();
        
        from("direct:process-filial").routeId("process-filial")
                .log("Processando filial ${body.filial.codigo}")
                .split(simple("body.filial"))
                .convertBodyTo(Branch.class)
                .choice().when(simple("${body.id} == null")).to("direct:create-filial")
                .otherwise().to("direct:update-filial");
        
        from("direct:create-filial").routeId("create-filial")
                .setHeader("CamelHttpMethod", constant("POST"))
                .setHeader("X-User-Email", constant("{{ksroute.api.email}}"))
                .setHeader("X-User-Token", constant("{{ksroute.api.token}}"))
                .marshal().json(JsonLibrary.Jackson)
                .throttle(5).to("https4://{{ksroute.api.url}}/branches.json")
                .unmarshal().json(JsonLibrary.Jackson, Branch.class)
                .toD("jpa?query=UPDATE Filial p SET p.ksrId = ${body.id} WHERE p.codigo = ${body.erpId}");
        
        from("direct:update-filial").routeId("update-filial")
                .setHeader("CamelHttpMethod", constant("PUT"))
                .setHeader("X-User-Email", constant("{{ksroute.api.email}}"))
                .setHeader("X-User-Token", constant("{{ksroute.api.token}}"))
                .setHeader("id", simple("body.id"))
                .marshal().json(JsonLibrary.Jackson)
                .throttle(5).recipientList(simple("https4://{{ksroute.api.url}}/branches/${header.id}.json"));
    }
}
