package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.apis.Route;
import com.heuristica.ksroutewinthor.services.CarregamentoService;
import java.util.ArrayList;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.util.toolbox.AggregationStrategies;
import org.springframework.stereotype.Component;

@Component
public class ImportRouteBuilder extends RouteBuilder {

    private static final String GET_URL = "https://{{ksroute.api.url}}/plannings/routes.json?q[mounted_at_null]=0&q[imported_at_null]=1";
    private static final String POST_URL = "https://{{ksroute.api.url}}/plannings/${header.planningId}/solutions/${header.solutionId}/routes/${header.id}/import.json";

    @Override
    public void configure() {
//        from("timer:get-routes?fixedRate=true&period=30s").routeId("get-routes")
//                .transacted("PROPAGATION_REQUIRED")
//                .setHeader(Exchange.HTTP_METHOD, constant("GET"))
//                .setHeader(Exchange.HTTP_URI, simple(GET_URL))
//                .to("seda:ksroute-api").unmarshal().json(JsonLibrary.Jackson, Route[].class)
//                .split(body()).log("Processando rota ${body.id}")
//                .to("direct:import-route");
        
        from("direct:import-route").routeId("import-route")
                .transacted("PROPAGATION_REQUIRES_NEW")
                .bean(CarregamentoService.class, "saveRoute")
                .enrich("direct:post-route", AggregationStrategies.useOriginal());        

        from("direct:post-route").routeId("post-route")                                       
                .setHeader("id", simple("body.id"))
                .setHeader("solutionId", simple("body.solution.id"))
                .setHeader("planningId", simple("body.solution.planning.id"))               
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.HTTP_URI, simple(POST_URL))               
                .setBody(constant(null)).to("seda:ksroute-api");       
    }

}
