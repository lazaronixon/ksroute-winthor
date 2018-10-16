package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.apis.Subregion;
import com.heuristica.ksroutewinthor.models.Praca;
import com.heuristica.ksroutewinthor.models.Regiao;
import com.heuristica.ksroutewinthor.models.Rota;
import com.heuristica.ksroutewinthor.services.PracaService;
import org.apache.camel.Exchange;
import static org.apache.camel.builder.PredicateBuilder.isNull;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.util.toolbox.AggregationStrategies;
import org.springframework.stereotype.Component;

@Component
class SubregionRouteBuilder extends RouteBuilder {
    
    private static final String SUBREGIONS_URL = "https://{{ksroute.api.url}}/subregions.json";
    private static final String SUBREGION_URL = "https://{{ksroute.api.url}}/subregions/${body.ksrId}.json"; 

    @Override
    public void configure() {
        from("direct:save-subregion").routeId("save-subregion")
                .bean(PracaService.class, "getEventable")
                .enrich("direct:enrich-region", AggregationStrategies.bean(LineEnricher.class, "setRegiao"))
                .enrich("direct:enrich-line", AggregationStrategies.bean(LineEnricher.class, "setRota")) 
                .choice().when(isNull(simple("body.ksrId"))).to("direct:post-subregion")
                .otherwise().to("direct:put-subregion");
        
        from("direct:post-subregion").routeId("post-subregion")
                .transacted("PROPAGATION_REQUIRES_NEW")
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.HTTP_URI, simple(SUBREGIONS_URL))
                .convertBodyTo(Subregion.class).marshal().json(JsonLibrary.Jackson)
                .to("direct:ksroute-api").unmarshal().json(JsonLibrary.Jackson, Subregion.class)
                .bean(PracaService.class, "saveApiResponse");

        from("direct:put-subregion").routeId("put-subregion")
                .setHeader(Exchange.HTTP_METHOD, constant("PUT"))
                .setHeader(Exchange.HTTP_URI, simple(SUBREGION_URL))
                .convertBodyTo(Subregion.class).marshal().json(JsonLibrary.Jackson)
                .to("direct:ksroute-api").unmarshal().json(JsonLibrary.Jackson, Subregion.class);

        from("direct:delete-subregion").routeId("delete-subregion")
                .setHeader(Exchange.HTTP_METHOD, constant("DELETE"))
                .setHeader(Exchange.HTTP_URI, simple(SUBREGION_URL))
                .setBody(constant(null)).to("direct:ksroute-api");
        
        from("direct:enrich-subregion").routeId("enrich-subregion")
                .transform(simple("body.praca"))
                .filter(isNull(simple("body.ksrId")))
                .enrich("direct:enrich-region", AggregationStrategies.bean(LineEnricher.class, "setRegiao"))
                .enrich("direct:enrich-line", AggregationStrategies.bean(LineEnricher.class, "setRota")) 
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
