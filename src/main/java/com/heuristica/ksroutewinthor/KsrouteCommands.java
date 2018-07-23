package com.heuristica.ksroutewinthor;

import com.heuristica.ksroutewinthor.models.RegiaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.Table;
import org.springframework.shell.table.TableBuilder;
import org.springframework.shell.table.TableModel;
import org.springframework.shell.table.TableModelBuilder;

@ShellComponent
public class KsrouteCommands {

    @Autowired RegiaoRepository regiaoRepository;

    @ShellMethod("List regions")
    public Table list() {
        TableModelBuilder tableModelBuilder = new TableModelBuilder();
        regiaoRepository.findAll().forEach((r) -> { 
            tableModelBuilder.addRow();
            tableModelBuilder.addValue(r); 
        });
        TableModel model = tableModelBuilder.build();
        TableBuilder tableBuilder = new TableBuilder(model);
        return tableBuilder.addFullBorder(BorderStyle.fancy_light).build();
    }
}
