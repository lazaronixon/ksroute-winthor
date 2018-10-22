package com.heuristica.ksroutewinthor;

import com.heuristica.ksroutewinthor.repositories.EventRepository;
import com.heuristica.ksroutewinthor.services.VeiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class MyCommands {
    
    @Autowired private EventRepository eventRepository;
    @Autowired private VeiculoService veiculoServices;
    
    @ShellMethod("Limpar fila de eventos.")
    public void clearQueue() {
        eventRepository.deleteAll();
    } 
    
    @ShellMethod("Carregar veículos.")
    public void loadVehicles() {
        veiculoServices.loadEvents();
    }    
    
}
