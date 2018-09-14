package com.heuristica.ksroutewinthor.services;

import com.heuristica.ksroutewinthor.apis.Line;
import com.heuristica.ksroutewinthor.models.Rota;
import com.heuristica.ksroutewinthor.models.RotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RotaService {

    @Autowired
    private RotaRepository rotas;

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW) 
    public Rota findRota(Long id) {
        return rotas.findById(id).get();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW) 
    public Rota saveLine(Line line) {
        Rota rota = findRota(Long.parseLong(line.getErpId()));
        rota.setKsrId(line.getId());
        return rotas.save(rota);
    }

}
