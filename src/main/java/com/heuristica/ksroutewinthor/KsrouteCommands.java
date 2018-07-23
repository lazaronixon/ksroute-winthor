package com.heuristica.ksroutewinthor;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class KsrouteCommands {

    @ShellMethod("List regions")
    public int add(int a, int b) {
        return a + b;
    }
}