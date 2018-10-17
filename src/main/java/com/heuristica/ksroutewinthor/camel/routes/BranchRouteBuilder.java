package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.apis.Branch;
import com.heuristica.ksroutewinthor.services.FilialService;
import com.heuristica.ksroutewinthor.services.RecordService;
import org.apache.camel.Exchange;
import static org.apache.camel.builder.PredicateBuilder.isNull;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
class BranchRouteBuilder extends RouteBuilder {

    private static final String BRANCHES_URL = "https://{{ksroute.api.url}}/branches.json";
    private static final String BRANCH_URL = "https://{{ksroute.api.url}}/branches/${body.record.remoteId}.json";    
    private static final String DELETE_BRANCH_URL = "https://{{ksroute.api.url}}/branches/${body.remoteId}.json";

    @Override
    public void configure() {
        from("direct:EVENT-SAVE-PCFILIAL").routeId("save-branch")
                .bean(FilialService.class, "findByEvent")
                .choice().when(isNull(simple("body.record"))).to("direct:post-branch")
                .otherwise().to("direct:put-branch");
        
        from("direct:EVENT-DELETE-PCFILIAL").routeId("delete-branch")
                .bean(RecordService.class, "findByEvent")
                .setHeader(Exchange.HTTP_METHOD, constant("DELETE"))
                .setHeader(Exchange.HTTP_URI, simple(DELETE_BRANCH_URL))
                .setBody(constant(null)).to("direct:ksroute-api");        
        
        from("direct:enrich-branch").routeId("enrich-branch")
                .transform(simple("body.filial"))
                .to("direct:post-branch");  
        
        from("direct:post-branch").routeId("post-branch")
                .transacted("PROPAGATION_REQUIRES_NEW")
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.HTTP_URI, simple(BRANCHES_URL))
                .convertBodyTo(Branch.class).marshal().json(JsonLibrary.Jackson)
                .to("direct:ksroute-api").unmarshal().json(JsonLibrary.Jackson, Branch.class)
                .bean(FilialService.class, "saveResponseApi");

        from("direct:put-branch").routeId("put-branch")
                .setHeader(Exchange.HTTP_METHOD, constant("PUT"))
                .setHeader(Exchange.HTTP_URI, simple(BRANCH_URL))
                .convertBodyTo(Branch.class).marshal().json(JsonLibrary.Jackson)
                .to("direct:ksroute-api");
    }
}
