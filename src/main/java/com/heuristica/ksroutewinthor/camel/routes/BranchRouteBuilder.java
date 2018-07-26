package com.heuristica.ksroutewinthor.camel.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.common.HttpOperationFailedException;
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
                .to("dozer:transformFilial?marshalId=defaultGson&targetModel=com.heuristica.ksroutewinthor.apis.Branch")
                .setHeader("CamelHttpMethod", constant("POST"))
                .setHeader("X-User-Email", constant("{{ksroute.api.email}}"))
                .setHeader("X-User-Token", constant("{{ksroute.api.token}}"))       
                .to("https4://{{ksroute.api.url}}/branches.json")
                .log("Created branch ${body}");
        
        from("direct:update-filial")
                .routeId("update-filial")
                .to("dozer:transformFilial?marshalId=defaultGson&targetModel=com.heuristica.ksroutewinthor.apis.Branch")
                .setHeader("CamelHttpMethod", constant("PUT"))
                .setHeader("X-User-Email", constant("{{ksroute.api.email}}"))
                .setHeader("X-User-Token", constant("{{ksroute.api.token}}")) 
                .to("http4://{{ksroute.api.url}}")
                .log("Updated branch ${body}");
    }
}
