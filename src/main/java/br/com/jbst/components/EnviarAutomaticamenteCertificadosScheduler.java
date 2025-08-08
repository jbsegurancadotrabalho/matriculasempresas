package br.com.jbst.components;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.jbst.entities.Empresa;
import br.com.jbst.entities.Turmas;
import br.com.jbst.repositories.EmpresaRepository;
import br.com.jbst.repositories.TurmasRepository;
import br.com.jbst.services.EnvioLinkCertificadosService;

@Component
public class EnviarAutomaticamenteCertificadosScheduler {

    @Autowired
    private TurmasRepository turmasRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private EnvioLinkCertificadosService envioService;

    @Scheduled(cron = "0 00 16 * * *") // Primeira execução: dia do encerramento
    public void enviarCertificadosNoDiaDoEncerramento() {
        Instant hoje = Instant.now().truncatedTo(ChronoUnit.DAYS);
        enviarCertificadosPorDataFim(hoje);
    }

    @Scheduled(cron = "0 00 16 * * *") // Segunda execução: 24h depois
    public void reenviarCertificadosUmDiaDepois() {
        Instant ontem = Instant.now().minus(1, ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS);
        enviarCertificadosPorDataFim(ontem);
    }

    // Método auxiliar reutilizado por ambos
    private void enviarCertificadosPorDataFim(Instant data) {
        List<Turmas> turmasEncerradas = turmasRepository.findByDatafimBetween(data, data.plus(1, ChronoUnit.DAYS));

        for (Turmas turma : turmasEncerradas) {
            List<Empresa> empresas = empresaRepository
                .findDistinctByFuncionarios_Matriculas_Turmas_IdTurmas(turma.getIdTurmas());

            for (Empresa empresa : empresas) {
                envioService.enviarLinksPorEmail(empresa, turma);
            }
        }
    }

    }
    

