package com.heuristica.ksroutewinthor.services;

import com.heuristica.ksroutewinthor.apis.Line;
import com.heuristica.ksroutewinthor.models.Event;
import com.heuristica.ksroutewinthor.models.Rota;
import com.heuristica.ksroutewinthor.models.RotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RotaService {

    @Autowired private RotaRepository rotas;

    public Rota saveApiResponse(Line line) {
        Rota rota = rotas.findById(Long.parseLong(line.getErpId())).get();
        //rota.setKsrId(line.getId());
        return rotas.save(rota);
    }
    
    public Rota getEventable(Event event) {
        return rotas.findById(Long.parseLong(event.getEventableId())).orElse(null);
    }

}
