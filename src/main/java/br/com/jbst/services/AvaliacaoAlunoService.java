package br.com.jbst.services;


import br.com.jbst.entities.Pergunta;
import br.com.jbst.repositoriesEAD.PerguntaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AvaliacaoAlunoService {

    private final PerguntaRepository perguntaRepository;

    public double calcularNota(List<UUID> respostasSelecionadas, UUID idAvaliacao) {
        List<Pergunta> perguntas = perguntaRepository.findByAvaliacao_IdAvaliacaoCurso(idAvaliacao);

        if (perguntas.isEmpty()) {
            throw new EntityNotFoundException("Nenhuma pergunta encontrada para esta avaliação.");
        }

        int total = perguntas.size();
        int acertos = 0;

        for (Pergunta pergunta : perguntas) {
            boolean acertou = pergunta.getRespostas().stream()
                .anyMatch(resposta ->
                    resposta.isCorreta() && respostasSelecionadas.contains(resposta.getIdResposta())
                );

            if (acertou) acertos++;
        }

        return (10.0 * acertos) / total;
    }

    public boolean isAprovado(double nota) {
        return nota >= 7.0;
    }
}
