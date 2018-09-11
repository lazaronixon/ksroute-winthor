package com.heuristica.ksroutewinthor.services;

import com.heuristica.ksroutewinthor.models.Regiao;
import com.heuristica.ksroutewinthor.models.RegiaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegiaoService {

    @Autowired
    private RegiaoRepository regioes;

    public Regiao findRegiao(Long id) {
        return regioes.findById(id).get();
    }

    public Regiao saveRegiao(Regiao regiao) {
        return regioes.save(regiao);
    }

}
