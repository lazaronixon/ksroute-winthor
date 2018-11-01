package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.models.Event;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
public class EventRouteBuilder extends ApplicationRouteBuilder  {   

    @Override
    public void configure() throws Exception { 
        super.configure();
        
        from("jpa:" + Event.class.getName() + "?delay=15s&namedQuery=newEvents").routeId("process-events")                
                .marshal().json(JsonLibrary.Jackson).to("activemq:ksroute-events");

        from("activemq:ksroute-events?concurrentConsumers=5&acknowledgementModeName=CLIENT_ACKNOWLEDGE").routeId("process-event")
                .transacted("PROPAGATION_REQUIRED")
                .unmarshal().json(JsonLibrary.Jackson, Event.class)
                .log("Processando ${body}")
                .toD("direct:Event-${body.persistAction}-${body.eventableType}");
    }
    
}
