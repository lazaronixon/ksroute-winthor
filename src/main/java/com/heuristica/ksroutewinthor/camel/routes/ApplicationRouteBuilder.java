package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.utils.HttpNotFoundException;
import org.apache.camel.builder.RouteBuilder;

public abstract class ApplicationRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        onException(Exception.class).to("bean:exceptionReporter");
        
        onException(HttpNotFoundException.class).handled(true).to("bean:exceptionReporter");   
    }

}
