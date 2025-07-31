package com.forumhub.domain.usuario;

import com.forumhub.validacoes.ValidacaoException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class ValidacoesAutor {

    private final UsuarioRepository usuarioRepository;
    public ValidacoesAutor(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario validarAutor(@Valid Long id) {

        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ValidacaoException("Autor não encontrado...!"));
    }
}
