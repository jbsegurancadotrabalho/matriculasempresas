package br.com.jbst.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.jbst.controller.TesteAlertaController;
import jakarta.annotation.PostConstruct;

@Component
public class AlertaVencimentoStartupTest {

    @Autowired
    private TesteAlertaController testeAlertaController;

    @PostConstruct
    public void executarAoIniciar() {
        testeAlertaController.testarEnvio();
    }
}
