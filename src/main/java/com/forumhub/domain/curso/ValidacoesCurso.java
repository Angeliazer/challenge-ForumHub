package com.forumhub.domain.curso;

import com.forumhub.validacoes.ValidacaoException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;


@Service
public class ValidacoesCurso {

    private final CursoRepository cursoRepository;
    public ValidacoesCurso(CursoRepository cursoRepository){
        this.cursoRepository = cursoRepository;
    }

    public Curso validarCurso(@Valid Long id) {

        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new ValidacaoException("Curso não encontrado...!"));

        return curso;
    }
}
