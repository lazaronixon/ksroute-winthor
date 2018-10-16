package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.models.Event;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;


@Component
public class EventRouteBuilder extends RouteBuilder  {

    @Override
    public void configure() throws Exception {
        from("jpa:" + Event.class.getName() + "?delay=15s&namedQuery=newEvents").routeId("process-events")
                .log("Processando ${body}")
                .toD("direct:${body.persistAction}-${body.eventableType}");
    }
    
}
