package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.apis.Subregion;
import com.heuristica.ksroutewinthor.models.Praca;
import com.heuristica.ksroutewinthor.models.Regiao;
import com.heuristica.ksroutewinthor.models.Rota;
import com.heuristica.ksroutewinthor.services.PracaService;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.processor.idempotent.MemoryIdempotentRepository;
import org.apache.camel.util.toolbox.AggregationStrategies;
import org.springframework.stereotype.Component;

@Component
class SubregionRouteBuilder extends ApplicationRouteBuilder {

    @Override
    public void configure() {
        super.configure();

        from("direct:process-praca").routeId("process-praca")
                .transform(simple("body.praca")) 
                .enrich("direct:process-regiao", AggregationStrategies.bean(LineEnricher.class, "setRegiao"))
                .enrich("direct:process-rota", AggregationStrategies.bean(LineEnricher.class, "setRota"))                                
                .choice()
                .when(simple("${body.ksrId} == null")).to("direct:create-praca")
                .otherwise().to("direct:update-praca");

        from("direct:create-praca").routeId("create-praca")
                .idempotentConsumer(simple("praca/${body.oraRowscn}"), MemoryIdempotentRepository.memoryIdempotentRepository())
                .convertBodyTo(Subregion.class).marshal().json(JsonLibrary.Jackson)
                .throttle(50).timePeriodMillis(10000).to("https4://{{ksroute.api.url}}/subregions.json")
                .unmarshal().json(JsonLibrary.Jackson, Subregion.class)
                .bean(PracaService.class, "saveSubregion(${body})");

        from("direct:update-praca").routeId("update-praca")
                .idempotentConsumer(simple("praca/${body.oraRowscn}"), MemoryIdempotentRepository.memoryIdempotentRepository())
                .setHeader("CamelHttpMethod", constant("PUT"))
                .setHeader("ksrId", simple("body.ksrId"))                
                .convertBodyTo(Subregion.class).marshal().json(JsonLibrary.Jackson)
                .throttle(50).timePeriodMillis(10000).recipientList(simple("https4://{{ksroute.api.url}}/subregions/${header.ksrId}.json"))
                .unmarshal().json(JsonLibrary.Jackson, Subregion.class)
                .bean(PracaService.class, "saveSubregion(${body})");
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
