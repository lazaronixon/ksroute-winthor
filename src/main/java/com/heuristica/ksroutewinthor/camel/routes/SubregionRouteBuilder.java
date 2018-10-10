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
import org.apache.camel.processor.idempotent.MemoryIdempotentRepository;
import org.apache.camel.util.toolbox.AggregationStrategies;
import org.springframework.stereotype.Component;

@Component
class SubregionRouteBuilder extends RouteBuilder {
    
    private static final String POST_URL = "https://{{ksroute.api.url}}/subregions.json";
    private static final String PUT_URL = "https://{{ksroute.api.url}}/subregions/${body.ksrId}.json";
    private static final String CACHE_KEY = "praca/${body.codpraca}/${body.oraRowscn}";    

    @Override
    public void configure() {
        from("direct:process-praca").routeId("process-praca")
                .transform(simple("body.praca")) 
                .enrich("direct:process-regiao", AggregationStrategies.bean(LineEnricher.class, "setRegiao"))
                .enrich("direct:process-rota", AggregationStrategies.bean(LineEnricher.class, "setRota"))                                
                .choice().when(isNull(simple("body.ksrId"))).to("direct:post-subregion")
                .otherwise().to("direct:put-subregion");

        from("direct:post-subregion").routeId("post-subregion")
                .setHeader(Exchange.HTTP_URI, simple(POST_URL))
                .convertBodyTo(Subregion.class).marshal().json(JsonLibrary.Jackson)
                .to("direct:ksroute-api").unmarshal().json(JsonLibrary.Jackson, Subregion.class)
                .bean(PracaService.class, "saveSubregion");

        from("direct:put-subregion").routeId("put-subregion")             
                .idempotentConsumer(simple(CACHE_KEY), MemoryIdempotentRepository.memoryIdempotentRepository())
                .setHeader(Exchange.HTTP_METHOD, constant("PUT"))
                .setHeader(Exchange.HTTP_URI, simple(PUT_URL))               
                .convertBodyTo(Subregion.class).marshal().json(JsonLibrary.Jackson)
                .to("direct:ksroute-api").unmarshal().json(JsonLibrary.Jackson, Subregion.class)
                .bean(PracaService.class, "saveSubregion");
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
