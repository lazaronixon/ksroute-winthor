package com.heuristica.ksroutewinthor.camel.routes;

import org.apache.camel.http.common.HttpOperationFailedException;
import org.springframework.stereotype.Component;

@Component
class KsrouteApiRouteBuilder extends ApplicationRouteBuilder {  
    
    protected static final Integer MAXIMUM_REQUEST_COUNT = 45;     
    protected static final Integer TIME_PERIOD_MILLIS = 10000;

    @Override
    public void configure() throws Exception {      
        super.configure();            
        
        from("direct:ksroute-api").routeId("ksroute-api")
                .doTry()
                    .setHeader("X-User-Token", constant("{{ksroute.api.token}}"))
                    .throttle(MAXIMUM_REQUEST_COUNT).timePeriodMillis(TIME_PERIOD_MILLIS)
                    .to("https4:ksroute-api-request")
                .endDoTry().doCatch(HttpOperationFailedException.class)
                    .choice().when(simple("${exception.statusCode} == 404")).to("bean:exceptionReporter")
                    .otherwise().throwException(RuntimeException.class, "HTTP Server Error").endChoice()
                .end();
    }      
}
