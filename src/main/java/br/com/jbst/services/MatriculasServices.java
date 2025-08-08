package br.com.jbst.services;


import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jbst.DTO.CriarContaResponseDto;
import br.com.jbst.DTO.GetMatriculasDTO;
import br.com.jbst.DTO1.GetFuncionarioCertificadoDTO;
import br.com.jbst.DTO1.GetMatriculasCertificadoDTO;
import br.com.jbst.DTO1.GetTurmasCertificadoDTO;
import br.com.jbst.DTO1.GetUsuarioDTO;
import br.com.jbst.DtoEAD.GetMatriculasEadDto;
import br.com.jbst.DtoEAD.GetTurmasEadDto;
import br.com.jbst.entities.Funcionario;
import br.com.jbst.entities.Matriculas;
import br.com.jbst.entities.Turmas;
import br.com.jbst.entities.Usuario;
import br.com.jbst.repositories.CursoRepository;
import br.com.jbst.repositories.EmpresaRepository;
import br.com.jbst.repositories.EnderecoRepository;
import br.com.jbst.repositories.FuncaoRepository;
import br.com.jbst.repositories.FuncionarioRepository;
import br.com.jbst.repositories.InstrutorRepository;
import br.com.jbst.repositories.MatriculasRepository;
import br.com.jbst.repositories.TurmasRepository;
import br.com.jbst.repositories.UnidadeDeTreinamentoRepository;
import br.com.jbst.repositories.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import br.com.jbst.DTO.SendMessageZapDTO;
import br.com.jbst.components.ZApiSenderComponent;


@Service
public class MatriculasServices {

	
	@Autowired UsuarioRepository usuarioRepository;
	@Autowired EmpresaRepository empresaRepository;
    @Autowired MatriculasRepository matriculasRepository;
    @Autowired FuncionarioRepository funcionarioRepository;
	@Autowired InstrutorRepository instrutorRepository;
    @Autowired EnderecoRepository enderecoRepository;
	@Autowired CursoRepository cursoRepository;
	@Autowired TurmasRepository turmasRepository;
	@Autowired FuncaoRepository funcaoRepository;
	@Autowired UnidadeDeTreinamentoRepository unidadeRepository;
	@Autowired ModelMapper modelMapper;
	@Autowired ZApiSenderComponent zApiSenderComponent;


	 public List<GetMatriculasDTO> consultarMatriculas() throws Exception {
	        List<Matriculas> matriculas = matriculasRepository.findAllMatriculas();
	        return mapToDTOList(matriculas);
	    }

	    public List<GetMatriculasDTO> consultarMatriculasFuncionariosPorMes(int mes) {
	        List<Matriculas> matriculasFuncionarios = matriculasRepository.findMatriculasFuncionariosByMes(mes);
	        return mapToDTOList(matriculasFuncionarios);
	    }

	    private List<GetMatriculasDTO> mapToDTOList(List<Matriculas> matriculas) {
	        return modelMapper.map(matriculas, new TypeToken<List<GetMatriculasDTO>>() {}.getType());
	    }
	    
	    @Transactional
	    public List<GetMatriculasDTO> findMatriculasByUsuarioId(UUID usuarioId) {
	        List<Matriculas> matriculas = matriculasRepository.findMatriculasByUsuarioId(usuarioId);
	        return matriculas.stream()
	                .map(matricula -> modelMapper.map(matricula, GetMatriculasDTO.class))
	                .collect(Collectors.toList());
	    }
	    
	    
	    @Transactional
	    public List<GetMatriculasDTO> findMatriculasByUsuarioIdAndMesAno(UUID usuarioId, int mes, int ano) {
	        // Valida os parâmetros
	        if (mes < 1 || mes > 12) {
	            throw new IllegalArgumentException("O mês deve estar entre 1 e 12.");
	        }
	        if (ano < 1900 || ano > LocalDate.now().getYear()) {
	            throw new IllegalArgumentException("O ano é inválido.");
	        }

	        // Busca as matrículas no repositório
	        List<Matriculas> matriculas = matriculasRepository.findMatriculasByUsuarioIdAndMesAno(usuarioId, mes, ano);

	        // Verifica se há matrículas encontradas
	        if (matriculas.isEmpty()) {
	            throw new EntityNotFoundException("Nenhuma matrícula encontrada para o usuário no período informado.");
	        }

	        // Mapeia as matrículas para DTOs
	        return matriculas.stream()
	                .map(matricula -> modelMapper.map(matricula, GetMatriculasDTO.class))
	                .collect(Collectors.toList());
	    }


	    
	    public List<CriarContaResponseDto> ConsultarTodosOsUsuarios() throws Exception {

			List<Usuario> usuario =usuarioRepository.findAll();
			List<CriarContaResponseDto> lista = modelMapper.map(usuario, new TypeToken<List<CriarContaResponseDto>>() {
			}.getType());
			return lista;
		}
	    
