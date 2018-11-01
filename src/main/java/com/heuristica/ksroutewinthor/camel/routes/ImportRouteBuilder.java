package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.apis.Route;
import com.heuristica.ksroutewinthor.services.CarregamentoService;
import org.apache.camel.Exchange;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
public class ImportRouteBuilder extends ApplicationRouteBuilder {

    private static final String GET_URL = "https://{{ksroute.api.url}}/plannings/routes.json?q[mounted_at_null]=0&q[imported_at_null]=1";
    private static final String POST_URL = "https://{{ksroute.api.url}}/plannings/${header.planningId}/solutions/${header.solutionId}/routes/${header.id}/import.json";

    @Override
    public void configure() throws Exception {
        super.configure();
        
        from("scheduler:get-routes?delay=30s").routeId("get-routes")
                .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                .setHeader(Exchange.HTTP_URI, simple(GET_URL))
                .to("direct:ksroute-api").unmarshal().json(JsonLibrary.Jackson, Route[].class)
                .split(body()).to("direct:import-route");
        
        from("direct:import-route").routeId("import-route")
                .transacted("PROPAGATION_REQUIRES_NEW")
                .log("Importando rota ${body.id}")
                .bean(CarregamentoService.class, "saveRoute")
                .setHeader("id", simple("body.id"))
                .setHeader("solutionId", simple("body.solution.id"))
                .setHeader("planningId", simple("body.solution.planning.id"))               
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.HTTP_URI, simple(POST_URL))               
                .setBody(constant(null)).to("direct:ksroute-api");      
    }

}
