package com.heuristica.ksroutewinthor.camel.routes;

import com.heuristica.ksroutewinthor.apis.Subregion;
import com.heuristica.ksroutewinthor.models.Praca;
import com.heuristica.ksroutewinthor.models.Regiao;
import com.heuristica.ksroutewinthor.models.Rota;
import org.apache.camel.model.dataformat.JsonLibrary;
import static org.apache.camel.processor.idempotent.MemoryIdempotentRepository.memoryIdempotentRepository;
import org.apache.camel.util.toolbox.AggregationStrategies;
import org.springframework.stereotype.Component;

@Component
class SubregionRouteBuilder extends ApplicationRouteBuilder {

    @Override
    public void configure() {
        super.configure();

        from("direct:process-praca").routeId("process-praca")
                .transform(simple("body.praca"))
                .idempotentConsumer(simple("praca/${body.codpraca}/${body.oraRowscn}"), memoryIdempotentRepository(100))
                .enrich("direct:process-regiao", AggregationStrategies.bean(LineEnricher.class, "setRegiao"))
                .enrich("direct:process-rota", AggregationStrategies.bean(LineEnricher.class, "setRota"))
                .choice().when(simple("${body.ksrId} == null")).to("direct:create-praca")
                .otherwise().to("direct:update-praca")
                .unmarshal().json(JsonLibrary.Jackson, Subregion.class);

        from("direct:create-praca").routeId("create-praca")
                .convertBodyTo(Subregion.class).marshal().json(JsonLibrary.Jackson)
                .throttle(5).to("https4://{{ksroute.api.url}}/subregions.json")
                .unmarshal().json(JsonLibrary.Jackson, Subregion.class)
                .toD("jpa?query=UPDATE Praca p SET p.ksrId = ${body.id} WHERE p.codpraca = ${body.erpId}").end();

        from("direct:update-praca").routeId("update-praca")
                .setHeader("CamelHttpMethod", constant("PUT"))
                .setHeader("ksrId", simple("body.ksrId"))
                .convertBodyTo(Praca.class).marshal().json(JsonLibrary.Jackson)
                .throttle(5).recipientList(simple("https4://{{ksroute.api.url}}/subregions/${header.ksrId}.json"));
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
