package com.forumhub.service;

import com.forumhub.domain.curso.Curso;
import com.forumhub.domain.curso.CursoRepository;
import com.forumhub.validacoes.ValidacaoException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;
    public CursoService(CursoRepository cursoRepository){
        this.cursoRepository = cursoRepository;
    }

    public Curso validarCurso(@Valid Long id) {

        return cursoRepository.findById(id)
                .orElseThrow(() -> new ValidacaoException("Curso não encontrado...!"));

    }
}
