package com.heuristica.ksroutewinthor.services;

import com.heuristica.ksroutewinthor.apis.Branch;
import com.heuristica.ksroutewinthor.models.Filial;
import com.heuristica.ksroutewinthor.models.FilialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FilialService {

    @Autowired private FilialRepository filiais;

    public Filial saveBranch(Branch branch) {
        Filial filial = filiais.findById(branch.getErpId()).get();
        filial.setKsrId(branch.getId());
        return filiais.save(filial);
    }

}
