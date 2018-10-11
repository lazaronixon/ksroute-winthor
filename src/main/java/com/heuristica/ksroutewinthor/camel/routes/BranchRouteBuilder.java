package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.apis.Branch;
import com.heuristica.ksroutewinthor.services.FilialService;
import org.apache.camel.Exchange;
import static org.apache.camel.builder.PredicateBuilder.isNull;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.processor.idempotent.MemoryIdempotentRepository;
import org.springframework.stereotype.Component;

@Component
class BranchRouteBuilder extends RouteBuilder {
    
    private static final String POST_URL = "https://{{ksroute.api.url}}/branches.json";
    private static final String PUT_URL = "https://{{ksroute.api.url}}/branches/${body.ksrId}.json";
    private static final String CACHE_KEY = "filial/${body.codigo}/${body.oraRowscn}";

    @Override
    public void configure() {
        from("direct:process-filial").routeId("process-filial")                                                                
                .transform(simple("body.filial"))           
                .choice().when(isNull(simple("body.ksrId"))).to("direct:post-branch")
                .otherwise().to("direct:put-branch");

        from("direct:post-branch").routeId("post-branch")
                .transacted("PROPAGATION_REQUIRES_NEW")
                .setHeader(Exchange.HTTP_URI, simple(POST_URL))
                .convertBodyTo(Branch.class).marshal().json(JsonLibrary.Jackson)
                .to("seda:ksroute-api").unmarshal().json(JsonLibrary.Jackson, Branch.class)
                .bean(FilialService.class, "saveApiResponse");

        from("direct:put-branch").routeId("put-branch")
                .idempotentConsumer(simple(CACHE_KEY), MemoryIdempotentRepository.memoryIdempotentRepository())
                .setHeader(Exchange.HTTP_METHOD, constant("PUT"))
                .setHeader(Exchange.HTTP_URI, simple(PUT_URL))
                .convertBodyTo(Branch.class).marshal().json(JsonLibrary.Jackson)
                .to("seda:ksroute-api").unmarshal().json(JsonLibrary.Jackson, Branch.class);
                
    }
}
