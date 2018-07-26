package com.heuristica.ksroutewinthor.camel.processors;

import com.heuristica.ksroutewinthor.models.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderProcessor extends ApplicationProcessor {

    @Autowired PedidoRepository pedidoRepository;


}
