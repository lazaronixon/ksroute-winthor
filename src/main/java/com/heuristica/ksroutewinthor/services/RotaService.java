package com.heuristica.ksroutewinthor.services;

import com.heuristica.ksroutewinthor.models.Rota;
import com.heuristica.ksroutewinthor.models.RotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RotaService {

    @Autowired
    private RotaRepository rotas;

    public Rota findRota(Long id) {
        return rotas.findById(id).get();
    }

    public Rota saveRota(Rota rota) {
        return rotas.save(rota);
    }

}
