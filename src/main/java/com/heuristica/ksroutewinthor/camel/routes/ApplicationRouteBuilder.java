package com.heuristica.ksroutewinthor.camel.routes;

import org.apache.camel.LoggingLevel;
import org.apache.camel.http.common.HttpOperationFailedException;
import org.apache.camel.spring.SpringRouteBuilder;

public class ApplicationRouteBuilder extends SpringRouteBuilder {

    @Override
    public void configure() {
        onException(HttpOperationFailedException.class)
                .filter(simple("${exception.statusCode} == 422"))
                .log(LoggingLevel.WARN, "Erro de validação: ${body}")
                .log(LoggingLevel.WARN, "Detalhe: ${exception.responseBody}");
    }

}
