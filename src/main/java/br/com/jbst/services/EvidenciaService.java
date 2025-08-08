package br.com.jbst.services;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import br.com.jbst.DtoEAD.GetEvidenciaEmpresaDTO;
import br.com.jbst.DtoEAD.PostEvidenciaEmpresaDTO;
import br.com.jbst.DtoEAD.PutEvidenciaEmpresaDTO;
import br.com.jbst.entities.Empresa;
import br.com.jbst.entities.Evidencias;
import br.com.jbst.entities.Turmas;
import br.com.jbst.repositories.EmpresaRepository;
import br.com.jbst.repositories.EvidenciasRepository;
import br.com.jbst.repositories.MatriculasRepository;
import br.com.jbst.repositories.TurmasRepository;
import br.com.jbst.components.MatriculasMessageProducer;

@Service
public class EvidenciaService {

    @Autowired
    private EvidenciasRepository evidenciasRepository;
    
    @Autowired
    private MatriculasRepository matriculasRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private TurmasRepository turmasRepository;

    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private MatriculasMessageProducer matriculasMessageProducer;

    public GetEvidenciaEmpresaDTO criarEvidencia(PostEvidenciaEmpresaDTO dto, byte[] evidenciaBytes) {
        
        Evidencias evidencia = new Evidencias();
        evidencia.setIdEvidencias(UUID.randomUUID());
        evidencia.setDataHoraCriacao(Instant.now());
        evidencia.setNome(dto.getNome());
        evidencia.setDescricao(dto.getDescricao());
        evidencia.setInserir_evidencias(evidenciaBytes);

        Empresa empresa = null;
        Turmas turma = null;

        if (dto.getIdEmpresa() != null) {
            empresa = empresaRepository.findById(dto.getIdEmpresa())
                    .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));
            evidencia.setEmpresa(empresa);
        }

        if (dto.getIdTurmas() != null) {
            turma = turmasRepository.findById(dto.getIdTurmas())
                    .orElseThrow(() -> new RuntimeException("Turma não encontrada"));
            evidencia.setTurmas(turma);
        }

        Evidencias evidenciaSalva = evidenciasRepository.save(evidencia);

     
        return modelMapper.map(evidenciaSalva, GetEvidenciaEmpresaDTO.class);
    }

    

    public GetEvidenciaEmpresaDTO editarEvidencia(PutEvidenciaEmpresaDTO dto) {
        Evidencias evidenciaExistente = evidenciasRepository.findById(dto.getIdEvidencias())
                .orElseThrow(() -> new RuntimeException("Evidência não encontrada"));

        modelMapper.map(dto, evidenciaExistente);

        Evidencias evidenciaAtualizada = evidenciasRepository.save(evidenciaExistente);
        return modelMapper.map(evidenciaAtualizada, GetEvidenciaEmpresaDTO.class);
    }

    public GetEvidenciaEmpresaDTO consultarPorId(UUID id) {
        Evidencias evidencia = evidenciasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evidência não encontrada"));
        return modelMapper.map(evidencia, GetEvidenciaEmpresaDTO.class);
    }

    public List<GetEvidenciaEmpresaDTO> consultarTodas() {
        List<Evidencias> evidencias = evidenciasRepository.findAll();
        return evidencias.stream()
                .map(evidencia -> modelMapper.map(evidencia, GetEvidenciaEmpresaDTO.class))
                .collect(Collectors.toList());
    }
}
