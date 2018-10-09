package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.apis.Route;
import com.heuristica.ksroutewinthor.services.CarregamentoService;
import org.apache.camel.Exchange;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
public class ImportRouteBuilder extends ApplicationRouteBuilder {

    private static final String GET_URL = "https4:{{ksroute.api.url}}/plannings/routes.json?q[mounted_at_null]=0&q[imported_at_null]=1";
    private static final String POST_URL = "https4:{{ksroute.api.url}}/plannings/${header.planningId}/solutions/${header.solutionId}/routes/${header.id}/import.json";

    @Override
    public void configure() {
        super.configure();

        from("timer:fetch-route?fixedRate=true&period=15s").routeId("import-route")
                .throttle(MAXIMUM_REQUEST_COUNT).timePeriodMillis(TIME_PERIOD_MILLIS).to(GET_URL)
                .unmarshal().json(JsonLibrary.Jackson, Route[].class).split(body())
                .log("Processando rota ${body.id}").to("direct:post-route");

        from("direct:post-route").routeId("post-route")
                .transacted("PROPAGATION_REQUIRES_NEW")
                .setHeader(Exchange.HTTP_METHOD, constant("PUT"))
                .setHeader("id", simple("body.id"))
                .setHeader("solutionId", simple("body.solution.id"))
                .setHeader("planningId", simple("body.solution.planning.id"))
                .setBody(constant(null)).throttle(MAXIMUM_REQUEST_COUNT).timePeriodMillis(TIME_PERIOD_MILLIS).toD(POST_URL)
                .bean(CarregamentoService.class, "saveRoute");
    }

}