	    public List<GetFuncionarioCertificadoDTO> consultarFuncionarioMatriculas() throws Exception {
	        List<Funcionario> funcionario = funcionarioRepository.findAll();
	        return mapToDTOListFuncionarioMatriculas(funcionario);
	    }

	    private List<GetFuncionarioCertificadoDTO> mapToDTOListFuncionarioMatriculas(List<Funcionario> funcionario) {
	        return modelMapper.map(funcionario, new TypeToken<List<GetFuncionarioCertificadoDTO>>() {}.getType());
	    }
	    
	    public List<GetMatriculasCertificadoDTO> consultarMatriculasCertificado() throws Exception {
	        List<Matriculas> matriculas = matriculasRepository.findAll();
	        return mapToDTOListMatriculasCertificado(matriculas);
	    }

	    private List<GetMatriculasCertificadoDTO> mapToDTOListMatriculasCertificado(List<Matriculas> matriculas) {
	        return modelMapper.map(matriculas, new TypeToken<List<GetMatriculasCertificadoDTO>>() {}.getType());
	    }
	    
	    public List<GetTurmasCertificadoDTO> consultarTurmasCertificado() throws Exception {
	        List<Turmas> turmas = turmasRepository.findAll();
	        return mapToDTOListTurmasCertificado(turmas);
	    }

	    private List<GetTurmasCertificadoDTO> mapToDTOListTurmasCertificado(List<Turmas> turmas) {
	        return modelMapper.map(turmas, new TypeToken<List< GetTurmasCertificadoDTO>>() {}.getType());
	    }
	    
	    
	    
	    public GetUsuarioDTO consultarUsuarioPorId(UUID idUsuario) throws Exception {
	        // Verificar se o usuário existe pelo ID fornecido
	        Usuario usuario = usuarioRepository.findById(idUsuario)
	                .orElseThrow(() -> new IllegalArgumentException("Usuário inválido: " + idUsuario));

	        // Mapear o usuário para o DTO
	        return modelMapper.map(usuario, GetUsuarioDTO.class);
	    }
	    
	    public GetFuncionarioCertificadoDTO consultarFuncionarioPorId(UUID idFuncionario) throws Exception {
	        // Verificar se o usuário existe pelo ID fornecido
	        Funcionario funcionario = funcionarioRepository.findById(idFuncionario)
	                .orElseThrow(() -> new IllegalArgumentException("Usuário inválido: " + idFuncionario));

	        // Mapear o usuário para o DTO
	        return modelMapper.map(funcionario, GetFuncionarioCertificadoDTO.class);
	    }

	    public GetMatriculasEadDto buscarMatriculaEadPorId(UUID idMatricula) {
	        Matriculas matricula = matriculasRepository.findById(idMatricula)
	            .orElseThrow(() -> new RuntimeException("Matrícula não encontrada"));
	        GetMatriculasEadDto dto = modelMapper.map(matricula, GetMatriculasEadDto.class);
	        GetTurmasEadDto turmasDto = modelMapper.map(matricula.getTurmas(), GetTurmasEadDto		.class);
	        dto.setTurmas(turmasDto);
	        return dto;
	    }
	    
	    
	    private void enviarMensagemWhatsapp(Matriculas matricula) {
	        Funcionario funcionario = matricula.getFuncionario();
	        if (funcionario != null && funcionario.getWhatsapp_funcionario() != null) {
	            String numero = funcionario.getWhatsapp_funcionario();

	            // Remover espaços e validar o número
	            numero = numero.replaceAll("[^0-9]", "");
	            if (!numero.startsWith("55")) {
	                numero = "55" + numero;
	            }

	            String mensagem = String.format("Olá %s! Sua matrícula nº %d no curso foi registrada com sucesso. Em breve você receberá mais informações.",
	                    funcionario.getNome(), matricula.getNumeroMatricula());

	            SendMessageZapDTO dto = new SendMessageZapDTO(numero, mensagem);
	            zApiSenderComponent.sendMessage(dto);
	        }
	    }
	    
	}
	 	



