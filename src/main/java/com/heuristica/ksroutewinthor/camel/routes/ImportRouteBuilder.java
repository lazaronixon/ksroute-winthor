package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.apis.Route;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.util.toolbox.AggregationStrategies;
import org.springframework.stereotype.Component;

@Component
public class ImportRouteBuilder extends ApplicationRouteBuilder {

    private static final String GET_URL = "https4://{{ksroute.api.url}}/plannings/routes.json?q[mounted_at_null]=0&q[imported_at_null]=1";
    private static final String POST_URL = "https4://{{ksroute.api.url}}/plannings/${header.planningId}/solutions/${header.solutionId}/routes/${header.id}/import.json";

    @Override
    public void configure() {
        super.configure();
        
        from("timer:fetch-route?fixedRate=true&period=15s")
        .throttle(MAXIMUM_REQUEST_COUNT).timePeriodMillis(TIME_PERIOD_MILLIS).to(GET_URL)
        .unmarshal().json(JsonLibrary.Jackson, Route[].class)
        .split(body())
        .enrich("direct:import-route", AggregationStrategies.useOriginal());
        
        from("direct:import-route").routeId("mount-route")                        
        .setHeader("CamelHttpMethod", constant("POST"))
        .setHeader("id", simple("body.id"))
        .setHeader("solutionId", simple("body.solution.id"))
        .setHeader("planningId", simple("body.solution.planning.id"))                        
        .setBody(constant(null)).throttle(MAXIMUM_REQUEST_COUNT).timePeriodMillis(TIME_PERIOD_MILLIS).recipientList(simple(POST_URL));       
        
    }

}
