package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.apis.Branch;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
class BranchRouteBuilder extends RouteBuilder {

    @Override
    public void configure() {
        
        from("direct:process-filial")
                .routeId("process-filial")
                .split().simple("body.filial")
                .choice()
                .when().simple("${body.ksrId} == null").to("direct:create-filial")
                .otherwise().to("direct:update-filial");
        
        from("direct:create-filial")
                .routeId("create-filial")
                .to("dozer:transformFilial?targetModel=com.heuristica.ksroutewinthor.apis.Branch")
                .marshal().json(JsonLibrary.Jackson)
                .setHeader("CamelHttpMethod", constant("POST"))
                .setHeader("X-User-Email", constant("{{ksroute.api.email}}"))
                .setHeader("X-User-Token", constant("{{ksroute.api.token}}"))
                .throttle(10).to("https4://{{ksroute.api.url}}/branches.json")
                .unmarshal().json(JsonLibrary.Jackson, Branch.class)
                .toD("jpa:?query=UPDATE Filial p SET p.ksrId = ${body.id} WHERE p.codigo = ${body.erpId}");
        
        from("direct:update-filial")
                .routeId("update-filial")
                .to("dozer:transformFilial?marshalId=defaultGson&targetModel=com.heuristica.ksroutewinthor.apis.Branch")
                .setHeader("CamelHttpMethod", constant("PUT"))
                .setHeader("X-User-Email", constant("{{ksroute.api.email}}"))
                .setHeader("X-User-Token", constant("{{ksroute.api.token}}")) 
                .to("http4://{{ksroute.api.url}}");
    }
}
