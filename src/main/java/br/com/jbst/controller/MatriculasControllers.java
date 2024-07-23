package br.com.jbst.controller;

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

import br.com.jbst.services.MatriculasServices;

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

}
