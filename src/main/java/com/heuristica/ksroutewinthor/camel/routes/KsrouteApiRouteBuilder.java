package com.heuristica.ksroutewinthor.camel.routes;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.common.HttpOperationFailedException;
import org.springframework.stereotype.Component;

@Component
class KsrouteApiRouteBuilder extends RouteBuilder {  
    
    protected static final Integer MAXIMUM_REQUEST_COUNT = 45;     
    protected static final Integer TIME_PERIOD_MILLIS = 10000;

    @Override
    public void configure() {        
        onException(HttpOperationFailedException.class).handled(true)                
            .log(LoggingLevel.WARN, "Erro no servidor: ${exception.message}")
            .log(LoggingLevel.WARN, "Menssagem: ${body}")
            .filter(header(Exchange.HTTP_METHOD).isEqualTo(constant("POST")))
            .throwException(new RuntimeException("Erro ao criar recurso"));

        from("seda:ksroute-api?concurrentConsumers=5").routeId("ksroute-api")
                .setHeader("X-User-Token", constant("{{ksroute.api.token}}"))
                .throttle(MAXIMUM_REQUEST_COUNT).timePeriodMillis(TIME_PERIOD_MILLIS).to("https4:ksroute-api");
    }
}
