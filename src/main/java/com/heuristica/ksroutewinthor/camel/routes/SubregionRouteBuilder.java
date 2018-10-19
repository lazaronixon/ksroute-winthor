package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.apis.Subregion;
import com.heuristica.ksroutewinthor.models.Praca;
import com.heuristica.ksroutewinthor.models.Regiao;
import com.heuristica.ksroutewinthor.models.Rota;
import com.heuristica.ksroutewinthor.services.PracaService;
import com.heuristica.ksroutewinthor.services.RecordService;
import org.apache.camel.Exchange;
import static org.apache.camel.builder.PredicateBuilder.isNotNull;
import static org.apache.camel.builder.PredicateBuilder.isNull;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.util.toolbox.AggregationStrategies;
import org.springframework.stereotype.Component;

@Component
class SubregionRouteBuilder extends RouteBuilder {
    
    private static final String SUBREGIONS_URL = "https://{{ksroute.api.url}}/subregions.json";
    private static final String SUBREGION_URL = "https://{{ksroute.api.url}}/subregions/${header.remoteId}.json"; 

    @Override
    public void configure() {
        from("direct:Event-Insert-Praca").routeId("Event-Insert-Praca")
                .bean(PracaService.class, "findByEvent")
                .filter(isNotNull(body()))
                .filter(isNull(simple("body.record")))
                .to("direct:post-subregion");        
        
        from("direct:Event-Update-Praca").routeId("Event-Update-Praca")
                .bean(PracaService.class, "findByEvent")
                .filter(isNotNull(body()))
                .choice().when(isNull(simple("body.record"))).to("direct:post-subregion")
                .otherwise().to("direct:put-subregion");
        
        from("direct:Event-Delete-Praca").routeId("Event-Delete-Praca")
                .bean(RecordService.class, "findByEvent")
                .filter(isNotNull(body()))
                .to("direct:delete-subregion");
        
        from("direct:post-subregion").routeId("post-subregion")
                .transacted("PROPAGATION_REQUIRES_NEW")
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.HTTP_URI, simple(SUBREGIONS_URL))
                .enrich("direct:enrich-region", AggregationStrategies.bean(LineEnricher.class, "setRegiao"))
                .enrich("direct:enrich-line", AggregationStrategies.bean(LineEnricher.class, "setRota")) 
                .convertBodyTo(Subregion.class).marshal().json(JsonLibrary.Jackson)
                .to("direct:ksroute-api").unmarshal().json(JsonLibrary.Jackson, Subregion.class)
                .bean(PracaService.class, "saveResponse");

        from("direct:put-subregion").routeId("put-subregion")
                .transacted("PROPAGATION_REQUIRES_NEW")
                .setHeader("remoteId", simple("body.record.remoteId"))
                .setHeader(Exchange.HTTP_METHOD, constant("PUT"))
                .setHeader(Exchange.HTTP_URI, simple(SUBREGION_URL))
                .enrich("direct:enrich-region", AggregationStrategies.bean(LineEnricher.class, "setRegiao"))
                .enrich("direct:enrich-line", AggregationStrategies.bean(LineEnricher.class, "setRota"))
                .convertBodyTo(Subregion.class).marshal().json(JsonLibrary.Jackson)
                .to("direct:ksroute-api").unmarshal().json(JsonLibrary.Jackson, Subregion.class)
                .bean(PracaService.class, "saveResponse");  
        
        from("direct:delete-subregion").routeId("delete-subregion")
                .transacted("PROPAGATION_REQUIRES_NEW")
                .setHeader("recordId", simple("body.id"))
                .setHeader("remoteId", simple("body.remoteId"))
                .setHeader(Exchange.HTTP_METHOD, constant("DELETE"))
                .setHeader(Exchange.HTTP_URI, simple(SUBREGION_URL))
                .setBody(constant(null)).to("direct:ksroute-api")
                .bean(RecordService.class, "deleteByRecordId");    
        
        from("direct:enrich-subregion").routeId("enrich-subregion")
                .transform(simple("body.praca"))
                .bean(RecordService.class, "fetchRecord")
                .filter(isNull(simple("body.record")))
                .to("direct:post-subregion");     
    }

    public class LineEnricher {

        public Praca setRegiao(Praca praca, Regiao regiao) {
            praca.setRegiao(regiao);
            return praca;
        }
        
        public Praca setRota(Praca praca, Rota rota) {
            praca.setRota(rota);
            return praca;
        }        
    }

}
