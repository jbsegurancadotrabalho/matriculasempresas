package br.com.jbst.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jbst.DTO.GetFormacaoDTO;
import br.com.jbst.entities.Formacao;
import br.com.jbst.repositories.FormacaoRepository;
import br.com.jbst.repositories.InstrutorRepository;

@Service
public class FormacaoService {

    @Autowired
    FormacaoRepository formacaoRepository;
 
    @Autowired
    InstrutorRepository instrutorRepository;
    
    @Autowired
    ModelMapper modelMapper;

  

    public List<GetFormacaoDTO> consultarFormacao() {
		List<Formacao> formacoes = formacaoRepository.findAll();
		return formacoes.stream().map(pf -> modelMapper.map(pf, GetFormacaoDTO.class))
				.collect(Collectors.toList());
	}

}

