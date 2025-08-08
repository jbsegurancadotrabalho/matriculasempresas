package br.com.jbst.controller;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.jbst.DTO.CriarContaResponseDto;
import br.com.jbst.DTO.GetMatriculasDTO;
import br.com.jbst.DTO1.GetFuncionarioCertificadoDTO;
import br.com.jbst.DTO1.GetMatriculasCertificadoDTO;
import br.com.jbst.DTO1.GetTurmasCertificadoDTO;
import br.com.jbst.DTO1.GetUsuarioDTO;
import br.com.jbst.DtoEAD.GetMatriculasEadDto;
import br.com.jbst.services.MatriculasServices;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculasControllers {
	  

	
	 @Autowired
	  MatriculasServices matriculasService;
	
	 @GetMapping("/consultar-matriculas")
	    public ResponseEntity<List<GetMatriculasDTO>> consultarMatriculas() throws Exception {
	        List<GetMatriculasDTO> matriculas = matriculasService.consultarMatriculas();
	        return ResponseEntity.status(HttpStatus.OK).body(matriculas);
	    }

	 @GetMapping("/consultar-matriculas-por-mes")
	 public ResponseEntity<List<GetMatriculasDTO>> consultarMatriculasPorMes(
	         @RequestParam(name = "mes", required = false, defaultValue = "1") int mes) {
	     List<GetMatriculasDTO> matriculas = matriculasService.consultarMatriculasFuncionariosPorMes(mes);
	     return ResponseEntity.status(HttpStatus.OK).body(matriculas);
	 }
	 
	

	 @GetMapping("/usuario/{usuarioId}")
	    public ResponseEntity<List<GetMatriculasDTO>> getMatriculasByUsuarioId(@PathVariable UUID usuarioId) {
	        List<GetMatriculasDTO> matriculas = matriculasService.findMatriculasByUsuarioId(usuarioId);
	        return ResponseEntity.ok(matriculas);
	    }

		@GetMapping("consultar-usuarios")
		public ResponseEntity<List<CriarContaResponseDto>> consultarTodosUsuarios() throws Exception {
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(matriculasService.ConsultarTodosOsUsuarios());	
			
		}
		
		
		 @GetMapping("/usuario-mes-ano-turma/{usuarioId}")
		    public ResponseEntity<?> findMatriculasByUsuarioIdAndMesAno(
		            @PathVariable UUID usuarioId,
		            @RequestParam("mes") int mes,
		            @RequestParam("ano") int ano) {

		        try {
		            // Chama o serviço para buscar as matrículas
		            List<GetMatriculasDTO> matriculas = matriculasService.findMatriculasByUsuarioIdAndMesAno(usuarioId, mes, ano);
		            return ResponseEntity.ok(matriculas);

		        } catch (IllegalArgumentException e) {
		            // Retorna erro de solicitação inválida
		            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

		        } catch (EntityNotFoundException e) {
		            // Retorna erro caso nenhuma matrícula seja encontrada
		            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

		        } catch (Exception e) {
		            // Retorna erro genérico em caso de falhas inesperadas
		            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno no servidor.");
		        }
		    }
		 @GetMapping("/funcionario")
		    public ResponseEntity<List<GetFuncionarioCertificadoDTO>> consultarFuncionarioMatriculas() {
		        try {
		            List<GetFuncionarioCertificadoDTO> matriculas = matriculasService.consultarFuncionarioMatriculas();
		            return ResponseEntity.ok(matriculas); // Retorna HTTP 200 com os dados
		        } catch (Exception e) {
		            // Log do erro para depuração
		            e.printStackTrace();
		            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                                 .body(Collections.emptyList()); // HTTP 500 com lista vazia
		        }
		    }

		 @GetMapping("/certificados")
		    public ResponseEntity<List<GetMatriculasCertificadoDTO>> consultarMatriculasCertificado() {
		        try {
		            // Chama o serviço para buscar as matrículas
		            List<GetMatriculasCertificadoDTO> matriculas = matriculasService.consultarMatriculasCertificado();
		            return ResponseEntity.ok(matriculas); // HTTP 200 com os dados
		        } catch (Exception e) {
		            // Log do erro para depuração
		            e.printStackTrace();
		            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                                 .body(Collections.emptyList()); // HTTP 500 com lista vazia
		        }
		    }
		 
		 @GetMapping("/turmas")
		    public ResponseEntity<List<GetTurmasCertificadoDTO>> consultarTurmasCertificado() {
		        try {
		            List<GetTurmasCertificadoDTO> turmasCertificado = matriculasService.consultarTurmasCertificado();
		            return ResponseEntity.ok(turmasCertificado);
		        } catch (Exception e) {
		            return ResponseEntity.status(500).build(); // Erro interno do servidor
		        }
		    }
		 
		 @GetMapping("/buscar-usuario-funcionario/{idUsuario}")
		    public ResponseEntity<GetUsuarioDTO> consultarUsuarioPorId(@PathVariable UUID idUsuario) {
		        try {
		            GetUsuarioDTO usuarioDTO = matriculasService.consultarUsuarioPorId(idUsuario);
		            return ResponseEntity.ok(usuarioDTO);
		        } catch (IllegalArgumentException e) {
		            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Retorna 404 se o usuário não for encontrado
		        } catch (Exception e) {
		            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Retorna 500 para erros inesperados
		        }
		    }
		 
		 @GetMapping("/{idFuncionario}")
		    public ResponseEntity<GetFuncionarioCertificadoDTO> consultarFuncionarioPorId(@PathVariable UUID idFuncionario) {
		        try {
		            GetFuncionarioCertificadoDTO funcionarioDTO = matriculasService.consultarFuncionarioPorId(idFuncionario);
		            return new ResponseEntity<>(funcionarioDTO, HttpStatus.OK);
		        } catch (IllegalArgumentException e) {
		            // Caso o ID não exista ou seja inválido, retornamos erro 404
		            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		        } catch (Exception e) {
		            // Caso haja outro erro, retornamos erro 500
		            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		        }
		    }
		 
		  @GetMapping("curso-ead/{idMatricula}")
		    public ResponseEntity<GetMatriculasEadDto> buscarMatriculaPorId(@PathVariable UUID idMatricula) {
		        GetMatriculasEadDto dto = matriculasService.buscarMatriculaEadPorId(idMatricula);
		        return ResponseEntity.ok(dto);
		    }

}
