package com.heuristica.ksroutewinthor.services;

import com.heuristica.ksroutewinthor.apis.Region;
import com.heuristica.ksroutewinthor.models.Regiao;
import com.heuristica.ksroutewinthor.models.RegiaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RegiaoService {

    @Autowired private RegiaoRepository regioes;
    
    public Regiao saveRegion(Region region) {
        Regiao regiao = regioes.findById(Long.parseLong(region.getErpId())).get();
        regiao.setKsrId(region.getId());
        return regioes.save(regiao);
    }

}
