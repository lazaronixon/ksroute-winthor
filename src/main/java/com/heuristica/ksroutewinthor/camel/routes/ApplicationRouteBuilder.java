package com.heuristica.ksroutewinthor.camel.routes;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.common.HttpOperationFailedException;

public class ApplicationRouteBuilder extends RouteBuilder {

    @Override
    public void configure() {    
        onException(HttpOperationFailedException.class).log(LoggingLevel.WARN, "Erro no servidor: ${body}");
    }

}
