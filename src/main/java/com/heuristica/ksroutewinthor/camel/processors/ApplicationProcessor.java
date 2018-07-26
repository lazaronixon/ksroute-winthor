package com.heuristica.ksroutewinthor.camel.processors;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;


public abstract class ApplicationProcessor {
    
    @Autowired DozerBeanMapper dozerBeanMapper;
    
    @Value("${ksroute.api.url}")
    protected String apiUrl;
    
    @Value("${ksroute.api.email}")
    protected String apiEmail; 
    
    @Value("${ksroute.api.token}")
    protected String apiToken;     
    
}
