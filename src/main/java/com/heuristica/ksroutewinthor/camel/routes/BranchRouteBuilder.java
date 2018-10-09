package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.apis.Branch;
import com.heuristica.ksroutewinthor.services.FilialService;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.processor.idempotent.MemoryIdempotentRepository;
import org.springframework.stereotype.Component;

@Component
class BranchRouteBuilder extends ApplicationRouteBuilder {
    
    private static final String POST_URL = "https4:{{ksroute.api.url}}/branches.json";
    private static final String PUT_URL = "https4:{{ksroute.api.url}}/branches/${header.ksrId}.json";
    private static final String CACHE_KEY = "filial/${body.codigo}/${body.oraRowscn}";

    @Override
    public void configure() {
        super.configure();

        from("direct:process-filial").routeId("process-filial")                                
                .transform(simple("body.filial"))               
                .choice().when(simple("${body.ksrId} == null")).to("direct:post-branch")
                .otherwise().to("direct:put-branch").end();

        from("direct:post-branch").routeId("post-branch")
                .convertBodyTo(Branch.class).marshal().json(JsonLibrary.Jackson)
                .throttle(MAXIMUM_REQUEST_COUNT).timePeriodMillis(TIME_PERIOD_MILLIS).to(POST_URL)
                .unmarshal().json(JsonLibrary.Jackson, Branch.class)
                .bean(FilialService.class, "saveBranch");

        from("direct:put-branch").routeId("put-branch")              
                .idempotentConsumer(simple(CACHE_KEY), MemoryIdempotentRepository.memoryIdempotentRepository())
                .setHeader("CamelHttpMethod", constant("PUT"))
                .setHeader("ksrId", simple("body.ksrId"))
                .convertBodyTo(Branch.class).marshal().json(JsonLibrary.Jackson)
                .throttle(MAXIMUM_REQUEST_COUNT).timePeriodMillis(TIME_PERIOD_MILLIS).toD(PUT_URL)
                .unmarshal().json(JsonLibrary.Jackson, Branch.class)
                .bean(FilialService.class, "saveBranch");
                
    }
}
