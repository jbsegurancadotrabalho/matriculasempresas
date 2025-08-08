package br.com.jbst.services;


import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jbst.DTO.GetMatriculaGratuitaDTO;
import br.com.jbst.DTO.MailSenderDto;
import br.com.jbst.DTO.PostMatriculaGratuitaDTO;
import br.com.jbst.DTO.PutMatriculaGratuitaDTO;
import br.com.jbst.DTO.SendMessageZapDTO;
import br.com.jbst.components.MatriculasMessageProducer;
import br.com.jbst.components.ZApiSenderComponent;
import br.com.jbst.entities.MatriculasGratuita;
import br.com.jbst.entities.Turmas;
import br.com.jbst.repositories.MatriculaGratuitaRepository;

@Service
public class MatriculaGratuitaService {

    @Autowired
    private MatriculaGratuitaRepository repository;

    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private MatriculasMessageProducer matriculasMessageProducer;

    @Autowired
    private ZApiSenderComponent zApiSenderComponent;

    public GetMatriculaGratuitaDTO criar(PostMatriculaGratuitaDTO dto) {
        MatriculasGratuita entity = modelMapper.map(dto, MatriculasGratuita.class);
        entity.setIdMatriculaGratuita(UUID.randomUUID());
        entity.setDataHoraCriacao(Instant.now());
        entity.setNumeroMatriculaGratuita(gerarNumeroMatricula());

        if (dto.getIdTurmas() != null) {
            Turmas turma = new Turmas();
            turma.setIdTurmas(dto.getIdTurmas());
            entity.setTurmas(turma);
        }

        MatriculasGratuita saved = repository.save(entity);
        
        // ✅ Enviar e-mail e WhatsApp
        enviarComunicadosMatricula(saved);

        return modelMapper.map(saved, GetMatriculaGratuitaDTO.class);
    }

    public Optional<GetMatriculaGratuitaDTO> atualizar(UUID id, PutMatriculaGratuitaDTO dto) {
        return repository.findById(id).map(existing -> {
            modelMapper.map(dto, existing);

            if (dto.getIdTurmas() != null) {
                Turmas turma = new Turmas();
                turma.setIdTurmas(dto.getIdTurmas());
                existing.setTurmas(turma);
            } else {
                existing.setTurmas(null);
            }

            MatriculasGratuita updated = repository.save(existing);
            
            // ✅ Enviar e-mail e WhatsApp
            enviarComunicadosMatricula(updated);

            return modelMapper.map(updated, GetMatriculaGratuitaDTO.class);
        });
    }

    public Optional<GetMatriculaGratuitaDTO> buscarPorId(UUID id) {
        return repository.findById(id)
                .map(entity -> modelMapper.map(entity, GetMatriculaGratuitaDTO.class));
    }

    public List<GetMatriculaGratuitaDTO> listarTodas() {
        return repository.findAll().stream()
                .map(entity -> modelMapper.map(entity, GetMatriculaGratuitaDTO.class))
                .collect(Collectors.toList());
    }
    
    private int gerarNumeroMatricula() {
		Integer ultimoNumero = repository.findMaxNumeroMatricula();
		if (ultimoNumero == null) {
			ultimoNumero = 0;
		}
		return ultimoNumero + 1;
	}
    
