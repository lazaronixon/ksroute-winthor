package com.heuristica.ksroutewinthor.services;

import com.heuristica.ksroutewinthor.apis.Subregion;
import com.heuristica.ksroutewinthor.models.Event;
import com.heuristica.ksroutewinthor.models.Praca;
import com.heuristica.ksroutewinthor.models.PracaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PracaService {

    @Autowired private PracaRepository pracas;
      
    public Praca saveApiResponse(Subregion subregion) {
        Praca praca = pracas.findById(Long.parseLong(subregion.getErpId())).get();
        praca.setKsrId(subregion.getId());
        return pracas.save(praca);
    }
    
    public Praca getEventable(Event event) {
        return pracas.findById(Long.parseLong(event.getEventableId())).orElse(null);
    }

}
