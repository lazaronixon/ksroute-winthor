package com.heuristica.ksroutewinthor.services;

import com.heuristica.ksroutewinthor.models.Praca;
import com.heuristica.ksroutewinthor.models.PracaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PracaService {

    @Autowired
    private PracaRepository pracas;

    public Praca findPraca(Long id) {
        return pracas.findById(id).get();
    }

    public Praca savePraca(Praca praca) {
        return pracas.save(praca);
    }

}
