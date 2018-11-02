package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.apis.Branch;
import com.heuristica.ksroutewinthor.services.FilialService;
import com.heuristica.ksroutewinthor.services.RecordService;
import org.apache.camel.Exchange;
import static org.apache.camel.builder.PredicateBuilder.isNull;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
class BranchRouteBuilder extends ApplicationRouteBuilder {

    private static final String BRANCHES_URL = "https://{{ksroute.api.url}}/branches.json";
    private static final String BRANCH_URL = "https://{{ksroute.api.url}}/branches/${header.remoteId}.json";

    @Override
    public void configure() throws Exception {
        super.configure();
        
        from("direct:event-save-filial").routeId("event-save-filial")
                .bean(FilialService.class, "findByEvent")
                .choice().when(isNull(simple("body.record"))).to("direct:post-branch")
                .otherwise().to("direct:put-branch");   
        
        from("direct:event-delete-filial").routeId("event-delete-filial")
                .transform(simple("body.deletedRecord"))
                .to("direct:delete-branch");
        
        from("direct:enrich-branch").routeId("enrich-branch")
                .transform(simple("body.filial"))
                .filter(isNull(simple("body.record")))
                .to("direct:post-branch");    
        
        from("direct:post-branch").routeId("post-branch")
                .transacted("PROPAGATION_REQUIRES_NEW")
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.HTTP_URI, simple(BRANCHES_URL))
                .convertBodyTo(Branch.class).marshal().json(JsonLibrary.Jackson)
                .to("direct:ksroute-api").unmarshal().json(JsonLibrary.Jackson, Branch.class)
                .bean(FilialService.class, "saveResponse");

        from("direct:put-branch").routeId("put-branch")
                .transacted("PROPAGATION_REQUIRES_NEW")
                .setHeader("remoteId", simple("body.record.remoteId"))
                .setHeader(Exchange.HTTP_METHOD, constant("PUT"))
                .setHeader(Exchange.HTTP_URI, simple(BRANCH_URL))
                .convertBodyTo(Branch.class).marshal().json(JsonLibrary.Jackson)
                .to("direct:ksroute-api").unmarshal().json(JsonLibrary.Jackson, Branch.class)
                .bean(FilialService.class, "saveResponse");
        
        from("direct:delete-branch").routeId("delete-branch")
                .transacted("PROPAGATION_REQUIRES_NEW")
                .setHeader("recordId", simple("body.id"))
                .setHeader("remoteId", simple("body.remoteId"))
                .setHeader(Exchange.HTTP_METHOD, constant("DELETE"))
                .setHeader(Exchange.HTTP_URI, simple(BRANCH_URL))
                .setBody(constant(null)).to("direct:ksroute-api")
                .bean(RecordService.class, "deleteByRecordId");
    }
}
