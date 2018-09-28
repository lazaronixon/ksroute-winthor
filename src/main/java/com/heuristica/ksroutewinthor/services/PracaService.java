package com.heuristica.ksroutewinthor.services;

import com.heuristica.ksroutewinthor.apis.Subregion;
import com.heuristica.ksroutewinthor.models.Praca;
import com.heuristica.ksroutewinthor.models.PracaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PracaService {

    @Autowired
    private PracaRepository pracas;
      
    @Transactional(propagation = Propagation.REQUIRES_NEW)      
    public Praca saveSubregion(Subregion subregion) {
        Praca praca = pracas.findById(Long.parseLong(subregion.getErpId())).get();
        praca.setKsrId(subregion.getId());
        return pracas.save(praca);
    }

}
