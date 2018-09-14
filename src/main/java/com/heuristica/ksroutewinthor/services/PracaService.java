package com.heuristica.ksroutewinthor.services;

import com.heuristica.ksroutewinthor.apis.Subregion;
import com.heuristica.ksroutewinthor.models.Praca;
import com.heuristica.ksroutewinthor.models.PracaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PracaService {

    @Autowired
    private PracaRepository pracas;

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW) 
    public Praca findPraca(Long id) {
        Praca praca = pracas.findById(id).get();
        return praca;
    }
      
    @Transactional(propagation = Propagation.REQUIRES_NEW)      
    public Praca saveSubregion(Subregion subregion) {
        Praca praca = findPraca(Long.parseLong(subregion.getErpId()));
        praca.setKsrId(subregion.getId());
        return pracas.save(praca);
    }

}
