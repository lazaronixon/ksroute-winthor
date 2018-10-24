package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.models.Event;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;


@Component
public class EventRouteBuilder extends RouteBuilder  {

    @Override
    public void configure() throws Exception {
        from("jpa:" + Event.class.getName() + "?delay=15s&namedQuery=newEvents").routeId("process-events")                
                .marshal().json(JsonLibrary.Jackson).to("activemq:ksr-process-event");

        from("activemq:ksr-process-event?concurrentConsumers=5").routeId("ksr-process-event")
                .transacted("PROPAGATION_REQUIRED")
                .unmarshal().json(JsonLibrary.Jackson, Event.class)
                .log("Processando ${body}")
                .toD("direct:Event-${body.persistAction}-${body.eventableType}");
    }
    
}
