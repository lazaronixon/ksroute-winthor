package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.models.Event;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;


@Component
public class EventRouteBuilder extends RouteBuilder  {

    @Override
    public void configure() throws Exception {
        from("jpa:" + Event.class.getName() + "?delay=15s&maximumResults=50&namedQuery=newEvents").routeId("process-events").to("seda:process-events-seda");
        
        from("seda:process-events-seda?concurrentConsumers=5").routeId("process-events-seda")
            .log("Processando ${body}")
            .toD("direct:Event-${body.persistAction}-${body.eventableType}");
    }
    
}
