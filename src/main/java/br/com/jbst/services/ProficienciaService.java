package br.com.jbst.services;

import java.util.List;



import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jbst.DTO.GetProficienciaDTO;
import br.com.jbst.entities.Proficiencia;
import br.com.jbst.repositories.ProficienciaRepository;

@Service
public class ProficienciaService {

    @Autowired
    private ProficienciaRepository proficienciaRepository;



    @Autowired
    private ModelMapper modelMapper;
   
	public List<GetProficienciaDTO> listarTodas() throws Exception {
		List<Proficiencia> proficiencia = proficienciaRepository.findAll();
		List<GetProficienciaDTO> lista = modelMapper.map(proficiencia, new TypeToken<List<GetProficienciaDTO>>() {
		}.getType());
		return lista;
	}

}
