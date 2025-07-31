package com.forumhub.domain.usuario;

import com.forumhub.domain.curso.Curso;
import com.forumhub.domain.topico.DtoTopicoRequest;
import com.forumhub.validacoes.ValidacaoException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ValidacoesAutor {

    private final UsuarioRepository usuarioRepository;
    public ValidacoesAutor(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario validarAutor(@Valid Long id) {

        Usuario autor = usuarioRepository.findById(id)
                .orElseThrow(() -> new ValidacaoException("Autor não encontrado...!"));

        return autor;
    }
}
