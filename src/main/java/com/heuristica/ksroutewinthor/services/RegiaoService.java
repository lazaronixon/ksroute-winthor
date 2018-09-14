package com.heuristica.ksroutewinthor.services;

import com.heuristica.ksroutewinthor.apis.Region;
import com.heuristica.ksroutewinthor.models.Regiao;
import com.heuristica.ksroutewinthor.models.RegiaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RegiaoService {

    @Autowired
    private RegiaoRepository regioes;
   
    @Transactional(propagation = Propagation.REQUIRES_NEW)    
    public Regiao saveRegion(Region region) {
        Regiao regiao = regioes.findById(Long.parseLong(region.getErpId())).get();
        regiao.setKsrId(region.getId());
        return regioes.save(regiao);
    }

}
