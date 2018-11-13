package com.heuristica.ksroutewinthor.utils;

import io.sentry.Sentry;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ExceptionReporter {  
    
    @EventListener(ApplicationReadyEvent.class)
    public void initializeSentry() {
        Sentry.init();
    }    

    public void report(Exception e) {        
        Sentry.capture(e);
    }
}
