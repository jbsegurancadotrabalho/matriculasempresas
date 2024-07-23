package br.com.jbst.services;


import java.util.List;

import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jbst.DTO.CriarContaResponseDto;
import br.com.jbst.DTO.GetMatriculasDTO;
import br.com.jbst.entities.Matriculas;
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
import ch.qos.logback.classic.Logger;
import jakarta.transaction.Transactional;




@Service
public class MatriculasServices {

	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	EmpresaRepository empresaRepository;

	@Autowired
	MatriculasRepository matriculasRepository;

	@Autowired
	FuncionarioRepository funcionarioRepository;

	@Autowired
	InstrutorRepository instrutorRepository;

	@Autowired
	EnderecoRepository enderecoRepository;
	@Autowired
	CursoRepository cursoRepository;

	@Autowired
	TurmasRepository turmasRepository;

	@Autowired
	FuncaoRepository funcaoRepository;

	@Autowired
	UnidadeDeTreinamentoRepository unidadeRepository;

	@Autowired
	ModelMapper modelMapper;


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
	    
	    
	    
	    public List<CriarContaResponseDto> ConsultarTodosOsUsuarios() throws Exception {

			List<Usuario> usuario =usuarioRepository.findAll();
			List<CriarContaResponseDto> lista = modelMapper.map(usuario, new TypeToken<List<CriarContaResponseDto>>() {
			}.getType());
			return lista;
		}
		
	}
	 