    public void enviarComunicadosMatricula(MatriculasGratuita matricula) {
        String linkMatricula = "http://jbseguranca.s3-website.us-east-2.amazonaws.com/fazer-curso-gratuito/" + matricula.getIdMatriculaGratuita();

        String numeroMatricula = matricula.getNumeroMatriculaGratuita() != null ? matricula.getNumeroMatriculaGratuita().toString() : "N/A";
        String idMatricula = matricula.getIdMatriculaGratuita() != null ? matricula.getIdMatriculaGratuita().toString() : "N/A";
        String numeroTurma = (matricula.getTurmas() != null && matricula.getTurmas().getNumeroTurma() != null)
                ? matricula.getTurmas().getNumeroTurma().toString()
                : "N/A";

        String centrosHtml = String.format("""
            <br>
            <p><strong>Centros de Treinamento JB Segurança do Trabalho:</strong></p>
            <ul>
                <li>📍 Centro do Rio: Rua Moncorvo Filho, 99 - Loja - Centro do Rio de Janeiro - RJ</li>
                <li>📍 São Paulo: Rua Siqueira Bueno, 1321 - Belém - São Paulo - SP</li>
            </ul>
            <p><strong>⚠️ Importante:</strong> Agendamento da parte prática exclusivamente para NR-35 e NR-33, mediante envio de e-mail para <a href="mailto:operacional@jbsegurancadotrabalho.com.br">operacional@jbsegurancadotrabalho.com.br</a> informando o ID da matrícula: <strong>%s</strong></p>
        """, idMatricula);

        String centrosTexto = String.format("""
            CENTROS DE TREINAMENTO JB:

            📍 Centro do Rio:
            Rua Moncorvo Filho, 99 - Loja - Centro do Rio de Janeiro - RJ

            📍 São Paulo:
            Rua Siqueira Bueno, 1321 - Belém - São Paulo - SP

            ⚠️ Agendamento da parte prática (NR-35 e NR-33) somente por e-mail:
            operacional@jbsegurancadotrabalho.com.br
            Informe o ID da matrícula: %s
        """, idMatricula);

        String orientacoesHtml = """
            <p><strong>Orientações Importantes:</strong></p>
            <ul>
                <li>Você deverá fazer o curso, que é composto por módulos e tópicos.</li>
                <li>Cada tópico possui uma pergunta que deve ser respondida.</li>
                <li>Assim que você responder corretamente, o tópico será marcado como <strong>concluído</strong>.</li>
                <li>Você receberá um e-mail de confirmação a cada tópico concluído.</li>
                <li>Ao final do curso, haverá uma <strong>avaliação final</strong>.</li>
                <li>Após realizar a avaliação, te enviaremos o link para acessá-la.</li>
                <li>Sendo aprovado, você receberá o link para <strong>gerar seu certificado</strong>.</li>
                <li>Os cursos com parte prática podem ser realizados presencialmente na sede da <strong>JB Segurança do Trabalho</strong>.</li>
                <li><strong>Não será cobrado nenhum valor</strong> para a realização da parte prática.</li>
            </ul>
        """;

        String orientacoesTexto = """
            ORIENTAÇÕES IMPORTANTES:

            - Você deverá fazer o curso, que é composto por módulos e tópicos.
            - Cada tópico tem uma pergunta e você deverá respondê-la.
            - Ao responder corretamente, o tópico será marcado como CONCLUÍDO e você receberá um e-mail.
            - Ao final do curso, haverá uma AVALIAÇÃO FINAL.
            - Te enviaremos o link da avaliação. Sendo aprovado, você receberá o link para gerar seu CERTIFICADO.
            - Os cursos com parte prática podem ser realizados presencialmente na sede da JB Segurança do Trabalho.
            - Não será cobrado nenhum valor pela parte prática.
        """;

        String contatosHtml = """
            <br><br>
            <p>📞 Telefones: (21) 3933-1161 | (11) 2694-2399</p>
            <p>📧 E-mail: <a href="mailto:operacional@jbsegurancadotrabalho.com.br">operacional@jbsegurancadotrabalho.com.br</a></p>
            <p>🌐 <a href="https://www.jbsegurancadotrabalho.com.br" target="_blank">www.jbsegurancadotrabalho.com.br</a></p>
            <p>🔗 Siga-nos nas redes sociais:</p>
            <ul>
                <li><a href="https://www.instagram.com/reel/DJ4F32NxUfM/" target="_blank">Instagram</a></li>
                <li><a href="https://www.youtube.com/@JBSeguran%C3%A7adoTrabalhoOficial" target="_blank">YouTube</a></li>
                <li><a href="https://www.linkedin.com/in/jesse-santanna-da-silva-91a408b3/recent-activity/all/" target="_blank">LinkedIn</a></li>
                <li><a href="https://web.facebook.com/jbengenhariadesegurancadotrabalho/?_rdc=1&_rdr#" target="_blank">Facebook</a></li>
                <li><a href="https://www.tiktok.com/@jbsegurancadotrabalho" target="_blank">TikTok</a></li>
            </ul>
        """;

        String contatosTexto = """
            📞 Telefones: (21) 3933-1161 | (11) 2694-2399
            📧 E-mail: operacional@jbsegurancadotrabalho.com.br
            🌐 www.jbsegurancadotrabalho.com.br

            Redes Sociais:
            Instagram: https://www.instagram.com/reel/DJ4F32NxUfM/
            YouTube: https://www.youtube.com/@JBSeguran%C3%A7adoTrabalhoOficial
            LinkedIn: https://www.linkedin.com/in/jesse-santanna-da-silva-91a408b3/recent-activity/all/
            Facebook: https://web.facebook.com/jbengenhariadesegurancadotrabalho/?_rdc=1&_rdr#
            TikTok: https://www.tiktok.com/@jbsegurancadotrabalho
        """;

        if (matricula.getEmail() != null && !matricula.getEmail().isBlank()) {
            MailSenderDto mailDto = new MailSenderDto();
            mailDto.setMailTo(matricula.getEmail());
            mailDto.setSubject("Matrícula Confirmada - Acesso ao Curso Gratuito");
            mailDto.setBody(String.format("""
                <p>Olá <strong>%s</strong>,</p>
                <p>Sua matrícula foi concluída com sucesso!</p>
                <p><strong>Número da Matrícula:</strong> %s</p>
                <p><strong>ID da Matrícula:</strong> %s</p>
                <p><strong>Número da Turma:</strong> %s</p>
                <p>Acesse seu curso gratuito clicando no link abaixo:</p>
                <p><a href="%s" target="_blank">%s</a></p>
                <br>
                %s
                <br>
                %s
                <br>
                <p>Atenciosamente,<br>Equipe JB Segurança do Trabalho</p>
                %s
            """, matricula.getFuncionario(), numeroMatricula, idMatricula, numeroTurma, linkMatricula, linkMatricula, orientacoesHtml, centrosHtml, contatosHtml));
            matriculasMessageProducer.sendMessage(mailDto);
        }

        if (matricula.getWhatsapp() != null && !matricula.getWhatsapp().isBlank()) {
            String numero = matricula.getWhatsapp().replaceAll("[^0-9]", "");
            if (!numero.startsWith("55")) {
                numero = "55" + numero;
            }

            SendMessageZapDTO zapDto = new SendMessageZapDTO();
            zapDto.setPhone(numero);
            zapDto.setMessage(String.format("""
                Olá %s! ✅

                Sua matrícula no curso gratuito foi confirmada!

                Número da Matrícula: %s
                ID da Matrícula: %s
                Número da Turma: %s

                Acesse aqui: %s

                %s

                %s

                Equipe JB Segurança do Trabalho
                %s
            """, matricula.getFuncionario(), numeroMatricula, idMatricula, numeroTurma, linkMatricula, orientacoesTexto, centrosTexto, contatosTexto));
            zApiSenderComponent.sendMessage(zapDto);
        }
    }

}

