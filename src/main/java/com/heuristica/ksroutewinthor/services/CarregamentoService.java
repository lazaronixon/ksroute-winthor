package com.heuristica.ksroutewinthor.services;

import com.google.common.base.Ascii;
import com.heuristica.ksroutewinthor.apis.Activity;
import com.heuristica.ksroutewinthor.apis.Order;
import com.heuristica.ksroutewinthor.apis.Route;
import com.heuristica.ksroutewinthor.models.Carregamento;
import com.heuristica.ksroutewinthor.repositories.CarregamentoRepository;
import com.heuristica.ksroutewinthor.models.Rota;
import com.heuristica.ksroutewinthor.dto.CarregPedidoSum;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static java.util.Comparator.comparing;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.stream.Collectors.toList;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CarregamentoService {
    
    @PersistenceContext private EntityManager entityManager;    
    
    @Autowired private Environment env;    
    @Autowired private ConsumService consumService; 
    @Autowired private PedidoService pedidoService;
    @Autowired private CarregamentoRepository carregamentos;     
                
    public Route saveRoute(Route route) {
        Carregamento carregamento = new Carregamento();
        carregamento.setNumcar(consumService.getNextSequence("proxnumcar"));
        carregamento.setCodfuncmon(Long.parseLong(env.getProperty("ksroute.codfuncmon")));
        carregamento.setCodmotorista(Long.parseLong(env.getProperty("ksroute.codmanobrista")));
        carregamento.setCodveiculo(Long.parseLong(route.getVehicle().getErpId()));
        carregamento.setDtsaida(LocalDate.now());
        carregamento.setDatamon(LocalDate.now());
        carregamento.setHoramon(LocalDateTime.now().getHour());
        carregamento.setMinutomon(LocalDateTime.now().getMinute());
        carregamento.setSegundomon(LocalDateTime.now().getSecond());       
        
        AtomicInteger seqIndex = new AtomicInteger();
                
        List<Order> orders = route.getActivities().stream()
                .sorted(comparing(Activity::getPosition))
                .flatMap(r -> r.getOrders().stream())                
                .collect(toList()); 
        
        orders.forEach(order -> {
            try {
                pedidoService.findById(Long.parseLong(order.getErpId())).ifPresent(pedido -> {
                    int index = seqIndex.getAndIncrement();
                    pedido.setNumseqmontagem(index + 1);
                    pedido.setNumseqentrega(Long.valueOf(index + 1));
                    pedido.setNumordemcarga(orders.size() - index);                
                    pedido.setPosicao("M");
                
                    carregamento.addToPedidoList(pedido);                    
                });                
            } catch (NoSuchElementException ex) {
                String msg = String.format("Pedido %s nao encontrado", order.getErpId());
                Logger.getLogger(CarregamentoService.class.getName()).log(Level.SEVERE, msg, ex);
            }
        });
        
        if (carregamento.getPedidoList().isEmpty() == false) {
            carregamentos.save(carregamento);
            carregamento.setRotaPrinc(findRodaPrinc(carregamento));            
            carregamento.setDestino(Ascii.truncate(carregamento.getRotaPrinc().getDescricao(), 19, ""));

            CarregPedidoSum pedidoSum = sumByCarreg(carregamento);
            carregamento.setVltotal(pedidoSum.getVltotal()); 
            carregamento.setTotpeso(pedidoSum.getTotpeso());
            carregamento.setTotvolume(pedidoSum.getTotvolume());
            carregamento.setNumnotas(pedidoSum.getNumnotas());
            carregamento.setNument(pedidoSum.getNument());
            carregamento.setNumcid(pedidoSum.getNumcid());
            carregamento.setQtitens(pedidoSum.getQtitens());
            carregamentos.save(carregamento);  
        }
        return route;
    }
    
    private Rota findRodaPrinc(Carregamento carregamento) {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT p.cliente.praca.rota.codrota "
                + "FROM Pedido p "
                + "WHERE p.carregamento = :carregamento "
                + "GROUP BY p.cliente.praca.rota.codrota "
                + "ORDER BY COUNT(p) DESC", Long.class);
        query.setParameter("carregamento", carregamento);
        query.setMaxResults(1);
        return entityManager.find(Rota.class, query.getSingleResult());
    }    
    
    private CarregPedidoSum sumByCarreg(Carregamento carregamento) {
        TypedQuery<CarregPedidoSum> query = entityManager.createQuery(
                "SELECT NEW com.heuristica.ksroutewinthor.dto.CarregPedidoSum( "
                + "SUM(o.vlatend), "
                + "SUM(o.totpeso), "
                + "SUM(o.totvolume), "                        
                + "COUNT(DISTINCT o), "
                + "COUNT(DISTINCT o.cliente.praca), "
                + "COUNT(DISTINCT o.cliente), "
                + "MAX((SELECT COUNT(DISTINCT pi.codprod) FROM PedidoItem pi WHERE pi.pedido.carregamento = o.carregamento))) "
                + "FROM Pedido o "
                + "WHERE o.carregamento = :carregamento ", CarregPedidoSum.class);
        query.setParameter("carregamento", carregamento);
        return query.getSingleResult();
    } 
    
}
