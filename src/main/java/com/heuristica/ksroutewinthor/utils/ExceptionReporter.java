package com.heuristica.ksroutewinthor.utils;

import io.sentry.Sentry;
import org.apache.camel.Exchange;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ExceptionReporter { 
    
    private static final String JMS_MESSAGE_ID = "JMSMessageID";
    
    @EventListener(ApplicationReadyEvent.class)
    public void initializeSentry() {
        Sentry.init();
    }    

    public void report(Exchange exchange) {
        Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
        Sentry.getContext().addExtra("messageId", exchange.getIn().getHeader(JMS_MESSAGE_ID, String.class));
        Sentry.getContext().addExtra("endpoint", exchange.getProperty(Exchange.FAILURE_ENDPOINT, String.class));
        Sentry.getContext().addExtra("routeId", exchange.getProperty(Exchange.FAILURE_ROUTE_ID, String.class));
        Sentry.getContext().addExtra("body", exchange.getIn().getBody(String.class));
        Sentry.capture(cause);
        Sentry.clearContext();
    }
}
